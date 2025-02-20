package fuzs.naturalwaters.client.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record BiomeClientInfo(Optional<Integer> waterSurfaceColor,
                              Optional<Integer> waterFogColor,
                              Optional<Float> waterFogDistance,
                              Optional<Float> waterSurfaceTransparency) {
    public static final BiomeClientInfo EMPTY = new BiomeClientInfo(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
    public static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(TextColor.CODEC.xmap(TextColor::getValue,
            TextColor::fromRgb), ExtraCodecs.RGB_COLOR_CODEC);
    public static final Codec<BiomeClientInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(COLOR_CODEC.optionalFieldOf(
                    "water_surface_color").forGetter(BiomeClientInfo::waterSurfaceColor),
            COLOR_CODEC.optionalFieldOf("water_fog_color").forGetter(BiomeClientInfo::waterFogColor),
            ExtraCodecs.floatRange(0.0F, 1.0F)
                    .optionalFieldOf("water_fog_distance")
                    .forGetter(BiomeClientInfo::waterFogDistance),
            ExtraCodecs.floatRange(0.0F, 1.0F)
                    .optionalFieldOf("water_surface_transparency")
                    .forGetter(BiomeClientInfo::waterSurfaceTransparency)).apply(instance, BiomeClientInfo::new));
    public static final StreamCodec<ByteBuf, BiomeClientInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT.apply(
                    ByteBufCodecs::optional),
            BiomeClientInfo::waterSurfaceColor,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterFogColor,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterFogDistance,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
            BiomeClientInfo::waterSurfaceTransparency,
            BiomeClientInfo::new);

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor) {
        this(Optional.of(waterSurfaceColor), Optional.of(waterFogColor), Optional.empty(), Optional.empty());
    }

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor, float waterFogDistance) {
        this(Optional.of(waterSurfaceColor),
                Optional.of(waterFogColor),
                Optional.of(waterFogDistance),
                Optional.empty());
    }

    public BiomeClientInfo(int waterSurfaceColor, int waterFogColor, float waterFogDistance, float waterSurfaceTransparency) {
        this(Optional.of(waterSurfaceColor),
                Optional.of(waterFogColor),
                Optional.of(waterFogDistance),
                Optional.of(waterSurfaceTransparency));
    }

    public float getWaterFogDistanceScale() {
        // return this as a scale, so we can apply that to the default fog distance Java Edition uses,
        // which is 96 instead of 60 in Bedrock Edition (do not want to make it worse)
        return this.waterFogDistance.orElse(1.0F);
    }

    public int getWaterSurfaceTransparency() {
        // Bedrock Edition default value is 65%, but 70% is closer to the Java Edition water texture transparency (which we remove muhahaha)
        return ARGB.as8BitChannel(this.waterSurfaceTransparency.orElse(0.75F));
    }
}
