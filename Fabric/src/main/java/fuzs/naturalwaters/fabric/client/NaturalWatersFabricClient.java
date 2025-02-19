package fuzs.naturalwaters.fabric.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class NaturalWatersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        FluidRenderHandlerRegistry.INSTANCE.register(Fluids.WATER, Fluids.FLOWING_WATER, new FluidRenderHandler() {
            private static final int DEFAULT_WATER_COLOR = 0x3F76E4;

            private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];

            @Override
            public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return this.sprites;
            }

            @Override
            public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                if (view != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(view, pos);
                } else {
                    return DEFAULT_WATER_COLOR;
                }
            }

            @Override
            public void reloadTextures(TextureAtlas textureAtlas) {
                this.sprites[0] = OpaqueWaterPackResources.WATER_STILL_MATERIAL.sprite();
                this.sprites[1] = OpaqueWaterPackResources.WATER_FLOW_MATERIAL.sprite();
                this.sprites[2] = ModelBakery.WATER_OVERLAY.sprite();
            }
        });
    }
}
