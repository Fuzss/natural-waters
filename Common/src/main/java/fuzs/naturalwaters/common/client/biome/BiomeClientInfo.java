package fuzs.naturalwaters.common.client.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record BiomeClientInfo(Optional<Integer> waterSurfaceColor,
                              Optional<Integer> waterFogColor,
                              Optional<Float> waterFogDistance,
                              Optional<Float> waterSurfaceOpacity) {
    public static final Codec<BiomeClientInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf(
                    "water_surface_color").forGetter(BiomeClientInfo::waterSurfaceColor),
            ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("water_fog_color").forGetter(BiomeClientInfo::waterFogColor),
            ExtraCodecs.floatRange(0.0F, 1.0F)
                    .optionalFieldOf("water_fog_distance")
                    .forGetter(BiomeClientInfo::waterFogDistance),
            ExtraCodecs.floatRange(0.0F, 1.0F)
                    .optionalFieldOf("water_surface_opacity")
                    .forGetter(BiomeClientInfo::waterSurfaceOpacity)).apply(instance, BiomeClientInfo::new));
    public static final StreamCodec<ByteBuf, BiomeClientInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT.apply(
                    ByteBufCodecs::optional),
            BiomeClientInfo::waterSurfaceColor,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterFogColor,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterFogDistance,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterSurfaceOpacity,
            BiomeClientInfo::new);

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor) {
        this(waterSurfaceColor, waterFogColor, 1.0F);
    }

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor, float waterFogDistance) {
        this(waterSurfaceColor, waterFogColor, waterFogDistance, 0.75F);
    }

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor, float waterFogDistance, float waterSurfaceOpacity) {
        this(Optional.of(waterSurfaceColor),
                Optional.of(waterFogColor),
                Optional.of(waterFogDistance),
                Optional.of(waterSurfaceOpacity));
    }

    public Optional<Integer> getWaterFogColor() {
        return this.waterFogColor.or(this::waterSurfaceColor);
    }

    public int getWaterSurfaceOpacity() {
        // Bedrock Edition default value for biomes is 65%,
        // but 70% is the Java Edition water texture transparency (which we remove)
        // increase this a little as it is slightly reduced later in ModBiomeColors::getAverageWaterTransparency
        return ARGB.as8BitChannel(this.waterSurfaceOpacity.orElse(0.75F));
    }
}
