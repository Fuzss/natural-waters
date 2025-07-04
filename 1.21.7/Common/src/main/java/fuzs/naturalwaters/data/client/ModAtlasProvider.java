package fuzs.naturalwaters.data.client;

import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.puzzleslib.api.client.data.v2.AbstractAtlasProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModAtlasProvider extends AbstractAtlasProvider {

    public ModAtlasProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addAtlases() {
        // we must manually add these, as they are dynamically created and therefore cannot be located by the directory lister
        this.addMaterial(OpaqueWaterPackResources.OPAQUE_WATER_STILL_MATERIAL);
        this.addMaterial(OpaqueWaterPackResources.OPAQUE_WATER_FLOW_MATERIAL);
    }
}
