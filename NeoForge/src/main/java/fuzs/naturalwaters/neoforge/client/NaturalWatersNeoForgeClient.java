package fuzs.naturalwaters.neoforge.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.data.client.ModAtlasProvider;
import fuzs.naturalwaters.neoforge.client.renderer.block.NeoForgeWaterBlockRenderer;
import fuzs.naturalwaters.neoforge.data.client.ModBiomeClientInfoProvider;
import fuzs.naturalwaters.neoforge.mixin.client.accessor.ClientExtensionsManagerNeoForgeAccessor;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

@Mod(value = NaturalWaters.MOD_ID, dist = Dist.CLIENT)
public class NaturalWatersNeoForgeClient {

    public NaturalWatersNeoForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        registerLoadingHandlers(modContainer.getEventBus());
        DataProviderHelper.registerDataProviders(NaturalWaters.MOD_ID,
                ModAtlasProvider::new,
                ModBiomeClientInfoProvider::new);
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final FMLClientSetupEvent evt) -> {
            evt.enqueueWork(() -> {
                // we cannot use the proper NeoForge event for registering our extension,
                // as it does not allow for replacing an existing extension, which NeoForge itself already provides
                ClientExtensionsManagerNeoForgeAccessor.naturalwaters$getFluidTypeExtensions()
                        .put(NeoForgeMod.WATER_TYPE.value(), new NeoForgeWaterBlockRenderer());
            });
        });
    }
}
