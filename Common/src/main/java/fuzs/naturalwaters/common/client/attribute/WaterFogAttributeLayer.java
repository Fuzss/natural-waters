package fuzs.naturalwaters.common.client.attribute;

import net.minecraft.core.Holder;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.GaussianSampler;
import net.minecraft.world.attribute.SpatialAttributeInterpolator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/**
 * Positional environment attribute layer replacing the vanilla biome layer for the water fog attributes.
 * <p>
 * Biomes are sampled the same way {@link net.minecraft.world.attribute.EnvironmentAttributeProbe} does it, but from
 * partially overridden {@link net.minecraft.world.attribute.EnvironmentAttributeMap}s which include the client biome
 * overrides while keeping the raw biome values as fallbacks. The expensive gaussian sampling is cached and only redone
 * when the sample position or {@link BiomeEnvironmentAttributes#getTimesChanged()} changes.
 */
public final class WaterFogAttributeLayer {
    private final BiomeManager biomeManager;
    private final SpatialAttributeInterpolator interpolator = new SpatialAttributeInterpolator();
    private @Nullable Vec3 lastPosition;
    private int lastTimesChanged = -1;

    public WaterFogAttributeLayer(BiomeManager biomeManager) {
        this.biomeManager = biomeManager;
    }

    public Vector3fc applyColor(Vector3fc baseValue, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator) {
        if (!BiomeEnvironmentAttributes.isActive() && biomeInterpolator != null) {
            // no overrides required, reuse the interpolation already built by the probe
            return biomeInterpolator.applyAttributeLayer(EnvironmentAttributes.WATER_FOG_COLOR, baseValue);
        }

        this.updateInterpolator(pos);
        return this.interpolator.applyAttributeLayer(EnvironmentAttributes.WATER_FOG_COLOR, baseValue);
    }

    public Float applyDistance(Float baseValue, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator) {
        if (!BiomeEnvironmentAttributes.isActive() && biomeInterpolator != null) {
            // no overrides required, reuse the interpolation already built by the probe
            return biomeInterpolator.applyAttributeLayer(EnvironmentAttributes.WATER_FOG_END_DISTANCE, baseValue);
        }

        this.updateInterpolator(pos);
        return this.interpolator.applyAttributeLayer(EnvironmentAttributes.WATER_FOG_END_DISTANCE, baseValue);
    }

    private void updateInterpolator(Vec3 pos) {
        int timesChanged = BiomeEnvironmentAttributes.getTimesChanged();
        if (this.lastTimesChanged == timesChanged && this.lastPosition != null && this.lastPosition.equals(pos)) {
            return;
        }

        this.lastPosition = pos;
        this.lastTimesChanged = timesChanged;
        this.interpolator.clear();
        GaussianSampler.sample(pos.scale(0.25),
                this.biomeManager::getNoiseBiomeAtQuart,
                (double weight, Holder<Biome> holder) -> {
                    this.interpolator.accumulate(weight, BiomeEnvironmentAttributes.get(holder));
                });
    }
}
