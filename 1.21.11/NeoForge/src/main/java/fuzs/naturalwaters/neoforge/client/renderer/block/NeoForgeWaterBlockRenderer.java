package fuzs.naturalwaters.neoforge.client.renderer.block;

import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
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
    private static final Identifier UNDERWATER_LOCATION = Identifier.withDefaultNamespace(
            "textures/misc/underwater.png");

    @Override
    public Identifier getStillTexture() {
        return OpaqueWaterPackResources.getWaterStillMaterial().texture();
    }

    @Override
    public Identifier getFlowingTexture() {
        return OpaqueWaterPackResources.getWaterFlowMaterial().texture();
    }

    @Override
    public Identifier getOverlayTexture() {
        return ModelBakery.WATER_OVERLAY.texture();
    }

    @Override
    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
        return UNDERWATER_LOCATION;
    }

    @Override
    public int getTintColor() {
        return NORMAL_WATER_COLOR;
    }

    @Override
    public int getTintColor(FluidState fluidState, BlockAndTintGetter level, BlockPos blockPos) {
        return ARGB.opaque(ModBiomeColors.getAverageWaterColor(level, blockPos));
    }
}
