package fuzs.naturalwaters.common.client.packs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.naturalwaters.common.NaturalWaters;
import fuzs.puzzleslib.common.api.client.packs.v1.NativeImageHelper;
import fuzs.puzzleslib.common.api.resources.v1.AbstractModPackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public class OpaqueWaterPackResources extends AbstractModPackResources {
    private static final Identifier VANILLA_WATER_STILL = FluidStateModelSet.WATER_MODEL.stillMaterial().sprite();
    /**
     * @see FluidModel.Unbaked#stillMaterial()
     */
    public static final Identifier WATER_STILL = NaturalWaters.id(VANILLA_WATER_STILL.getPath());
    private static final Identifier VANILLA_WATER_FLOW = FluidStateModelSet.WATER_MODEL.flowingMaterial().sprite();
    /**
     * @see FluidModel.Unbaked#flowingMaterial()
     */
    public static final Identifier WATER_FLOW = NaturalWaters.id(VANILLA_WATER_FLOW.getPath());

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
    private final Map<Identifier, Identifier> sprites;

    public OpaqueWaterPackResources() {
        this(ImmutableMap.<Identifier, Identifier>builder()
                .put(sprite(WATER_STILL), sprite(VANILLA_WATER_STILL))
                .put(metadata(WATER_STILL), metadata(VANILLA_WATER_STILL))
                .put(sprite(WATER_FLOW), sprite(VANILLA_WATER_FLOW))
                .put(metadata(WATER_FLOW), metadata(VANILLA_WATER_FLOW))
                .build());
    }

    private OpaqueWaterPackResources(Map<Identifier, Identifier> sprites) {
        this.sprites = sprites;
    }

    private static Identifier sprite(Identifier identifier) {
        return identifier.withPath((String path) -> "textures/" + path + ".png");
    }

    private static Identifier metadata(Identifier identifier) {
        return identifier.withPath((String path) -> "textures/" + path + ".png.mcmeta");
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, Identifier identifier) {
        if (this.sprites.containsKey(identifier)) {
            Identifier vanillaSprite = this.sprites.get(identifier);
            Optional<Resource> optional = this.resourceManager.getResource(vanillaSprite);
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
