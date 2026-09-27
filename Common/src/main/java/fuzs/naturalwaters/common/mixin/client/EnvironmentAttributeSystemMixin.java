package fuzs.naturalwaters.common.mixin.client;

import fuzs.naturalwaters.common.client.attribute.BiomeEnvironmentAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnvironmentAttributeSystem.class)
abstract class EnvironmentAttributeSystemMixin {

    @Inject(method = "addStaticLayers", at = @At("RETURN"))
    private static void naturalwaters$addStaticLayers(EnvironmentAttributeSystem.Builder builder, LevelAccessor level, CallbackInfo callbackInfo) {
        if (level.isClientSide()) {
            // replace the suppressed vanilla biome layer in the exact same position of the layer order
            BiomeEnvironmentAttributes.addLayers(builder, level.getBiomeManager());
        }
    }

    @Inject(method = "addBiomeLayerForAttribute", at = @At("HEAD"), cancellable = true)
    private static void naturalwaters$addBiomeLayerForAttribute(EnvironmentAttributeSystem.Builder builder, EnvironmentAttribute<?> attribute, BiomeManager biomeManager, CallbackInfo callbackInfo) {
        // the integrated server shares this class, but it must keep its vanilla biome layers
        if (Thread.currentThread() != Minecraft.getInstance().getRunningThread()) {
            return;
        }

        if (attribute == EnvironmentAttributes.WATER_FOG_COLOR
                || attribute == EnvironmentAttributes.WATER_FOG_END_DISTANCE) {
            callbackInfo.cancel();
        }
    }
}
