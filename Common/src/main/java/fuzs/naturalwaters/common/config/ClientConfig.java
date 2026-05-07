package fuzs.naturalwaters.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Override biome water surface colors.", gameRestart = true)
    public boolean waterSurfaceColor = true;
    @Config(description = "Override biome water fog colors.")
    public boolean waterFogColor = true;
    @Config(description = "Override biome water fog distance.")
    public boolean waterFogDistance = true;
    @Config(description = "Render water blocks with a varying transparency depending on the current biome.",
            gameRestart = true)
    public boolean waterSurfaceTransparency = true;
}
