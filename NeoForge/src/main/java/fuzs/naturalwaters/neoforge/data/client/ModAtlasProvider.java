package fuzs.naturalwaters.neoforge.data.client;

import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractAtlasProvider;

public class ModAtlasProvider extends AbstractAtlasProvider {

    public ModAtlasProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addAtlases() {
        // we must manually add these, as they are dynamically created and therefore cannot be located by the directory lister
        this.material(OpaqueWaterPackResources.OPAQUE_WATER_STILL_MATERIAL);
        this.material(OpaqueWaterPackResources.OPAQUE_WATER_FLOW_MATERIAL);
    }
}
