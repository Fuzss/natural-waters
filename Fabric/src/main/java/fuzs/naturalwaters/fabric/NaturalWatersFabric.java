package fuzs.naturalwaters.fabric;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class NaturalWatersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(NaturalWaters.MOD_ID, NaturalWaters::new);
    }
}
