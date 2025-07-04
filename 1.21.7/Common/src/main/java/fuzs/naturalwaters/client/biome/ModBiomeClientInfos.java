package fuzs.naturalwaters.client.biome;

import com.google.common.collect.ImmutableMap;
import fuzs.naturalwaters.init.ConventionalBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class ModBiomeClientInfos {
    static final Map<Predicate<Holder<Biome>>, BiomeClientInfo> DYNAMIC_BIOME_CLIENT_INFOS;

    static {
        ImmutableMap.Builder<Predicate<Holder<Biome>>, BiomeClientInfo> builder = ImmutableMap.builder();

        // Other Dimensions
        register(builder, BuiltInBiomeClientInfos.HELL, ConventionalBiomeTags.IS_NETHER);
        register(builder, BuiltInBiomeClientInfos.THE_END, ConventionalBiomeTags.IS_END);

        // Mountains
        register(builder, BuiltInBiomeClientInfos.EXTREME_HILLS, ConventionalBiomeTags.IS_MOUNTAIN);
        register(builder, BuiltInBiomeClientInfos.EXTREME_HILLS_MUTATED, ConventionalBiomeTags.IS_MOUNTAIN_PEAK);
        register(builder, BuiltInBiomeClientInfos.EXTREME_HILLS_EDGE, ConventionalBiomeTags.IS_MOUNTAIN_SLOPE);

        // Plains
        register(builder,
                BuiltInBiomeClientInfos.ICE_PLAINS,
                ConventionalBiomeTags.IS_PLAINS,
                ConventionalBiomeTags.IS_COLD,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder, BuiltInBiomeClientInfos.ICE_PLAINS, ConventionalBiomeTags.IS_SNOWY_PLAINS);
        register(builder, BuiltInBiomeClientInfos.PLAINS, ConventionalBiomeTags.IS_PLAINS);

        // Forests
        register(builder,
                BuiltInBiomeClientInfos.FOREST_HILLS,
                ConventionalBiomeTags.IS_FOREST,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.FLOWER_FOREST, ConventionalBiomeTags.IS_FLOWER_FOREST);
        register(builder, BuiltInBiomeClientInfos.FOREST, ConventionalBiomeTags.IS_FOREST);
        register(builder,
                BuiltInBiomeClientInfos.BIRCH_FOREST_HILLS,
                ConventionalBiomeTags.IS_BIRCH_FOREST,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.BIRCH_FOREST, ConventionalBiomeTags.IS_BIRCH_FOREST);
        register(builder, BuiltInBiomeClientInfos.ROOFED_FOREST, ConventionalBiomeTags.IS_DARK_FOREST);

        // Taigas
        register(builder,
                BuiltInBiomeClientInfos.COLD_TAIGA,
                ConventionalBiomeTags.IS_TAIGA,
                ConventionalBiomeTags.IS_COLD,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder,
                BuiltInBiomeClientInfos.MEGA_TAIGA,
                ConventionalBiomeTags.IS_TAIGA,
                ConventionalBiomeTags.IS_OLD_GROWTH);
        register(builder,
                BuiltInBiomeClientInfos.TAIGA_HILLS,
                ConventionalBiomeTags.IS_TAIGA,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.TAIGA, ConventionalBiomeTags.IS_TAIGA);

        // Other Biomes
        register(builder,
                BuiltInBiomeClientInfos.JUNGLE_HILLS,
                ConventionalBiomeTags.IS_JUNGLE,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.JUNGLE, ConventionalBiomeTags.IS_JUNGLE);
        register(builder,
                BuiltInBiomeClientInfos.SAVANNA_PLATEAU,
                ConventionalBiomeTags.IS_SAVANNA,
                ConventionalBiomeTags.IS_PLATEAU);
        register(builder,
                BuiltInBiomeClientInfos.SAVANNA_PLATEAU,
                ConventionalBiomeTags.IS_SAVANNA,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.SAVANNA, ConventionalBiomeTags.IS_SAVANNA);
        register(builder,
                BuiltInBiomeClientInfos.SWAMPLAND_MUTATED,
                ConventionalBiomeTags.IS_SWAMP,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.SWAMPLAND, ConventionalBiomeTags.IS_SWAMP);
        register(builder,
                BuiltInBiomeClientInfos.DESERT_HILLS,
                ConventionalBiomeTags.IS_DESERT,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.DESERT, ConventionalBiomeTags.IS_DESERT);
        register(builder,
                BuiltInBiomeClientInfos.MESA_PLATEAU,
                ConventionalBiomeTags.IS_BADLANDS,
                ConventionalBiomeTags.IS_PLATEAU);
        register(builder,
                BuiltInBiomeClientInfos.MESA_PLATEAU,
                ConventionalBiomeTags.IS_BADLANDS,
                ConventionalBiomeTags.IS_HILL);
        register(builder, BuiltInBiomeClientInfos.MESA, ConventionalBiomeTags.IS_BADLANDS);

        // Beaches & Shores
        register(builder,
                BuiltInBiomeClientInfos.COLD_BEACH,
                ConventionalBiomeTags.IS_BEACH,
                ConventionalBiomeTags.IS_COLD,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder, BuiltInBiomeClientInfos.BEACH, ConventionalBiomeTags.IS_BEACH);
        register(builder, BuiltInBiomeClientInfos.STONE_BEACH, ConventionalBiomeTags.IS_STONY_SHORES);
        register(builder, BuiltInBiomeClientInfos.MUSHROOM_ISLAND, ConventionalBiomeTags.IS_MUSHROOM);

        // Rivers
        register(builder,
                BuiltInBiomeClientInfos.FROZEN_RIVER,
                ConventionalBiomeTags.IS_RIVER,
                ConventionalBiomeTags.IS_COLD,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder, BuiltInBiomeClientInfos.RIVER, ConventionalBiomeTags.IS_RIVER);

        // Oceans
        register(builder,
                BuiltInBiomeClientInfos.FROZEN_OCEAN,
                ConventionalBiomeTags.IS_SHALLOW_OCEAN,
                ConventionalBiomeTags.IS_AQUATIC_ICY,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder,
                BuiltInBiomeClientInfos.COLD_OCEAN,
                ConventionalBiomeTags.IS_SHALLOW_OCEAN,
                ConventionalBiomeTags.IS_COLD);
        register(builder, BuiltInBiomeClientInfos.OCEAN, ConventionalBiomeTags.IS_SHALLOW_OCEAN);
        register(builder,
                BuiltInBiomeClientInfos.DEEP_FROZEN_OCEAN,
                ConventionalBiomeTags.IS_DEEP_OCEAN,
                ConventionalBiomeTags.IS_AQUATIC_ICY,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder,
                BuiltInBiomeClientInfos.DEEP_COLD_OCEAN,
                ConventionalBiomeTags.IS_DEEP_OCEAN,
                ConventionalBiomeTags.IS_COLD);
        register(builder, BuiltInBiomeClientInfos.DEEP_OCEAN, ConventionalBiomeTags.IS_DEEP_OCEAN);
        register(builder,
                BuiltInBiomeClientInfos.FROZEN_OCEAN,
                ConventionalBiomeTags.IS_OCEAN,
                ConventionalBiomeTags.IS_AQUATIC_ICY,
                ConventionalBiomeTags.IS_ICY,
                ConventionalBiomeTags.IS_SNOWY);
        register(builder,
                BuiltInBiomeClientInfos.COLD_OCEAN,
                ConventionalBiomeTags.IS_OCEAN,
                ConventionalBiomeTags.IS_COLD);
        register(builder, BuiltInBiomeClientInfos.OCEAN, ConventionalBiomeTags.IS_OCEAN);

        // Biome Themes
        register(builder, BuiltInBiomeClientInfos.DEFAULT, ConventionalBiomeTags.IS_UNDERGROUND);
        register(builder, BuiltInBiomeClientInfos.DEFAULT, ConventionalBiomeTags.IS_CAVE);
        register(builder, BuiltInBiomeClientInfos.JUNGLE, ConventionalBiomeTags.IS_LUSH);
        register(builder, BuiltInBiomeClientInfos.MUSHROOM_ISLAND, ConventionalBiomeTags.IS_MAGICAL);
        register(builder, BuiltInBiomeClientInfos.PALE_GARDEN, ConventionalBiomeTags.IS_SPOOKY);
        register(builder, BuiltInBiomeClientInfos.FLOWER_FOREST, ConventionalBiomeTags.IS_FLORAL);
        register(builder, BuiltInBiomeClientInfos.DESERT, ConventionalBiomeTags.IS_SANDY);

        // Biome Climates
        register(builder, BuiltInBiomeClientInfos.DESERT, ConventionalBiomeTags.IS_HOT);
        register(builder, BuiltInBiomeClientInfos.FOREST, ConventionalBiomeTags.IS_TEMPERATE);
        register(builder, BuiltInBiomeClientInfos.TAIGA, ConventionalBiomeTags.IS_COLD);
        register(builder, BuiltInBiomeClientInfos.SWAMPLAND, ConventionalBiomeTags.IS_WET);
        register(builder, BuiltInBiomeClientInfos.DESERT, ConventionalBiomeTags.IS_DRY);
        register(builder, BuiltInBiomeClientInfos.ICE_PLAINS, ConventionalBiomeTags.IS_SNOWY);
        register(builder, BuiltInBiomeClientInfos.ICE_PLAINS_SPIKES, ConventionalBiomeTags.IS_ICY);
        register(builder, BuiltInBiomeClientInfos.OCEAN, ConventionalBiomeTags.IS_AQUATIC);

        DYNAMIC_BIOME_CLIENT_INFOS = builder.build();
    }

    private ModBiomeClientInfos() {
        // NO-OP
    }

    @SafeVarargs
    static void register(ImmutableMap.Builder<Predicate<Holder<Biome>>, BiomeClientInfo> builder, BiomeClientInfo biomeClientInfo, TagKey<Biome> primaryTagKey, TagKey<Biome>... secondaryTagKeys) {
        builder.put((Holder<Biome> holder) -> {
            if (holder.is(primaryTagKey)) {
                if (secondaryTagKeys.length != 0) {
                    for (TagKey<Biome> tagKey : secondaryTagKeys) {
                        if (holder.is(tagKey)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }, biomeClientInfo);
    }

    public static Optional<BiomeClientInfo> pick(Holder<Biome> holder) {
        for (Map.Entry<Predicate<Holder<Biome>>, BiomeClientInfo> entry : DYNAMIC_BIOME_CLIENT_INFOS.entrySet()) {
            if (entry.getKey().test(holder)) {
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }
}
