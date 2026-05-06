package fuzs.naturalwaters.client.packs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.renderer.ModBiomeColors;
import fuzs.naturalwaters.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.packs.v1.NativeImageHelper;
import fuzs.puzzleslib.common.api.resources.v1.AbstractModPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class OpaqueWaterPackResources extends AbstractModPackResources {
    /**
     * @see net.minecraft.client.renderer.block.FluidStateModelSet#WATER_MODEL
     */
    private static final FluidModel.Unbaked WATER_MODEL = new FluidModel.Unbaked(new Material(Identifier.withDefaultNamespace(
            "block/water_still")),
            new Material(Identifier.withDefaultNamespace("block/water_flow")),
            new Material(Identifier.withDefaultNamespace("block/water_overlay")),
            BlockTintSources.water());
    public static final FluidModel.Unbaked OPAQUE_WATER_MODEL = new FluidModel.Unbaked(new Material(NaturalWaters.id(
            "block/water_still")),
            new Material(NaturalWaters.id("block/water_flow")),
            WATER_MODEL.overlayMaterial(),
            water());
    private static final Map<Identifier, Identifier> RESOURCE_LOCATIONS;

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

    static {
        ImmutableMap.Builder<Identifier, Identifier> builder = ImmutableMap.builder();
        registerTextureMapping(builder::put,
                OPAQUE_WATER_MODEL.stillMaterial().sprite(),
                WATER_MODEL.stillMaterial().sprite());
        registerTextureMapping(builder::put,
                OPAQUE_WATER_MODEL.flowingMaterial().sprite(),
                WATER_MODEL.flowingMaterial().sprite());
        RESOURCE_LOCATIONS = builder.build();
    }

    public static BlockTintSource water() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return ModBiomeColors.getAverageWaterColor(level, pos);
            }
        };
    }

    private static void registerTextureMapping(BiConsumer<Identifier, Identifier> consumer, Identifier providedResourceLocation, Identifier originalResourceLocation) {
        consumer.accept(getTextureLocation(providedResourceLocation), getTextureLocation(originalResourceLocation));
        consumer.accept(getMetadataLocation(providedResourceLocation), getMetadataLocation(originalResourceLocation));
    }

    private static Identifier getTextureLocation(Identifier identifier) {
        return identifier.withPath((String s) -> "textures/" + s + ".png");
    }

    private static Identifier getMetadataLocation(Identifier identifier) {
        return identifier.withPath((String s) -> "textures/" + s + ".png.mcmeta");
    }

    public static FluidModel.Unbaked getFluidModel() {
        if (NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
            return OPAQUE_WATER_MODEL;
        } else {
            return WATER_MODEL;
        }
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, Identifier identifier) {
        if (RESOURCE_LOCATIONS.containsKey(identifier)) {
            Optional<Resource> optional = this.resourceManager.getResource(RESOURCE_LOCATIONS.get(identifier));
            if (optional.isPresent()) {
                if (identifier.getPath().endsWith(".png")) {
                    try (NativeImage nativeImage = NativeImage.read(optional.get().open())) {
                        for (int x = 0; x < nativeImage.getWidth(); x++) {
                            for (int y = 0; y < nativeImage.getHeight(); y++) {
                                int pixel = nativeImage.getPixel(x, y);
                                int alpha = ARGB.alpha(pixel);
                                if (alpha != 0) {
                                    nativeImage.setPixel(x, y, ARGB.opaque(pixel));
                                }
                            }
                        }
                        byte[] byteArray = NativeImageHelper.asByteArray(nativeImage);
                        return () -> new ByteArrayInputStream(byteArray);
                    } catch (IOException ignored) {
                        // NO-OP
                    }
                }

                return optional.get()::open;
            } else {
                return null;
            }
        } else {
            return super.getResource(packType, identifier);
        }
    }
}
