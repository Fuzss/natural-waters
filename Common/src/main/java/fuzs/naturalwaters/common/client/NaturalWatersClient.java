package fuzs.naturalwaters.common.client;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.common.client.color.block.WaterTintSource;
import fuzs.naturalwaters.common.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.core.v1.context.BlockColorsContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.ResourcePackReloadListenersContext;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.FogEvents;
import fuzs.puzzleslib.common.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.common.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.common.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FogType;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class NaturalWatersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        TagsUpdatedCallback.EVENT.register(ClientBiomeManager::onTagsUpdated);
        FogEvents.SETUP.register(NaturalWatersClient::onSetupFog);
    }

    private static void onSetupFog(Camera camera, float partialTick, @Nullable FogEnvironment fogEnvironment, FogType fogType, FogData fogData) {
        if (!NaturalWaters.CONFIG.get(ClientConfig.class).waterFogDistance) {
            return;
        }

        if (fogType == FogType.WATER && camera.entity() instanceof LocalPlayer player) {
            Holder<Biome> holder = player.level().getBiome(player.blockPosition());
            Optional<Float> optional = ClientBiomeManager.getBiomeClientInfo(holder).waterFogDistance();
            if (optional.isPresent()) {
                fogData.environmentalEnd = 96.0F * Math.max(0.25F, player.getWaterVision()) * optional.get();
                fogData.skyEnd = fogData.cloudEnd = fogData.environmentalEnd;
            }
        }
    }

    @Override
    public void onRegisterBlockColorProviders(BlockColorsContext context) {
        context.registerBlockColor(Blocks.WATER_CAULDRON, new WaterTintSource());
        context.registerBlockColor(Blocks.WATER, new WaterTintSource());
        context.registerBlockColor(Blocks.BUBBLE_COLUMN, new WaterTintSource());
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.registerRepositorySource(PackResourcesHelper.buildClientPack(NaturalWaters.id("opaque_water"),
                OpaqueWaterPackResources::new,
                true));
    }

    @Override
    public void onAddResourcePackReloadListeners(ResourcePackReloadListenersContext context) {
        ClientBiomeManager.onAddResourcePackReloadListeners(context::registerReloadListener);
    }
}
