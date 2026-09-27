package fuzs.naturalwaters.common.client;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.attribute.BiomeEnvironmentAttributes;
import fuzs.naturalwaters.common.client.biome.ClientBiomeManager;
import fuzs.naturalwaters.common.client.color.block.WaterTintSource;
import fuzs.naturalwaters.common.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.core.v1.context.BlockColorsContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.ResourcePackReloadListenersContext;
import fuzs.puzzleslib.common.api.client.event.v1.ClientTagsUpdatedCallback;
import fuzs.puzzleslib.common.api.client.event.v1.level.ClientLevelEvents;
import fuzs.puzzleslib.common.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.common.api.resources.v2.PackResourcesBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Blocks;

public class NaturalWatersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTagsUpdatedCallback.EVENT.register(ClientBiomeManager::onClientTagsUpdated);
        ClientLevelEvents.UNLOAD.register(NaturalWatersClient::onLevelUnload);
    }

    private static void onLevelUnload(Minecraft minecraft, ClientLevel clientLevel) {
        BiomeEnvironmentAttributes.clear();
    }

    @Override
    public void onClientSetup() {
        NaturalWaters.CONFIG.getHolder(ClientConfig.class).addCallback((Runnable) BiomeEnvironmentAttributes::rebuild);
    }

    @Override
    public void onRegisterBlockColorProviders(BlockColorsContext context) {
        if (NaturalWaters.CONFIG.get(ClientConfig.class).requiresCustomWaterTintSource()) {
            context.registerBlockColor(Blocks.WATER_CAULDRON, new WaterTintSource());
            context.registerBlockColor(Blocks.WATER, new WaterTintSource());
            context.registerBlockColor(Blocks.BUBBLE_COLUMN, new WaterTintSource());
        }
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.registerRepositorySource(PackResourcesBuilder.client(NaturalWaters.id("opaque_water"),
                OpaqueWaterPackResources::new).hidden(true).buildRepositorySource());
    }

    @Override
    public void onAddResourcePackReloadListeners(ResourcePackReloadListenersContext context) {
        ClientBiomeManager.onAddResourcePackReloadListeners(context::registerReloadListener);
    }
}
