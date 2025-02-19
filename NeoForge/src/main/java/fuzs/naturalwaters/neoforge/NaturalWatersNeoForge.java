package fuzs.naturalwaters.neoforge;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.neoforge.data.ModDataMapProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(NaturalWaters.MOD_ID)
public class NaturalWatersNeoForge {

    public NaturalWatersNeoForge() {
        ModConstructor.construct(NaturalWaters.MOD_ID, NaturalWaters::new);
        DataProviderHelper.registerDataProviders(NaturalWaters.MOD_ID, ModDataMapProvider::new);
    }
}
