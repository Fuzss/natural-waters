package fuzs.naturalwaters.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
abstract class LiquidBlockRendererMixin {
    @Shadow
    @Final
    private TextureAtlasSprite[] waterIcons;
    @Unique
    private ThreadLocal<@Nullable Float> naturalwaters$vertexAlpha = new ThreadLocal<>();

    @Inject(method = "setupSprites", at = @At("TAIL"))
    protected void setupSprites(CallbackInfo callback) {
        this.waterIcons[0] = OpaqueWaterPackResources.WATER_STILL_MATERIAL.sprite();
        this.waterIcons[1] = OpaqueWaterPackResources.WATER_FLOW_MATERIAL.sprite();
    }

    @Inject(method = "tesselate", at = @At("HEAD"))
    public void tesselate$0(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, BlockState blockState, FluidState fluidState, CallbackInfo callback) {
        if (!fluidState.is(FluidTags.LAVA)) {
            this.naturalwaters$vertexAlpha.set(NaturalWatersClient.getAverageWaterTransparency(level, pos));
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
