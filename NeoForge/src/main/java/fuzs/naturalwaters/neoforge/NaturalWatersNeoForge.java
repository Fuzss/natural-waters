package fuzs.naturalwaters.neoforge;

import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(NaturalWaters.MOD_ID)
public class NaturalWatersNeoForge {

    public NaturalWatersNeoForge() {
        ModConstructor.construct(NaturalWaters.MOD_ID, NaturalWaters::new);
    }
}
