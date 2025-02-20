package fuzs.naturalwaters.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Override biome water surface colors.", worldRestart = true)
    public  boolean waterSurfaceColor = true;
    @Config(description = "Override biome water fog colors.")
    public  boolean waterFogColor = true;
    @Config(description = "Override biome water fog distance.")
    public  boolean waterFogDistance = true;
    @Config(description = "Render water blocks with a varying transparency depending on the current biome.", worldRestart = true)
    public boolean waterSurfaceTransparency = true;
}
