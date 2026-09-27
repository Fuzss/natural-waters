package fuzs.naturalwaters.common.mixin.client;

import fuzs.naturalwaters.common.client.attribute.BiomeEnvironmentAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.GaussianSampler;
import net.minecraft.world.attribute.SpatialAttributeInterpolator;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnvironmentAttributeProbe.class)
abstract class EnvironmentAttributeProbeMixin {
    @Shadow
    @Final
    private SpatialAttributeInterpolator biomeInterpolator;

    @ModifyArg(method = "tick",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/attribute/GaussianSampler;sample(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/GaussianSampler$Sampler;Lnet/minecraft/world/attribute/GaussianSampler$Accumulator;)V"))
    public GaussianSampler.Accumulator<Holder<Biome>> tick(GaussianSampler.Accumulator<Holder<Biome>> accumulator) {
        // Feed the partially overridden biome attribute maps into the vanilla interpolator.
        // This makes the vanilla biome layer blend them without any additional sampling.
        return (double weight, Holder<Biome> holder) -> this.biomeInterpolator.accumulate(weight,
                BiomeEnvironmentAttributes.get(holder));
    }
}
