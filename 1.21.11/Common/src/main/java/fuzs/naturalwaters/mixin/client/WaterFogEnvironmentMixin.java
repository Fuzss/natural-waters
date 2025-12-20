package fuzs.naturalwaters.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.handler.WaterFogHandler;
import fuzs.naturalwaters.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFogEnvironment.class)
abstract class WaterFogEnvironmentMixin {

    @ModifyReturnValue(method = "getBaseColor", at = @At("RETURN"))
    public int getBaseColor(int waterFogColor, ClientLevel clientLevel, Camera camera, int renderDistance, float partialTick) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogColor) {
            return waterFogColor;
        }

        return WaterFogHandler.getWaterFogBaseColor(clientLevel, camera, renderDistance, partialTick)
                .orElse(waterFogColor);
    }
}
