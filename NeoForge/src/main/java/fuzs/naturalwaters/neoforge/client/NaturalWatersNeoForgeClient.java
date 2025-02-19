package fuzs.naturalwaters.neoforge.client;

import fuzs.naturalwaters.NaturalWaters;
import fuzs.naturalwaters.client.NaturalWatersClient;
import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.naturalwaters.neoforge.data.client.ModAtlasProvider;
import fuzs.naturalwaters.neoforge.mixin.client.accessor.ClientExtensionsManagerNeoForgeAccessor;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Map;

@Mod(value = NaturalWaters.MOD_ID, dist = Dist.CLIENT)
public class NaturalWatersNeoForgeClient {

    public NaturalWatersNeoForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(NaturalWaters.MOD_ID, NaturalWatersClient::new);
        registerLoadingHandlers(modContainer.getEventBus());
        DataProviderHelper.registerDataProviders(NaturalWaters.MOD_ID, ModAtlasProvider::new);
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final FMLClientSetupEvent evt) -> {
            evt.enqueueWork(() -> {
                Map<FluidType, IClientFluidTypeExtensions> fluidTypeIClientFluidTypeExtensionsMap = ClientExtensionsManagerNeoForgeAccessor.naturalwaters$getFluidTypeExtensions();
                fluidTypeIClientFluidTypeExtensionsMap.put(NeoForgeMod.WATER_TYPE.value(),
                        new IClientFluidTypeExtensions() {
                            private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.withDefaultNamespace(
                                    "textures/misc/underwater.png");

                            @Override
                            public ResourceLocation getStillTexture() {
                                return OpaqueWaterPackResources.WATER_STILL_MATERIAL.texture();
                            }

                            @Override
                            public ResourceLocation getFlowingTexture() {
                                return OpaqueWaterPackResources.WATER_FLOW_MATERIAL.texture();
                            }

                            @Override
                            public ResourceLocation getOverlayTexture() {
                                return ModelBakery.WATER_OVERLAY.texture();
                            }

                            @Override
                            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                                return UNDERWATER_LOCATION;
                            }

                            @Override
                            public int getTintColor() {
                                return 0x3F76E4;
                            }

                            @Override
                            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                                return ARGB.opaque(BiomeColors.getAverageWaterColor(getter, pos));
                            }
                        });
            });
        });
    }
}
