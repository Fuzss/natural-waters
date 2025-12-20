package fuzs.naturalwaters.client.handler;

import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

import java.util.Optional;

/**
 * Copied from Minecraft 1.21.10's {@code WaterFogEnvironment} class.
 * <p>
 * This bypasses the whole new {@link net.minecraft.world.attribute.EnvironmentAttributeSystem} based on
 * {@link net.minecraft.world.attribute.EnvironmentAttribute} for now, until this implementation is overhauled.
 */
public class WaterFogHandler {
    private static int targetBiomeFog = -1;
    private static int previousBiomeFog = -1;
    private static long biomeChangedTime = -1L;

    public static Optional<Integer> getWaterFogBaseColor(ClientLevel clientLevel, Camera camera, int renderDistance, float partialTick) {
        long timeInMillis = Util.getMillis();
        Optional<Integer> waterFogColor = ClientBiomeManager.getBiomeClientInfo(clientLevel.getBiome(camera.blockPosition()))
                .getWaterFogColor();
        if (waterFogColor.isEmpty()) {
            return Optional.empty();
        }

        if (biomeChangedTime < 0L) {
            targetBiomeFog = waterFogColor.get();
            previousBiomeFog = waterFogColor.get();
            biomeChangedTime = timeInMillis;
        }

        float interpolationTime = Mth.clamp((timeInMillis - biomeChangedTime) / 5000.0F, 0.0F, 1.0F);
        int interpolatedWaterFogColor = ARGB.srgbLerp(interpolationTime, previousBiomeFog, targetBiomeFog);
        if (targetBiomeFog != waterFogColor.get()) {
            targetBiomeFog = waterFogColor.get();
            previousBiomeFog = interpolatedWaterFogColor;
            biomeChangedTime = timeInMillis;
        }

        return Optional.of(interpolatedWaterFogColor);
    }
}
