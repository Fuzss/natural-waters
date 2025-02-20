package fuzs.naturalwaters.client.packs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.config.ClientConfig;
import fuzs.puzzleslib.api.client.packs.v1.NativeImageHelper;
import fuzs.puzzleslib.api.resources.v1.AbstractModPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class OpaqueWaterPackResources extends AbstractModPackResources {
    public static final Material WATER_STILL_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS,
            ResourceLocation.withDefaultNamespace("block/water_still"));
    public static final Material WATER_FLOW_MATERIAL = ModelBakery.WATER_FLOW;
    public static final Material OPAQUE_WATER_STILL_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS,
            NaturalWaters.id(WATER_STILL_MATERIAL.texture().getPath()));
    public static final Material OPAQUE_WATER_FLOW_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS,
            NaturalWaters.id(WATER_FLOW_MATERIAL.texture().getPath()));
    private static final Map<ResourceLocation, ResourceLocation> RESOURCE_LOCATIONS;

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

    static {
        ImmutableMap.Builder<ResourceLocation, ResourceLocation> builder = ImmutableMap.builder();
        registerTextureMapping(builder::put, OPAQUE_WATER_STILL_MATERIAL.texture(), WATER_STILL_MATERIAL.texture());
        registerTextureMapping(builder::put, OPAQUE_WATER_FLOW_MATERIAL.texture(), WATER_FLOW_MATERIAL.texture());
        RESOURCE_LOCATIONS = builder.build();
    }

    static void registerTextureMapping(BiConsumer<ResourceLocation, ResourceLocation> consumer, ResourceLocation providedResourceLocation, ResourceLocation originalResourceLocation) {
        consumer.accept(getTextureLocation(providedResourceLocation), getTextureLocation(originalResourceLocation));
        consumer.accept(getMetadataLocation(providedResourceLocation), getMetadataLocation(originalResourceLocation));
    }

    static ResourceLocation getTextureLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPath((String s) -> "textures/" + s + ".png");
    }

    static ResourceLocation getMetadataLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPath((String s) -> "textures/" + s + ".png.mcmeta");
    }

    public static Material getWaterStillMaterial() {
        if (NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
            return OPAQUE_WATER_STILL_MATERIAL;
        } else {
            return WATER_STILL_MATERIAL;
        }
    }

    public static Material getWaterFlowMaterial() {
        if (NaturalWaters.CONFIG.get(ClientConfig.class).waterSurfaceTransparency) {
            return OPAQUE_WATER_FLOW_MATERIAL;
        } else {
            return WATER_FLOW_MATERIAL;
        }
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if (RESOURCE_LOCATIONS.containsKey(resourceLocation)) {
            Optional<Resource> optional = this.resourceManager.getResource(RESOURCE_LOCATIONS.get(resourceLocation));
            if (optional.isPresent()) {
                if (resourceLocation.getPath().endsWith(".png")) {
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
            return super.getResource(packType, resourceLocation);
        }
    }
}
