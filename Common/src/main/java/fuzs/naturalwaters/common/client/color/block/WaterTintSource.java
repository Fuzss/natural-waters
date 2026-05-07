package fuzs.naturalwaters.common.client.color.block;

import fuzs.naturalwaters.common.client.renderer.CustomBiomeColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @see BlockTintSources#water()
 * @see BlockTintSources#waterParticles()
 */
public class WaterTintSource implements BlockTintSource {
    @Override
    public int color(BlockState state) {
        return -1;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.colorInWorld(level, pos);
    }

    public final int colorInWorld(BlockAndTintGetter level, BlockPos pos) {
        int color = CustomBiomeColors.getAverageWaterColor(level, pos);
        float alpha = CustomBiomeColors.getAverageWaterTransparency(level, pos);
        return ARGB.color(alpha, color);
    }
}
