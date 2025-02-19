package fuzs.naturalwaters.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.naturalwaters.init.ModRegistry;
import fuzs.neoforgedatapackextensions.api.v1.DataMapRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record WaterColor(Optional<Integer> waterSurfaceColor,
                         Optional<Integer> waterFogColor,
                         Optional<Integer> waterFogDistance,
                         Optional<Float> waterSurfaceTransparency) {
    public static final WaterColor DEFAULT = new WaterColor(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
    public static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(TextColor.CODEC.xmap(TextColor::getValue,
            TextColor::fromRgb), ExtraCodecs.RGB_COLOR_CODEC);
    public static final Codec<WaterColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(COLOR_CODEC.optionalFieldOf(
                    "water_surface_color").forGetter(WaterColor::waterSurfaceColor),
            COLOR_CODEC.optionalFieldOf("water_fog_color").forGetter(WaterColor::waterFogColor),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("water_fog_distance").forGetter(WaterColor::waterFogDistance),
            ExtraCodecs.floatRange(0.0F, 1.0F)
                    .optionalFieldOf("water_surface_transparency")
                    .forGetter(WaterColor::waterSurfaceTransparency)).apply(instance, WaterColor::new));
    public static final StreamCodec<ByteBuf, WaterColor> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT.apply(
                    ByteBufCodecs::optional),
            WaterColor::waterSurfaceColor,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            WaterColor::waterFogColor,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional),
            WaterColor::waterFogDistance,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional),
            WaterColor::waterSurfaceTransparency,
            WaterColor::new);

    public WaterColor(int waterSurfaceColor, int waterFogColor) {
        this(Optional.of(waterSurfaceColor), Optional.of(waterFogColor), Optional.empty(), Optional.empty());
    }

    public WaterColor(int waterSurfaceColor, int waterFogColor, int waterFogDistance) {
        this(Optional.of(waterSurfaceColor),
                Optional.of(waterFogColor),
                Optional.of(waterFogDistance),
                Optional.empty());
    }

    public WaterColor(int waterSurfaceColor, int waterFogColor, int waterFogDistance, float waterSurfaceTransparency) {
        this(Optional.of(waterSurfaceColor),
                Optional.of(waterFogColor),
                Optional.of(waterFogDistance),
                Optional.of(waterSurfaceTransparency));
    }

    public float getWaterFogDistanceScale() {
        return this.waterFogDistance.orElse(60) / 60.0F;
    }

    public int getWaterSurfaceTransparency() {
        return ARGB.as8BitChannel(this.waterSurfaceTransparency.orElse(0.7F));
    }

    public static WaterColor getWaterColor(Holder<Biome> holder) {
        WaterColor waterColor = DataMapRegistry.INSTANCE.getData(ModRegistry.WATER_COLOR_DATA_MAP, holder);
        return waterColor != null ? waterColor : DEFAULT;
    }
}
