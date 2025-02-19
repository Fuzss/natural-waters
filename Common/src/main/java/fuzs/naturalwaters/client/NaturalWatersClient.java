package fuzs.naturalwaters.client;

import com.mojang.blaze3d.shaders.FogShape;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.world.level.biome.WaterColor;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.level.ClientLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.FogEvents;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.material.FogType;

public class NaturalWatersClient implements ClientModConstructor {
    static final ColorResolver WATER_TRANSPARENCY_RESOLVER = (Biome biome, double x, double z) -> {
        Registry<Biome> biomes = Minecraft.getInstance()
                .getConnection()
                .registryAccess()
                .lookupOrThrow(Registries.BIOME);
        biomes = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME);
        Holder<Biome> holder = biomes
                .wrapAsHolder(biome);
        WaterColor waterColor = WaterColor.getWaterColor(holder);
        if (holder.is(Biomes.SWAMP)) {
            return 255;
        } else {
            return waterColor.getWaterSurfaceTransparency();
        }
    };

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        FogEvents.RENDER.register((GameRenderer gameRenderer, Camera camera, float partialTick, FogRenderer.FogMode fogMode, FogType fogType, MutableFloat fogStart, MutableFloat fogEnd, MutableValue<FogShape> fogShape) -> {
            if (fogType == FogType.WATER && camera.getEntity() instanceof LocalPlayer localPlayer) {
                Holder<Biome> holder = localPlayer.level().getBiome(localPlayer.blockPosition());
                WaterColor waterColor = WaterColor.getWaterColor(holder);
                float waterFogDistanceScale = waterColor.getWaterFogDistanceScale();
                if (waterFogDistanceScale != 1.0F) {
                    fogEnd.accept(96.0F * Math.max(0.25F, localPlayer.getWaterVision()) * waterFogDistanceScale);
                    if (fogEnd.getAsFloat() > gameRenderer.getRenderDistance()) {
                        fogEnd.accept(gameRenderer.getRenderDistance());
                    } else {
                        fogShape.accept(FogShape.SPHERE);
                    }
                }
            }
        });
        ClientLevelEvents.LOAD.register((Minecraft minecraft, ClientLevel level) -> {
            level.tintCaches.put(WATER_TRANSPARENCY_RESOLVER,
                    new BlockTintCache((BlockPos pos) -> level.calculateBlockTint(pos, WATER_TRANSPARENCY_RESOLVER)));
        });
    }

    public static float getAverageWaterTransparency(BlockAndTintGetter level, BlockPos blockPos) {
        return level.getBlockTint(blockPos, WATER_TRANSPARENCY_RESOLVER) / 255.0F;
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(NaturalWaters.id("opaque_water"),
                OpaqueWaterPackResources::new,
                false));
    }
}
