package fuzs.naturalwaters.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import fuzs.naturalwaters.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;
import fuzs.puzzleslib.api.client.event.v1.level.ClientLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.FogEvents;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NaturalWatersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        AddResourcePackReloadListenersCallback.EVENT.register(ClientBiomeManager::onAddResourcePackReloadListeners);
        TagsUpdatedCallback.EVENT.register(ClientBiomeManager::onTagsUpdated);
        ClientLevelEvents.LOAD.register(ModBiomeColors::onLevelLoad);
        FogEvents.SETUP.register(NaturalWatersClient::onSetupFog);
    }

    private static void onSetupFog(Camera camera, float partialTick, @Nullable FogEnvironment fogEnvironment, FogType fogType, FogData fogData) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogDistance) return;
        if (fogType == FogType.WATER && camera.getEntity() instanceof LocalPlayer localPlayer) {
            Holder<Biome> holder = localPlayer.level().getBiome(localPlayer.blockPosition());
            Optional<Float> optional = ClientBiomeManager.getBiomeClientInfo(holder).waterFogDistance();
            if (optional.isPresent()) {
                GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
                fogData.environmentalEnd = Math.min(
                        96.0F * Math.max(0.25F, localPlayer.getWaterVision()) * optional.get(),
                        gameRenderer.getRenderDistance());
                fogData.skyEnd = fogData.cloudEnd = fogData.environmentalEnd;
            }
        }
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(NaturalWaters.id("opaque_water"),
                OpaqueWaterPackResources::new,
                true));
    }
}
