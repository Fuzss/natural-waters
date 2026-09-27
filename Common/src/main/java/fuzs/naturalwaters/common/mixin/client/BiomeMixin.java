package fuzs.naturalwaters.common.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.naturalwaters.common.client.attribute.BiomeEnvironmentAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
abstract class BiomeMixin {
    @ModifyReturnValue(method = "getAttributes", at = @At("RETURN"))
    private EnvironmentAttributeMap naturalwaters$getAttributes(EnvironmentAttributeMap attributes) {
        // Only apply the client-only overrides on the client thread, the integrated server shares this class.
        if (Thread.currentThread() != Minecraft.getInstance().getRunningThread()) {
            return attributes;
        }

        return BiomeEnvironmentAttributes.get(Biome.class.cast(this), attributes);
    }
}
