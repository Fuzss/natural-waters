package fuzs.naturalwaters.neoforge.client;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.naturalwaters.common.client.NaturalWatersClient;
import fuzs.naturalwaters.common.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.common.client.renderer.CustomBiomeColors;
import fuzs.naturalwaters.common.config.ClientConfig;
import fuzs.naturalwaters.common.data.client.ModAtlasProvider;
import fuzs.naturalwaters.neoforge.client.color.block.NeoForgeWaterTintSource;
import fuzs.naturalwaters.neoforge.data.client.ModBiomeClientInfoProvider;
import fuzs.naturalwaters.neoforge.mixin.client.accessor.RegisterFluidModelsEventNeoForgeAccessor;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

import java.util.Map;

@Mod(value = NaturalWaters.MOD_ID, dist = Dist.CLIENT)
public class NaturalWatersNeoForgeClient {
    /**
     * @see net.minecraft.client.renderer.block.FluidStateModelSet#WATER_MODEL
     */
    private static final FluidModel.Unbaked WATER_MODEL = new FluidModel.Unbaked(new Material(OpaqueWaterPackResources.WATER_STILL),
            new Material(OpaqueWaterPackResources.WATER_FLOW),
            FluidStateModelSet.WATER_MODEL.overlayMaterial(),
            new NeoForgeWaterTintSource());

    public NaturalWatersNeoForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        registerLoadingHandlers(modContainer.getEventBus());
        DataProviderHelper.registerDataProviders(NaturalWaters.MOD_ID,
                ModAtlasProvider::new,
                ModBiomeClientInfoProvider::new);
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final RegisterColorHandlersEvent.ColorResolvers event) -> {
            event.register(CustomBiomeColors.WATER_COLOR_RESOLVER);
            event.register(CustomBiomeColors.WATER_TRANSPARENCY_RESOLVER);
        });
        eventBus.addListener((final RegisterFluidModelsEvent event) -> {
            if (NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
                registerFluidModel(event, WATER_MODEL, Fluids.WATER, Fluids.FLOWING_WATER);
            }
        });
    }

    /**
     * @see RegisterFluidModelsEvent#register(FluidModel.Unbaked, Fluid, Fluid)
     */
    private static void registerFluidModel(RegisterFluidModelsEvent event, FluidModel.Unbaked model, Fluid stillFluid, Fluid flowingFluid) {
        MaterialBaker materials = RegisterFluidModelsEventNeoForgeAccessor.class.cast(event)
                .naturalwaters$getMaterials();
        FluidModel baked = model.bake(materials, stillFluid::toString);
        registerFluidModel(event, stillFluid, baked);
        registerFluidModel(event, flowingFluid, baked);
    }

    /**
     * @see RegisterFluidModelsEvent#register(FluidModel.Unbaked, Fluid)
     */
    private static void registerFluidModel(RegisterFluidModelsEvent event, Fluid fluid, FluidModel model) {
        Map<Fluid, FluidModel> models = RegisterFluidModelsEventNeoForgeAccessor.class.cast(event)
                .naturalwaters$getModels();
        FluidModel oldModel = models.put(fluid, model);
        if (oldModel != null) {
            NaturalWaters.LOGGER.warn("Duplicate FluidModel registration for Fluid {} (old: {}, new: {})",
                    fluid,
                    oldModel,
                    model);
        }
    }
}
