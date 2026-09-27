package fuzs.naturalwaters.common.client.biome;

import com.google.common.collect.ImmutableMap;
import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.attribute.BiomeEnvironmentAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class ClientBiomeManager extends SimpleJsonResourceReloadListener<BiomeClientInfo> {
    public static final String ASSET_DIRECTORY = NaturalWaters.MOD_ID + "/biomes";
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json(ASSET_DIRECTORY);
    static final BiomeClientInfo BUILT_IN_FALLBACK = new BiomeClientInfo(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    @Nullable
    private static ClientBiomeManager instance;
    private Map<ResourceKey<Biome>, BiomeClientInfo> loaded = Map.of();
    private Map<ResourceKey<Biome>, BiomeClientInfo> resolved = Map.of();

    public ClientBiomeManager() {
        super(BiomeClientInfo.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<Identifier, BiomeClientInfo> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.loaded = this.resolved = map.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap((Map.Entry<Identifier, BiomeClientInfo> entry) -> {
                    return ResourceKey.create(Registries.BIOME, entry.getKey());
                }, Map.Entry::getValue));
        ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
        if (clientPacketListener != null) {
            this.resolved = fillMissingBiomeClientInfos(clientPacketListener.registryAccess()
                    .lookupOrThrow(Registries.BIOME), new IdentityHashMap<>(this.loaded));
        }

        BiomeEnvironmentAttributes.clear();
    }

    public static BiomeClientInfo getBiomeClientInfo(Biome biome) {
        // never know what mods are going to do here outside a loaded level, so be extra careful
        ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
        if (clientPacketListener != null) {
            return clientPacketListener.registryAccess()
                    .lookup(Registries.BIOME)
                    .flatMap((Registry<Biome> registry) -> registry.getResourceKey(biome))
                    .map(ClientBiomeManager::getBiomeClientInfo)
                    .orElse(BUILT_IN_FALLBACK);
        } else {
            return BUILT_IN_FALLBACK;
        }
    }

    public static BiomeClientInfo getBiomeClientInfo(ResourceKey<Biome> resourceKey) {
        ClientBiomeManager clientBiomeManager = instance;
        if (clientBiomeManager != null) {
            return clientBiomeManager.resolved.getOrDefault(resourceKey, BUILT_IN_FALLBACK);
        } else {
            return BUILT_IN_FALLBACK;
        }
    }

    public static void onAddResourcePackReloadListeners(BiConsumer<Identifier, PreparableReloadListener> consumer) {
        consumer.accept(NaturalWaters.id("client_biome_manager"), instance = new ClientBiomeManager());
    }

    public static void onClientTagsUpdated(RegistryAccess registryAccess) {
        ClientBiomeManager clientBiomeManager = instance;
        if (clientBiomeManager != null) {
            clientBiomeManager.resolved = fillMissingBiomeClientInfos(registryAccess.lookupOrThrow(Registries.BIOME),
                    new IdentityHashMap<>(clientBiomeManager.loaded));
        }

        BiomeEnvironmentAttributes.clear();
    }

    private static Map<ResourceKey<Biome>, BiomeClientInfo> fillMissingBiomeClientInfos(HolderLookup.RegistryLookup<Biome> biomeLookup, Map<ResourceKey<Biome>, BiomeClientInfo> infos) {
        biomeLookup.listElements().forEach((Holder.Reference<Biome> holder) -> {
            if (!infos.containsKey(holder.key())) {
                ModBiomeClientInfos.pick(holder).ifPresent((BiomeClientInfo info) -> infos.put(holder.key(), info));
            }
        });

        return ImmutableMap.copyOf(infos);
    }
}
