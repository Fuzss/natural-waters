package fuzs.naturalwaters.client.biome;

import com.google.common.collect.ImmutableMap;
import fuzs.naturalwaters.NaturalWaters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

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
    private Map<ResourceKey<Biome>, BiomeClientInfo> biomeClientInfos = Map.of();
    private Map<ResourceKey<Biome>, BiomeClientInfo> resolvedBiomeClientInfos = Map.of();

    public ClientBiomeManager() {
        super(BiomeClientInfo.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<ResourceLocation, BiomeClientInfo> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.biomeClientInfos = this.resolvedBiomeClientInfos = map.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap((Map.Entry<ResourceLocation, BiomeClientInfo> entry) -> ResourceKey.create(
                        Registries.BIOME,
                        entry.getKey()), Map.Entry::getValue));
        ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
        if (clientPacketListener != null) {
            this.resolvedBiomeClientInfos = fillMissingBiomeClientInfos(clientPacketListener.registryAccess()
                    .lookupOrThrow(Registries.BIOME), new IdentityHashMap<>(this.biomeClientInfos));
        }
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

    public static BiomeClientInfo getBiomeClientInfo(Holder<Biome> holder) {
        return holder.unwrapKey().map(ClientBiomeManager::getBiomeClientInfo).orElse(BUILT_IN_FALLBACK);
    }

    public static BiomeClientInfo getBiomeClientInfo(ResourceKey<Biome> resourceKey) {
        ClientBiomeManager clientBiomeManager = instance;
        if (clientBiomeManager != null) {
            return clientBiomeManager.resolvedBiomeClientInfos.getOrDefault(resourceKey, BUILT_IN_FALLBACK);
        } else {
            return BUILT_IN_FALLBACK;
        }
    }

    public static void onAddResourcePackReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> consumer) {
        consumer.accept(NaturalWaters.id("client_biome_manager"), instance = new ClientBiomeManager());
    }

    public static void onTagsUpdated(HolderLookup.Provider registries, boolean client) {
        if (client) {
            ClientBiomeManager clientBiomeManager = instance;
            if (clientBiomeManager != null) {
                clientBiomeManager.resolvedBiomeClientInfos = fillMissingBiomeClientInfos(registries.lookupOrThrow(
                        Registries.BIOME), new IdentityHashMap<>(clientBiomeManager.biomeClientInfos));
            }
        }
    }

    static Map<ResourceKey<Biome>, BiomeClientInfo> fillMissingBiomeClientInfos(HolderLookup.RegistryLookup<Biome> biomeLookup, Map<ResourceKey<Biome>, BiomeClientInfo> biomeClientInfos) {
        biomeLookup.listElements().forEach((Holder.Reference<Biome> holder) -> {
            if (!biomeClientInfos.containsKey(holder.key())) {
                ModBiomeClientInfos.pick(holder)
                        .ifPresent((BiomeClientInfo biomeClientInfo) -> biomeClientInfos.put(holder.key(),
                                biomeClientInfo));
            }
        });

        return ImmutableMap.copyOf(biomeClientInfos);
    }
}
