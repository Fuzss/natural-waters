package fuzs.naturalwaters.neoforge.mixin.client.accessor;

import net.neoforged.neoforge.client.extensions.common.ClientExtensionsManager;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ClientExtensionsManager.class)
public interface ClientExtensionsManagerNeoForgeAccessor {

    @Accessor("FLUID_TYPE_EXTENSIONS")
    static Map<FluidType, IClientFluidTypeExtensions> naturalwaters$getFluidTypeExtensions() {
        throw new RuntimeException();
    }
}
