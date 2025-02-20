package fuzs.naturalwaters.neoforge.client.renderer.block;

import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

/**
 * Copied from {@link net.neoforged.neoforge.client.ClientNeoForgeMod} for replacing water textures.
 */
public final class NeoForgeWaterBlockRenderer implements IClientFluidTypeExtensions {
    /**
     * See {@link net.minecraft.data.worldgen.biome.OverworldBiomes#NORMAL_WATER_COLOR}.
     */
    private static final int NORMAL_WATER_COLOR = 4159204;
    /**
     * See {@link net.minecraft.client.renderer.ScreenEffectRenderer#UNDERWATER_LOCATION}.
     */
    private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.withDefaultNamespace(
            "textures/misc/underwater.png");

    @Override
    public ResourceLocation getStillTexture() {
        return OpaqueWaterPackResources.WATER_STILL_MATERIAL.texture();
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return OpaqueWaterPackResources.WATER_FLOW_MATERIAL.texture();
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return ModelBakery.WATER_OVERLAY.texture();
    }

    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft minecraft) {
        return UNDERWATER_LOCATION;
    }

    @Override
    public int getTintColor() {
        return NORMAL_WATER_COLOR;
    }

    @Override
    public int getTintColor(FluidState fluidState, BlockAndTintGetter level, BlockPos blockPos) {
        return ARGB.opaque(BiomeColors.getAverageWaterColor(level, blockPos));
    }
}
