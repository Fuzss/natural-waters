package fuzs.naturalwaters.client.packs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.naturalwaters.NaturalWaters;
import fuzs.puzzleslib.api.client.packs.v1.NativeImageHelper;
import fuzs.puzzleslib.api.resources.v1.AbstractModPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
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

public class OpaqueWaterPackResources extends AbstractModPackResources {
    static final ResourceLocation WATER_STILL = NaturalWaters.id("block/water_still");
    static final ResourceLocation WATER_FLOW = NaturalWaters.id("block/water_flow");
    public static final Material WATER_STILL_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, WATER_STILL);
    public static final Material WATER_FLOW_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, WATER_FLOW);
    static final ResourceLocation VANILLA_WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
    static final ResourceLocation VANILLA_WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
    static final Map<ResourceLocation, ResourceLocation> SPRITES = ImmutableMap.<ResourceLocation, ResourceLocation>builder()
            .put(map(WATER_STILL), map(VANILLA_WATER_STILL))
            .put(map(WATER_FLOW), map(VANILLA_WATER_FLOW))
            .build();
    static final Map<ResourceLocation, ResourceLocation> META = ImmutableMap.<ResourceLocation, ResourceLocation>builder()
            .put(metaMap(WATER_STILL), metaMap(VANILLA_WATER_STILL))
            .put(metaMap(WATER_FLOW), metaMap(VANILLA_WATER_FLOW))
            .build();

    static ResourceLocation map(ResourceLocation resourceLocation) {
        return resourceLocation.withPath(s -> "textures/" + s + ".png");
    }

    static ResourceLocation metaMap(ResourceLocation resourceLocation) {
        return resourceLocation.withPath(s -> "textures/" + s + ".png.mcmeta");
    }

    private final ResourceManager resourceManager;

    {
        Minecraft minecraft = Minecraft.getInstance();
        this.resourceManager = minecraft.getResourceManager();
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if (SPRITES.containsKey(resourceLocation)) {
            Optional<Resource> resource = this.resourceManager.getResource(SPRITES.get(resourceLocation));
            if (resource.isPresent()) {
                try (NativeImage nativeImage = NativeImage.read(resource.get().open())) {
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
                return resource.get()::open;
            } else {
                return null;
            }
        } else if (META.containsKey(resourceLocation)) {
            return this.resourceManager.getResource(META.get(resourceLocation))
                    .<IoSupplier<InputStream>>map((Resource resource) -> resource::open)
                    .orElse(null);
        } else {
            return super.getResource(packType, resourceLocation);
        }
    }
}
