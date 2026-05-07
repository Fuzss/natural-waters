package fuzs.naturalwaters.neoforge.client.color.block;

import fuzs.naturalwaters.common.client.color.block.WaterTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.client.fluid.FluidTintSources;

/**
 * @see FluidTintSources#water()
 */
public class NeoForgeWaterTintSource extends WaterTintSource implements FluidTintSource {
    @Override
    public int color(FluidState state) {
        return 0xFF3F76E4;
    }

    @Override
    public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
        return this.colorInWorld(level, pos);
    }
}
