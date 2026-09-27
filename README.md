# Natural Waters

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](banner.png)

## Configuration

Natural Waters ships with per-biome water values based on Bedrock Edition. Everything that can be changed per biome is
defined inside **[resource packs](https://minecraft.wiki/w/Resource_pack)**, so it can be overridden without affecting
any other mod.

### Where files live

Per-biome water settings are stored as JSON files under:

```
assets/<namespace>/naturalwaters/biomes/<path>.json
```

The `<namespace>` and `<path>` together are the id of the biome the file applies to. For example, to configure
`minecraft:swamp`, the file must be located at:

```
assets/minecraft/naturalwaters/biomes/swamp.json
```

You only need to define the fields you want to change. Any field left out keeps its vanilla or built-in value, and
biomes without a file fall back to a built-in default chosen from biome tags.

### Available fields

| Field                   | Allowed Values               | Description                                                                                                            |
|-------------------------|------------------------------|------------------------------------------------------------------------------------------------------------------------|
| `water_fog_color`       | RGB as `String` or `Integer` | Color of the fog seen while submerged. Defaults to `water_surface_color` when omitted.                                 |
| `water_fog_distance`    | `0.0 ~ 1.0`                  | Multiplier applied to the water fog end distance, so values below `1.0` pull the fog in closer. Defaults to `1.0`.     |
| `water_surface_color`   | RGB as `String` or `Integer` | Tint color of the water surface. Defaults to the vanilla biome water color.                                            |
| `water_surface_opacity` | `0.0 ~ 1.0`                  | Opacity of the water surface. Higher values make water more opaque; it never renders fully opaque. Defaults to `0.75`. |

Colors can be written either as a hex string with an optional `#` (for example `"#44AFF5"`) or as a decimal RGB integer.

### Example

```json
{
  "water_fog_color": "#0289D5",
  "water_fog_distance": 1.0,
  "water_surface_color": "#02B0E5",
  "water_surface_opacity": 0.55
}
```
