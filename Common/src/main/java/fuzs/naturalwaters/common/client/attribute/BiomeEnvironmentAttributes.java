package fuzs.naturalwaters.common.client.attribute;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.biome.BiomeClientInfo;
import fuzs.naturalwaters.common.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.common.config.ClientConfig;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.biome.Biome;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Overlays client-only biome water values onto {@link EnvironmentAttributeMap} instances so that the built-in
 * {@link net.minecraft.world.attribute.EnvironmentAttributeSystem} biome layer picks them up and interpolates them like
 * any other biome attribute.
 */
public final class BiomeEnvironmentAttributes {
    private static final Map<Biome, EnvironmentAttributeMap> BIOME_ATTRIBUTES_CACHE = new IdentityHashMap<>();

    private BiomeEnvironmentAttributes() {
        // NO-OP
    }

    public static EnvironmentAttributeMap get(Biome biome, EnvironmentAttributeMap original) {
        return BIOME_ATTRIBUTES_CACHE.computeIfAbsent(biome, (Biome key) -> create(key, original));
    }

    private static EnvironmentAttributeMap create(Biome biome, EnvironmentAttributeMap attributes) {
        ClientConfig config = NaturalWaters.CONFIG.get(ClientConfig.class);
        BiomeClientInfo biomeClientInfo = ClientBiomeManager.getBiomeClientInfo(biome);
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
        BIOME_ATTRIBUTES_CACHE.clear();
    }
}
