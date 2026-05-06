package fuzs.naturalwaters.fabric.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.world.level.material.Fluids;

public class NaturalWatersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        FluidRenderingRegistry.register(Fluids.WATER, Fluids.FLOWING_WATER, OpaqueWaterPackResources.getFluidModel());
    }
}
