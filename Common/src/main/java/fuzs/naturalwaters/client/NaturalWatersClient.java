package fuzs.naturalwaters.client;

import com.mojang.blaze3d.shaders.FogShape;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;
import fuzs.puzzleslib.api.client.event.v1.level.ClientLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.FogEvents;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;

public class NaturalWatersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        AddResourcePackReloadListenersCallback.EVENT.register(ClientBiomeManager::onAddResourcePackReloadListeners);
        TagsUpdatedCallback.EVENT.register(ClientBiomeManager::onTagsUpdated);
        ClientLevelEvents.LOAD.register(ModBiomeColors::onLevelLoad);
        FogEvents.RENDER.register((GameRenderer gameRenderer, Camera camera, float partialTick, FogRenderer.FogMode fogMode, FogType fogType, MutableFloat fogStart, MutableFloat fogEnd, MutableValue<FogShape> fogShape) -> {
            if (fogType == FogType.WATER && camera.getEntity() instanceof LocalPlayer localPlayer) {
                Holder<Biome> holder = localPlayer.level().getBiome(localPlayer.blockPosition());
                float waterFogDistanceScale = ClientBiomeManager.getBiomeClientInfo(holder).getWaterFogDistanceScale();
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
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(NaturalWaters.id("opaque_water"),
                OpaqueWaterPackResources::new,
                false));
    }
}
