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

| Field                   | Allowed Values                               | Description                                                                                                            |
|-------------------------|----------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| `water_fog_color`       | RGB as `Hex color`, `Decimal`, or `Vector3f` | Color of the fog seen while submerged. Defaults to `water_surface_color` when omitted.                                 |
| `water_fog_distance`    | `0.0 ~ 1.0`                                  | Multiplier applied to the water fog end distance, so values below `1.0` pull the fog in closer. Defaults to `1.0`.     |
| `water_surface_color`   | RGB as `Hex color`, `Decimal`, or `Vector3f` | Tint color of the water surface. Defaults to the vanilla biome water color.                                            |
| `water_surface_opacity` | `0.0 ~ 1.0`                                  | Opacity of the water surface. Higher values make water more opaque; it never renders fully opaque. Defaults to `0.75`. |

Color values support the following formats:

- A hex string that must begin with `#` and contain exactly six hex digits, for example `"#44AFF5"`.
- A decimal RGB integer, for example `4504565`.
- A list of three floats `[r, g, b]` in the range `0.0 ~ 1.0`, for example `[0.0, 0.5, 1.0]`.

### Example

```json
{
  "water_fog_color": "#0289d5",
  "water_fog_distance": 1.0,
  "water_surface_color": "#02b0e5",
  "water_surface_opacity": 0.55
}
```

### Built-in defaults

All vanilla biomes already ship with built-in values based on Bedrock Edition, so normally there is nothing to define.
These values are baked into the mod instead of being shipped as files.

For any biome without its own file — including biomes added by other mods — a default is picked at runtime from
conventional biome tags such as `c:is_ocean`, `c:is_forest`, `c:is_swamp` and `c:is_nether`. If none of those tags
match, no default is applied and the vanilla water values are kept.

This built-in fallback can be circumvented by adding a file for the biome: as soon as a file exists, the tag-based
default is ignored. An empty file (`{}`) is enough to opt out and restore default water colors and fog, which is
handy for modded biomes that incorrectly match one of the tags.
