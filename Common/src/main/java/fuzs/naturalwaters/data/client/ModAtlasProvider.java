package fuzs.naturalwaters.data.client;

import fuzs.naturalwaters.client.packs.OpaqueWaterPackResources;
import fuzs.puzzleslib.common.api.client.data.v2.AbstractAtlasProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.AtlasIds;

public class ModAtlasProvider extends AbstractAtlasProvider {

    public ModAtlasProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addAtlases() {
        // we must manually add these, as they are dynamically created and therefore cannot be located by the directory lister
        this.add(AtlasIds.BLOCKS, new SingleFile(OpaqueWaterPackResources.OPAQUE_WATER_MODEL.stillMaterial().sprite()));
        this.add(AtlasIds.BLOCKS,
                new SingleFile(OpaqueWaterPackResources.OPAQUE_WATER_MODEL.flowingMaterial().sprite()));
    }
}
