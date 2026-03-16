<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.1-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 1.21.1"/>
  <img src="https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge" alt="Fabric"/>
  <img src="https://img.shields.io/badge/Version-2.0.0-orange?style=for-the-badge" alt="v2.0.0"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="MIT License"/>
  <img src="https://img.shields.io/badge/Author-ELITE%20Studios-red?style=for-the-badge" alt="ELITE Studios"/>
</p>

<h1 align="center">🔨 BlockBreakModifier</h1>

<p align="center">
  A fully customizable <strong>Fabric mod</strong> that lets you override <em>which tools</em> break any block and <em>how fast</em>,
  plus <em>blast resistance</em> — all through simple YAML config files, with <strong>per-world configs</strong>,
  a <strong>global config</strong>, and an <strong>in-game reload button</strong> so you never need to restart.
</p>

---

## ✨ Features

- **Override breaking tools** — make any block breakable by any tool (or bare hand)
- **Override breaking speed** — set an exact float speed per tool per block
- **Override blast resistance** — control how resistant any block is to explosions
- **Per-world configs** — every singleplayer world gets its own config folder
- **Global config** — one file that overrides all worlds and applies on servers
- **In-game reload** — `↻ BBM` button on world list, no restart needed
- **Zero extra dependencies** — SnakeYAML is bundled inside the jar
- **Server support** — global config applies on dedicated servers identically
- **Open source** — MIT licensed, contributions welcome

---

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **1.21.1**
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop `blockbreakmodifier-2.0.0.jar` into your `mods/` folder
4. Launch the game — config files are auto-created:
   ```
   config/blockbreakmodifier/blockbreakmodifier-config.yml          ← global
   config/blockbreakmodifier/<WorldName>/blockbreakmodifier-config.yml  ← per-world
   ```

---

## 🗂️ Config Structure

```
config/
└── blockbreakmodifier/
    ├── blockbreakmodifier-config.yml        ← GLOBAL (applies everywhere, overrides worlds)
    ├── MyWorld/
    │   └── blockbreakmodifier-config.yml    ← per-world: MyWorld only
    ├── SurvivalSMP/
    │   └── blockbreakmodifier-config.yml    ← per-world: SurvivalSMP only
    └── HardcoreWorld/
        └── blockbreakmodifier-config.yml    ← per-world: HardcoreWorld only
```

### Priority Rule

> **Global config always wins.** If the same block is defined in both a world config and the global config, the global definition is used.

This lets you set server-wide / all-worlds overrides in the global config, while keeping world-specific tweaks in each world's own file.

---

## ⚙️ Config Format

Both config files use the same YAML format:

```yaml
blocks:
  <namespace>.<block_name>:
    blast-resistance: <float>       # optional
    breaking-tools:
      <namespace>.<tool_name>: <speed_float>
```

> **Key rule:** Replace all `:` in Minecraft IDs with `.` in the config.
> The mod converts them back automatically.
> `minecraft:obsidian` → `minecraft.obsidian`

---

### 🔧 Breaking Speed Reference

| Speed | Feel |
|-------|------|
| `1.0` | Bare hand on dirt |
| `4.0` | Wooden pickaxe on stone |
| `8.0` | Diamond pickaxe on stone |
| `30.0` | Very fast |
| `50.0` | Near-instant |
| `100.0` | Instant |

---

### 💥 Blast Resistance Reference

| Block | Vanilla Value |
|-------|---------------|
| Dirt | `0.5` |
| Stone | `6.0` |
| Obsidian | `1200.0` |
| Crying Obsidian | `1200.0` |
| Ancient Debris | `1200.0` |
| Bedrock | `3600000.0` |

Set to `0.0` to make a block fully destroyable by TNT.

---

## 📝 Config Examples

### Example 1 — Obsidian with Wooden Pickaxe (near-instant, TNT-destroyable)

```yaml
blocks:
  minecraft.obsidian:
    blast-resistance: 10.0
    breaking-tools:
      minecraft.wooden_pickaxe: 50.0
      minecraft.stone_pickaxe: 80.0
      minecraft.iron_pickaxe: 100.0
```

### Example 2 — Ancient Debris with Golden Pickaxe

```yaml
blocks:
  minecraft.ancient_debris:
    breaking-tools:
      minecraft.golden_pickaxe: 60.0
```

### Example 3 — Stone with Bare Hands

> `minecraft.air` = no item held (empty hand)

```yaml
blocks:
  minecraft.stone:
    breaking-tools:
      minecraft.air: 5.0
```

### Example 4 — Multiple Blocks at Once

```yaml
blocks:
  minecraft.obsidian:
    blast-resistance: 10.0
    breaking-tools:
      minecraft.wooden_pickaxe: 50.0
      minecraft.iron_pickaxe: 100.0
  minecraft.crying_obsidian:
    blast-resistance: 10.0
    breaking-tools:
      minecraft.wooden_pickaxe: 50.0
  minecraft.diamond_ore:
    breaking-tools:
      minecraft.wooden_pickaxe: 30.0
  minecraft.netherrack:
    blast-resistance: 0.0
    breaking-tools:
      minecraft.wooden_shovel: 10.0
```

### Example 5 — Custom Modded Block

```yaml
blocks:
  somemod.custom_block:
    blast-resistance: 5.0
    breaking-tools:
      minecraft.golden_pickaxe: 20.0
      minecraft.wooden_axe: 15.0
```

---

## 🔄 Reloading Without Restart

1. Edit any config file and save it
2. Open **Singleplayer** world list
3. Hover your mouse over a world entry
4. Click the **`↻ BBM`** button that appears at the bottom-right of the entry
5. A chat message confirms the reload — changes apply instantly

On **servers**, the global config is loaded once on startup. To hot-reload on a server, a restart is currently required (server-side `/bbm reload` command planned for v2.1.0).

---

## 🛠️ Tool ID Reference

| Tool | Config Key |
|------|------------|
| Wooden Pickaxe | `minecraft.wooden_pickaxe` |
| Stone Pickaxe | `minecraft.stone_pickaxe` |
| Iron Pickaxe | `minecraft.iron_pickaxe` |
| Golden Pickaxe | `minecraft.golden_pickaxe` |
| Diamond Pickaxe | `minecraft.diamond_pickaxe` |
| Netherite Pickaxe | `minecraft.netherite_pickaxe` |
| Wooden Shovel | `minecraft.wooden_shovel` |
| Wooden Axe | `minecraft.wooden_axe` |
| Shears | `minecraft.shears` |
| Empty Hand | `minecraft.air` |
| Any modded tool | `<modid>.<tool_name>` |

---

## 🏗️ Project Structure

```
Block-Break-Modifier/
├── src/main/java/com/blockbreakmodifier/
│   ├── BlockBreakModifier.java              # Mod entrypoint (server + client)
│   ├── BlockBreakConfig.java               # YAML loader, merger, data store
│   ├── client/
│   │   └── BlockBreakModifierClient.java   # Client entrypoint, world join/leave hooks
│   └── mixin/
│       ├── MiningSpeedMixin.java           # Intercepts getBlockBreakingSpeed
│       ├── BlastResistanceMixin.java       # Intercepts getBlastResistance
│       ├── WorldJoinMixin.java             # Detects world join/leave (client-only)
│       └── WorldListEntryMixin.java        # Adds ↻ BBM button to world list
├── src/main/resources/
│   ├── fabric.mod.json
│   ├── blockbreakmodifier.mixins.json
│   ├── blockbreakmodifier.client.mixins.json
│   ├── blockbreakmodifier-config.yml        # Default global config template
│   └── blockbreakmodifier-world-config.yml  # Default per-world config template
├── build.gradle
├── gradle.properties
├── settings.gradle
├── LICENSE
└── README.md
```

---

## 🔨 Building from Source

```bash
git clone https://github.com/meyoxyt/Block-Break-Modifier.git
cd "Block-Break-Modifier"
./gradlew build
```

Output: `build/libs/blockbreakmodifier-2.0.0.jar`

---

## 🤝 Contributing

Pull requests and issues are welcome!
Please open an issue first for large changes.

---

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.

---

<p align="center">
  Made with ❤️ by <strong>ELITE Studios</strong> &nbsp;|&nbsp;
  <a href="https://plugincenter.store">plugincenter.store</a>
</p>
