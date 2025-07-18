package fuzs.naturalwaters.fabric.client.renderer.block;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import fuzs.naturalwaters.config.ClientConfig;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

/**
 * Copied from {@link net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl.WaterRenderHandler}
 * for replacing water textures.
 */
public final class FabricWaterBlockRenderer implements FluidRenderHandler {
    /**
     * See {@link net.minecraft.data.worldgen.biome.OverworldBiomes#NORMAL_WATER_COLOR}.
     */
    private static final int NORMAL_WATER_COLOR = 4159204;

    private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3], opaqueSprites = new TextureAtlasSprite[3];

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        if (NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
            return this.opaqueSprites;
        } else {
            return this.sprites;
        }
    }

    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        if (view != null && pos != null) {
            return ModBiomeColors.getAverageWaterColor(view, pos);
        } else {
            return NORMAL_WATER_COLOR;
        }
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        this.sprites[0] = OpaqueWaterPackResources.WATER_STILL_MATERIAL.sprite();
        this.opaqueSprites[0] = OpaqueWaterPackResources.OPAQUE_WATER_STILL_MATERIAL.sprite();
        this.sprites[1] = OpaqueWaterPackResources.WATER_FLOW_MATERIAL.sprite();
        this.opaqueSprites[1] = OpaqueWaterPackResources.OPAQUE_WATER_FLOW_MATERIAL.sprite();
        this.sprites[2] = this.opaqueSprites[2] = ModelBakery.WATER_OVERLAY.sprite();
    }
}
