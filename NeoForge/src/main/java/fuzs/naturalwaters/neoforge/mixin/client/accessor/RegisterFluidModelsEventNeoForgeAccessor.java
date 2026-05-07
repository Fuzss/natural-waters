package fuzs.naturalwaters.neoforge.mixin.client.accessor;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RegisterFluidModelsEvent.class)
public interface RegisterFluidModelsEventNeoForgeAccessor {
    @Accessor("models")
    Map<Fluid, FluidModel> naturalwaters$getModels();

    @Accessor("materials")
    MaterialBaker naturalwaters$getMaterials();
}
