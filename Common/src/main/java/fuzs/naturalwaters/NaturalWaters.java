package fuzs.naturalwaters;

import fuzs.naturalwaters.init.ModRegistry;
import fuzs.naturalwaters.world.level.biome.WaterColor;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.BiomeModificationContext;
import fuzs.puzzleslib.api.biome.v1.SpecialEffectsContext;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.BiomeModificationsContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class NaturalWaters implements ModConstructor {
    public static final String MOD_ID = "naturalwaters";
    public static final String MOD_NAME = "Natural Waters";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    @Override
    public void onRegisterBiomeModifications(BiomeModificationsContext context) {
        MutableObject<Holder<Biome>> biome = new MutableObject<>();
        context.registerBiomeModification(BiomeLoadingPhase.MODIFICATIONS, biomeLoadingContext -> {
            biome.setValue(biomeLoadingContext.holder());
            return true;
        }, (BiomeModificationContext biomeModificationContext) -> {
            Holder<Biome> holder = biome.getValue();
            Objects.requireNonNull(holder);
            WaterColor waterColor = WaterColor.getWaterColor(holder);
            SpecialEffectsContext specialEffectsContext = biomeModificationContext.specialEffects();
            waterColor.waterSurfaceColor().ifPresent(specialEffectsContext::setWaterColor);
            waterColor.waterFogColor().ifPresent(specialEffectsContext::setWaterFogColor);
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
