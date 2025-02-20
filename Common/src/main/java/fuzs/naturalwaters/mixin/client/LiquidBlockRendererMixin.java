package fuzs.naturalwaters.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import fuzs.naturalwaters.config.ClientConfig;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
abstract class LiquidBlockRendererMixin {
    @Unique
    private ThreadLocal<@Nullable Float> naturalwaters$vertexAlpha = new ThreadLocal<>();

    @Inject(method = "tesselate", at = @At("HEAD"))
    public void tesselate$0(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, BlockState blockState, FluidState fluidState, CallbackInfo callback) {
        if (fluidState.is(FluidTags.WATER) &&
                NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
            this.naturalwaters$vertexAlpha.set(ModBiomeColors.getAverageWaterTransparency(level, pos));
        }
    }

    @Inject(method = "tesselate", at = @At("RETURN"))
    public void tesselate$1(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, BlockState blockState, FluidState fluidState, CallbackInfo callback) {
        this.naturalwaters$vertexAlpha.remove();
    }

    @ModifyArg(
            method = "vertex", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
    ), index = 3
    )
    private float vertex(float alpha) {
        return this.naturalwaters$vertexAlpha.get() != null ? this.naturalwaters$vertexAlpha.get() : alpha;
    }
}
