package fuzs.naturalwaters.neoforge.data;

import fuzs.naturalwaters.init.ModRegistry;
import fuzs.naturalwaters.world.level.biome.WaterColor;
import fuzs.neoforgedatapackextensions.neoforge.api.v1.NeoForgeDataMapToken;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(DataProviderContext context) {
        this(context.getPackOutput(), context.getRegistries());
    }

    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider registries) {
        Builder<WaterColor, Biome> builder = this.builder(NeoForgeDataMapToken.unwrap(ModRegistry.WATER_COLOR_DATA_MAP));
        builder.add(Biomes.SUNFLOWER_PLAINS, new WaterColor(0X44AFF5, 0X44AFF5, 60), false);
        builder.add(Biomes.PLAINS, new WaterColor(0X44AFF5, 0X44AFF5, 60), false);
        builder.add(Biomes.DESERT, new WaterColor(0x32a598, 0x32a598, 60), false);
        builder.add(Biomes.WINDSWEPT_HILLS, new WaterColor(0x007BF7, 0x007BF7), false);
        builder.add(Biomes.FOREST, new WaterColor(0x1e97f2, 0x1e97f2, 60), false);
        builder.add(Biomes.FLOWER_FOREST, new WaterColor(0x20a3CC, 0x20a3CC, 60), false);
        builder.add(Biomes.TAIGA, new WaterColor(0x287082, 0x287082, 60), false);
        builder.add(Biomes.SWAMP, new WaterColor(0x4c6559, 0x4c6559, 30, 1.0F), false);
        builder.add(BiomeTags.IS_NETHER, new WaterColor(0x905957, 0x905957, 15), false);
    }
}
