package fuzs.naturalwaters.common.client.attribute;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.biome.BiomeClientInfo;
import fuzs.naturalwaters.common.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.common.config.ClientConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Overlays client-only biome water values onto {@link EnvironmentAttributeMap} instances which are sampled by
 * {@link net.minecraft.world.attribute.EnvironmentAttributeProbe}, so that the vanilla biome layer blends them without
 * any additional biome sampling.
 * <p>
 * The overlays are built from the holder of a sampled biome, so the biome resource key is always available, and no
 * client connection is required. Instances are cached per biome key and invalidated via {@link #clear()}.
 */
public final class BiomeEnvironmentAttributes {
    private static final Map<ResourceKey<Biome>, EnvironmentAttributeMap> BIOME_ATTRIBUTES_CACHE = new HashMap<>();

    private BiomeEnvironmentAttributes() {
        // NO-OP
    }

    public static EnvironmentAttributeMap get(Holder<Biome> holder) {
        EnvironmentAttributeMap attributes = holder.value().getAttributes();
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).applyCustomEnvironmentAttributes()) {
            return attributes;
        }

        return holder.unwrapKey().map((ResourceKey<Biome> biomeKey) -> {
            return BIOME_ATTRIBUTES_CACHE.computeIfAbsent(biomeKey, (ResourceKey<Biome> key) -> {
                return create(key, attributes);
            });
        }).orElse(attributes);
    }

    private static EnvironmentAttributeMap create(ResourceKey<Biome> resourceKey, EnvironmentAttributeMap original) {
        ClientConfig config = NaturalWaters.CONFIG.get(ClientConfig.class);
        BiomeClientInfo biomeClientInfo = ClientBiomeManager.getBiomeClientInfo(resourceKey);
        EnvironmentAttributeMap.Builder builder = EnvironmentAttributeMap.builder().putAll(original);
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

        return updated ? builder.build() : original;
    }

    public static void clear() {
        BIOME_ATTRIBUTES_CACHE.clear();
    }
}
