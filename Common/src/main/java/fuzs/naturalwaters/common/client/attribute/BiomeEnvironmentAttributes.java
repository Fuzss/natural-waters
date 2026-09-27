package fuzs.naturalwaters.common.client.attribute;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.biome.BiomeClientInfo;
import fuzs.naturalwaters.common.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.common.config.ClientConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Overlays client-only biome water values onto {@link EnvironmentAttributeMap} instances so that the built-in
 * {@link net.minecraft.world.attribute.EnvironmentAttributeSystem} biome layer picks them up and interpolates them like
 * any other biome attribute.
 * <p>
 * The overlays are built eagerly during registry synchronization in {@link ClientBiomeManager}, keyed by {@link Biome}
 * identity, so {@link Biome#getAttributes()} only needs a cache lookup and never depends on the client connection.
 */
public final class BiomeEnvironmentAttributes {
    private static Map<Biome, EnvironmentAttributeMap> cachedAttributesByBiome = Map.of();
    private static HolderLookup.@Nullable RegistryLookup<Biome> biomeLookup;
    private static boolean rebuilding;

    private BiomeEnvironmentAttributes() {
        // NO-OP
    }

    public static EnvironmentAttributeMap get(Biome biome, EnvironmentAttributeMap original) {
        // while rebuilding let the raw biome attributes pass through so they can be read from an unmodified map
        if (rebuilding) {
            return original;
        }

        return cachedAttributesByBiome.getOrDefault(biome, original);
    }

    public static void rebuild(HolderLookup.RegistryLookup<Biome> biomeLookup) {
        BiomeEnvironmentAttributes.biomeLookup = biomeLookup;
        rebuild();
    }

    public static void rebuild() {
        HolderLookup.RegistryLookup<Biome> biomeLookup = BiomeEnvironmentAttributes.biomeLookup;
        if (biomeLookup == null) {
            return;
        }

        rebuilding = true;
        try {
            IdentityHashMap<Biome, EnvironmentAttributeMap> rebuiltAttributes = new IdentityHashMap<>();
            biomeLookup.listElements().forEach((Holder.Reference<Biome> holder) -> {
                rebuiltAttributes.put(holder.value(),
                        create(holder.value().getAttributes(), ClientBiomeManager.getBiomeClientInfo(holder.key())));
            });
            cachedAttributesByBiome = rebuiltAttributes;
        } finally {
            rebuilding = false;
        }
    }

    private static EnvironmentAttributeMap create(EnvironmentAttributeMap attributes, BiomeClientInfo biomeClientInfo) {
        ClientConfig config = NaturalWaters.CONFIG.get(ClientConfig.class);
        EnvironmentAttributeMap.Builder builder = EnvironmentAttributeMap.builder().putAll(attributes);
        boolean updated = false;

        if (config.waterFogColor) {
            Optional<Integer> waterFogColor = biomeClientInfo.getWaterFogColor();
            if (waterFogColor.isPresent()) {
                builder.set(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(waterFogColor.get()));
                updated = true;
            }
        }

        if (config.waterFogDistance) {
            Optional<Float> waterFogDistance = biomeClientInfo.waterFogDistance();
            if (waterFogDistance.isPresent()) {
                // Multiply to compose with the value already provided by dimensions or data packs.
                builder.modify(EnvironmentAttributes.WATER_FOG_END_DISTANCE,
                        FloatModifier.MULTIPLY,
                        waterFogDistance.get());
                updated = true;
            }
        }

        return updated ? builder.build() : attributes;
    }

    public static void clear() {
        cachedAttributesByBiome = Map.of();
        biomeLookup = null;
    }
}
