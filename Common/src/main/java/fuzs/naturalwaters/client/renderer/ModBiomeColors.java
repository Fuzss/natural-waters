package fuzs.naturalwaters.client.renderer;

import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeColors {
    public static final ColorResolver WATER_COLOR_RESOLVER = (Biome biome, double x, double y) -> {
        return ClientBiomeManager.getBiomeClientInfo(biome).waterSurfaceColor().orElseGet(biome::getWaterColor);
    };
    public static final ColorResolver WATER_TRANSPARENCY_RESOLVER = (Biome biome, double x, double z) -> {
        return ClientBiomeManager.getBiomeClientInfo(biome).getWaterSurfaceTransparency();
    };

    public static void onLevelLoad(Minecraft minecraft, ClientLevel clientLevel) {
        clientLevel.tintCaches.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache((BlockPos blockPos) -> {
            return clientLevel.calculateBlockTint(blockPos, WATER_COLOR_RESOLVER);
        }));
        clientLevel.tintCaches.put(WATER_TRANSPARENCY_RESOLVER,
                new BlockTintCache((BlockPos blockPos) -> clientLevel.calculateBlockTint(blockPos,
                        WATER_TRANSPARENCY_RESOLVER)));
    }

    public static float getAverageWaterTransparency(BlockAndTintGetter level, BlockPos blockPos) {
        // don't allow textures to become fully opaque,
        // Bedrock Edition does this also internally despite values going up to 100%
        return level.getBlockTint(blockPos, WATER_TRANSPARENCY_RESOLVER) / 255.0F * 0.95F;
    }
}
