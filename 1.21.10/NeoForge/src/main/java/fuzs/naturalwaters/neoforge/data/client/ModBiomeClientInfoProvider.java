package fuzs.naturalwaters.neoforge.data.client;

import fuzs.naturalwaters.client.biome.BiomeClientInfo;
import fuzs.naturalwaters.client.biome.BuiltInBiomeClientInfos;
import fuzs.naturalwaters.client.biome.ClientBiomeManager;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class ModBiomeClientInfoProvider extends JsonCodecProvider<BiomeClientInfo> {

    public ModBiomeClientInfoProvider(DataProviderContext context) {
        this(context.getModId(), context.getPackOutput(), context.getRegistries());
    }

    public ModBiomeClientInfoProvider(String modId, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput,
                PackOutput.Target.RESOURCE_PACK,
                ClientBiomeManager.ASSET_DIRECTORY,
                BiomeClientInfo.CODEC,
                lookupProvider,
                modId);
    }

    @Override
    protected void gather() {
        this.biome(Biomes.PLAINS, BuiltInBiomeClientInfos.PLAINS);
        this.biome(Biomes.SUNFLOWER_PLAINS, BuiltInBiomeClientInfos.SUNFLOWER_PLAINS);
        this.biome(Biomes.SNOWY_PLAINS, BuiltInBiomeClientInfos.ICE_PLAINS);
        this.biome(Biomes.ICE_SPIKES, BuiltInBiomeClientInfos.ICE_PLAINS_SPIKES);
        this.biome(Biomes.DESERT, BuiltInBiomeClientInfos.DESERT);
        this.biome(Biomes.SWAMP, BuiltInBiomeClientInfos.SWAMPLAND);
        this.biome(Biomes.MANGROVE_SWAMP, BuiltInBiomeClientInfos.MANGROVE_SWAMP);
        this.biome(Biomes.FOREST, BuiltInBiomeClientInfos.FOREST);
        this.biome(Biomes.FLOWER_FOREST, BuiltInBiomeClientInfos.FLOWER_FOREST);
        this.biome(Biomes.BIRCH_FOREST, BuiltInBiomeClientInfos.BIRCH_FOREST);
        this.biome(Biomes.DARK_FOREST, BuiltInBiomeClientInfos.ROOFED_FOREST);
        this.biome(Biomes.PALE_GARDEN, BuiltInBiomeClientInfos.PALE_GARDEN);
        this.biome(Biomes.OLD_GROWTH_BIRCH_FOREST, BuiltInBiomeClientInfos.BIRCH_FOREST_HILLS);
        this.biome(Biomes.OLD_GROWTH_PINE_TAIGA, BuiltInBiomeClientInfos.MEGA_TAIGA);
        this.biome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, BuiltInBiomeClientInfos.MEGA_SPRUCE_TAIGA);
        this.biome(Biomes.TAIGA, BuiltInBiomeClientInfos.TAIGA);
        this.biome(Biomes.SNOWY_TAIGA, BuiltInBiomeClientInfos.COLD_TAIGA);
        this.biome(Biomes.SAVANNA, BuiltInBiomeClientInfos.SAVANNA);
        this.biome(Biomes.SAVANNA_PLATEAU, BuiltInBiomeClientInfos.SAVANNA_PLATEAU);
        this.biome(Biomes.WINDSWEPT_HILLS, BuiltInBiomeClientInfos.EXTREME_HILLS);
        this.biome(Biomes.WINDSWEPT_GRAVELLY_HILLS, BuiltInBiomeClientInfos.EXTREME_HILLS_MUTATED);
        this.biome(Biomes.WINDSWEPT_FOREST, BuiltInBiomeClientInfos.EXTREME_HILLS_PLUS_TREES);
        this.biome(Biomes.WINDSWEPT_SAVANNA, BuiltInBiomeClientInfos.SAVANNA_MUTATED);
        this.biome(Biomes.JUNGLE, BuiltInBiomeClientInfos.JUNGLE);
        this.biome(Biomes.SPARSE_JUNGLE, BuiltInBiomeClientInfos.JUNGLE_EDGE);
        this.biome(Biomes.BAMBOO_JUNGLE, BuiltInBiomeClientInfos.BAMBOO_JUNGLE);
        this.biome(Biomes.BADLANDS, BuiltInBiomeClientInfos.MESA);
        this.biome(Biomes.ERODED_BADLANDS, BuiltInBiomeClientInfos.MESA_BRYCE);
        this.biome(Biomes.WOODED_BADLANDS, BuiltInBiomeClientInfos.MESA_PLATEAU);
        this.biome(Biomes.MEADOW, BuiltInBiomeClientInfos.MEADOW);
        this.biome(Biomes.CHERRY_GROVE, BuiltInBiomeClientInfos.CHERRY_GROVE);
        this.biome(Biomes.GROVE, BuiltInBiomeClientInfos.GROVE);
        this.biome(Biomes.SNOWY_SLOPES, BuiltInBiomeClientInfos.SNOWY_SLOPES);
        this.biome(Biomes.FROZEN_PEAKS, BuiltInBiomeClientInfos.FROZEN_PEAKS);
        this.biome(Biomes.JAGGED_PEAKS, BuiltInBiomeClientInfos.JAGGED_PEAKS);
        this.biome(Biomes.STONY_PEAKS, BuiltInBiomeClientInfos.STONY_PEAKS);
        this.biome(Biomes.RIVER, BuiltInBiomeClientInfos.RIVER);
        this.biome(Biomes.FROZEN_RIVER, BuiltInBiomeClientInfos.FROZEN_RIVER);
        this.biome(Biomes.BEACH, BuiltInBiomeClientInfos.BEACH);
        this.biome(Biomes.SNOWY_BEACH, BuiltInBiomeClientInfos.COLD_BEACH);
        this.biome(Biomes.STONY_SHORE, BuiltInBiomeClientInfos.STONE_BEACH);
        this.biome(Biomes.WARM_OCEAN, BuiltInBiomeClientInfos.WARM_OCEAN);
        this.biome(Biomes.LUKEWARM_OCEAN, BuiltInBiomeClientInfos.LUKEWARM_OCEAN);
        this.biome(Biomes.DEEP_LUKEWARM_OCEAN, BuiltInBiomeClientInfos.DEEP_LUKEWARM_OCEAN);
        this.biome(Biomes.OCEAN, BuiltInBiomeClientInfos.OCEAN);
        this.biome(Biomes.DEEP_OCEAN, BuiltInBiomeClientInfos.DEEP_OCEAN);
        this.biome(Biomes.COLD_OCEAN, BuiltInBiomeClientInfos.COLD_OCEAN);
        this.biome(Biomes.DEEP_COLD_OCEAN, BuiltInBiomeClientInfos.DEEP_COLD_OCEAN);
        this.biome(Biomes.FROZEN_OCEAN, BuiltInBiomeClientInfos.FROZEN_OCEAN);
        this.biome(Biomes.DEEP_FROZEN_OCEAN, BuiltInBiomeClientInfos.DEEP_FROZEN_OCEAN);
        this.biome(Biomes.MUSHROOM_FIELDS, BuiltInBiomeClientInfos.MUSHROOM_ISLAND);
        this.biome(Biomes.DRIPSTONE_CAVES, BuiltInBiomeClientInfos.DRIPSTONE_CAVES);
        this.biome(Biomes.LUSH_CAVES, BuiltInBiomeClientInfos.LUSH_CAVES);
        this.biome(Biomes.DEEP_DARK, BuiltInBiomeClientInfos.DEEP_DARK);
        this.biome(Biomes.NETHER_WASTES, BuiltInBiomeClientInfos.HELL);
        this.biome(Biomes.WARPED_FOREST, BuiltInBiomeClientInfos.WARPED_FOREST);
        this.biome(Biomes.CRIMSON_FOREST, BuiltInBiomeClientInfos.CRIMSON_FOREST);
        this.biome(Biomes.SOUL_SAND_VALLEY, BuiltInBiomeClientInfos.SOULSAND_VALLEY);
        this.biome(Biomes.BASALT_DELTAS, BuiltInBiomeClientInfos.BASALT_DELTAS);
        this.biome(Biomes.THE_END, BuiltInBiomeClientInfos.THE_END);
        this.biome(Biomes.END_HIGHLANDS, BuiltInBiomeClientInfos.THE_END);
        this.biome(Biomes.END_MIDLANDS, BuiltInBiomeClientInfos.THE_END);
        this.biome(Biomes.SMALL_END_ISLANDS, BuiltInBiomeClientInfos.THE_END);
        this.biome(Biomes.END_BARRENS, BuiltInBiomeClientInfos.THE_END);
    }

    public final void biome(ResourceKey<Biome> resourceKey, BiomeClientInfo biomeClientInfo) {
        this.unconditional(resourceKey.location(), biomeClientInfo);
    }
}
