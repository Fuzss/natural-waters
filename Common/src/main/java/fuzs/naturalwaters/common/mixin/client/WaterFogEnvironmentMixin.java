package fuzs.naturalwaters.common.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.handler.WaterFogHandler;
import fuzs.naturalwaters.common.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.util.ARGB;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFogEnvironment.class)
abstract class WaterFogEnvironmentMixin {

    @ModifyReturnValue(method = "getBaseColor", at = @At("RETURN"))
    public Vector3fc getBaseColor(Vector3fc waterFogColor, ClientLevel clientLevel, Camera camera, int renderDistance, float partialTick) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogColor) {
            return waterFogColor;
        }

        return WaterFogHandler.getWaterFogBaseColor(clientLevel, camera, renderDistance, partialTick)
                .<Vector3fc>map(ARGB::vector3fFromRGB24)
                .orElse(waterFogColor);
    }
}
