package fuzs.naturalwaters.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.config.ClientConfig;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
abstract class FogRendererMixin {

    @WrapOperation(
            method = "computeFogColor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I")
    )
    private static int computeFogColor(Biome biome, Operation<Integer> operation) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogColor) return operation.call(biome);
        return ClientBiomeManager.getBiomeClientInfo(biome).getWaterFogColor().orElseGet(() -> operation.call(biome));
    }
}
