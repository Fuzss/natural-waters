package fuzs.naturalwaters.mixin.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WaterFogEnvironment.class)
abstract class WaterFogEnvironmentMixin {

    @ModifyVariable(
            method = "getBaseColor", at = @At("STORE"), ordinal = 1
    )
    public int getBaseColor(int waterFogColor, ClientLevel level, Camera camera) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogColor) return waterFogColor;
        Holder<Biome> holder = level.getBiome(camera.getBlockPosition());
        return ClientBiomeManager.getBiomeClientInfo(holder).getWaterFogColor().orElse(waterFogColor);
    }
}
