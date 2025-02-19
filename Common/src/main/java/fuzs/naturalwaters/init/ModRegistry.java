package fuzs.naturalwaters.init;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.world.level.biome.WaterColor;
import fuzs.neoforgedatapackextensions.api.v1.DataMapRegistry;
import fuzs.neoforgedatapackextensions.api.v1.DataMapToken;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

public class ModRegistry {
    public static final DataMapToken<Biome, WaterColor> WATER_COLOR_DATA_MAP = DataMapRegistry.INSTANCE.register(
            NaturalWaters.id("water_colors"),
            Registries.BIOME,
            WaterColor.CODEC,
            WaterColor.CODEC,
            false);

    public static void bootstrap() {
        // NO-OP
    }
}
