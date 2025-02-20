package fuzs.naturalwaters.fabric.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.fabric.client.renderer.block.FabricWaterBlockRenderer;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.world.level.material.Fluids;

public class NaturalWatersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        FluidRenderHandlerRegistry.INSTANCE.register(Fluids.WATER,
                Fluids.FLOWING_WATER,
                new FabricWaterBlockRenderer());
    }
}
