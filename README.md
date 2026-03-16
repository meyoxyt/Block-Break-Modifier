<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.4-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 1.21.4"/>
  <img src="https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge" alt="Fabric"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="MIT License"/>
  <img src="https://img.shields.io/badge/Author-ELITE%20Studios-red?style=for-the-badge" alt="ELITE Studios"/>
</p>

<h1 align="center">🔨 BlockBreakModifier</h1>

<p align="center">
  A fully customizable <strong>Fabric mod</strong> that lets you override <em>which tools</em> break any block and <em>how fast</em> — all through a simple YAML config file. No more limits on what breaks what.
</p>

---

## ✨ Features

- **Override breaking tools** — make any block breakable by any tool (or bare hand)
- **Override breaking speed** — set an exact float speed per tool per block
- **Override blast resistance** — control how resistant blocks are to explosions
- **Per-block, per-tool granularity** — configure each combination independently
- **Zero extra mod dependencies** — ships with SnakeYAML bundled
- **Clean YAML config** — human-readable, fully documented, empty by default
- **Open source** — MIT licensed, contributions welcome

---

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **1.21.4**
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop the `blockbreakmodifier-*.jar` into your `mods/` folder
4. Launch the game once — the config file is auto-generated at:
   ```
   config/blockbreakmodifier-config.yml
   ```
5. Edit the config, save, and restart the game

---

## ⚙️ Configuration

The config lives at:
```
<minecraft folder>/config/blockbreakmodifier-config.yml
```

### Format Overview

```yaml
blocks:
  <namespace>.<block_name>:
    blast-resistance: <float>       # optional
    breaking-tools:
      <namespace>.<tool_name>: <speed_float>
```

> **Important:** Because YAML treats colons (`:`) as special syntax, replace all
> colons in Minecraft IDs with dots (`.`) inside the config file.  
> `minecraft:obsidian` → `minecraft.obsidian`  
> The mod converts them back automatically at runtime.

---

### 🔧 Breaking Speed Reference

| Speed Value | Feel |
|-------------|------|
| `1.0` | Bare-hand on dirt |
| `4.0` | Moderate — wooden pickaxe on stone |
| `8.0` | Fast — diamond pickaxe on stone |
| `30.0` | Very fast |
| `50.0` | Near-instant |
| `100.0` | Instant |

Vanilla speed values are multiplied by efficiency enchantments on top. A value you set here is the **base speed** returned to the game engine before enchantments are applied.

---

### 💥 Blast Resistance Reference

| Block | Vanilla Blast Resistance |
|-------|-------------------------|
| Dirt | `0.5` |
| Stone | `6.0` |
| Obsidian | `1200.0` |
| Crying Obsidian | `1200.0` |
| Ancient Debris | `1200.0` |
| Bedrock | `3600000.0` |

Set to `0.0` to make a block fully destroyable by explosions.

---

## 📝 Config Examples

### Example 1 — Obsidian with a Wooden Pickaxe (near-instant)

```yaml
blocks:
  minecraft.obsidian:
    blast-resistance: 10.0
    breaking-tools:
      minecraft.wooden_pickaxe: 50.0
      minecraft.stone_pickaxe: 80.0
      minecraft.iron_pickaxe: 100.0
```

### Example 2 — Ancient Debris with a Golden Pickaxe

```yaml
blocks:
  minecraft.ancient_debris:
    breaking-tools:
      minecraft.golden_pickaxe: 60.0
```

### Example 3 — Stone with Bare Hands

> `minecraft:air` represents an empty hand (no item held).

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

### Example 5 — Custom Mod Block

```yaml
blocks:
  somemod.custom_block:
    blast-resistance: 5.0
    breaking-tools:
      minecraft.golden_pickaxe: 20.0
      minecraft.wooden_axe: 15.0
```

---

## 🧰 Tool ID Reference

| Tool | Config Key |
|------|------------|
| Wooden Pickaxe | `minecraft.wooden_pickaxe` |
| Stone Pickaxe | `minecraft.stone_pickaxe` |
| Iron Pickaxe | `minecraft.iron_pickaxe` |
| Golden Pickaxe | `minecraft.golden_pickaxe` |
| Diamond Pickaxe | `minecraft.diamond_pickaxe` |
| Netherite Pickaxe | `minecraft.netherite_pickaxe` |
| Wooden Shovel | `minecraft.wooden_shovel` |
| Stone Shovel | `minecraft.stone_shovel` |
| Wooden Axe | `minecraft.wooden_axe` |
| Shears | `minecraft.shears` |
| Empty Hand | `minecraft.air` |
| Any modded tool | `<modid>.<tool_name>` |

---

## 🏗️ Project Structure

```
Block-Break-Modifier/
├── src/main/java/com/blockbreakmodifier/
│   ├── BlockBreakModifier.java          # Mod entrypoint
│   ├── BlockBreakConfig.java            # YAML config loader & data store
│   └── mixin/
│       ├── MiningSpeedMixin.java        # Intercepts getBlockBreakingSpeed
│       └── BlastResistanceMixin.java    # Intercepts getBlastResistance
├── src/main/resources/
│   ├── fabric.mod.json
│   ├── blockbreakmodifier.mixins.json
│   └── blockbreakmodifier-config.yml   # Bundled default config template
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
cd Block-Break-Modifier
./gradlew build
```

The compiled jar will be at `build/libs/blockbreakmodifier-1.0.0.jar`.

---

## 🤝 Contributing

Pull requests and issues are welcome!  
Please open an issue first if you plan a large change so we can discuss it.

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.

---

<p align="center">
  Made with ❤️ by <strong>ELITE Studios</strong> &nbsp;|&nbsp;
  <a href="https://plugincenter.store">plugincenter.store</a>
</p>
