<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.11-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 1.21.11"/>
  <img src="https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge" alt="Fabric"/>
  <img src="https://img.shields.io/badge/Version-1.0.0-orange?style=for-the-badge" alt="v1.0.0"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="MIT License"/>
  <img src="https://img.shields.io/badge/Author-ELITE%20Studios-red?style=for-the-badge" alt="ELITE Studios"/>
</p>

<h1 align="center">🔨 BlockBreakModifier</h1>

<p align="center">
  A fully customizable <strong>Fabric mod</strong> for Minecraft <strong>1.21.11</strong> that lets you override
  <em>which tools</em> break any block, <em>how fast</em>, and <em>blast resistance</em> —
  all through YAML config files with <strong>per-world configs</strong>, a <strong>global config</strong>,
  and an <strong>in-game reload button</strong>. No restart ever needed.
</p>

---

## ✨ Features

- **Override breaking tools** — make any block breakable by any tool, or bare hand
- **Override breaking speed** — exact float speed per tool per block
- **Override blast resistance** — control TNT/creeper resistance per block
- **Per-world configs** — every singleplayer world gets its own config folder, auto-created on first join
- **Global config** — one file that overrides all worlds and is the only config used on servers
- **In-game reload button** — `↻ BBM` button on the world list, hover a world and click
- **No restart needed** — edit config, hit the button, changes apply instantly
- **Zero extra dependencies** — SnakeYAML 2.2 is bundled inside the jar
- **Server support** — global config applies on dedicated servers at startup
- **Open source** — MIT licensed

---

## 📦 Installation

1. Install [Fabric Loader 0.18.1+](https://fabricmc.net/use/) for Minecraft **1.21.11**
2. Install [Fabric API 0.141.3+1.21.11](https://modrinth.com/mod/fabric-api)
3. Drop `blockbreakmodifier-1.0.0.jar` into your `mods/` folder
4. Launch the game — configs are auto-created:
   ```
   .minecraft/config/blockbreakmodifier/blockbreakmodifier-config.yml          <- global
   .minecraft/config/blockbreakmodifier/<WorldName>/blockbreakmodifier-config.yml  <- per-world
   ```

---

## 🗂️ Config Structure

```
config/
└── blockbreakmodifier/
    ├── blockbreakmodifier-config.yml        <- GLOBAL (all worlds + servers, highest priority)
    ├── MyWorld/
    │   └── blockbreakmodifier-config.yml    <- only for "MyWorld"
    ├── SurvivalSMP/
    │   └── blockbreakmodifier-config.yml    <- only for "SurvivalSMP"
    └── HardcoreRun/
        └── blockbreakmodifier-config.yml    <- only for "HardcoreRun"
```

> **Priority:** Global config always wins. If the same block is in both a world config and the global config, the global entry is used.

---

## ⚙️ Config Format

Both configs use the same YAML format:

```yaml
blocks:
  <namespace>.<block_name>:
    blast-resistance: <float>       # optional
    breaking-tools:
      <namespace>.<tool_name>: <speed_float>
```

> Replace all `:` in Minecraft IDs with `.` — the mod converts them back automatically.
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

Efficiency enchantments stack on top of this base value.

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

Set to `0.0` to make a block fully destroyable by TNT. Leave the field out to keep vanilla behavior.

---

## 📝 Config Examples

### Example 1 — Obsidian with Wooden Pickaxe, near-instant, TNT-destroyable

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

```yaml
blocks:
  minecraft.stone:
    breaking-tools:
      minecraft.air: 5.0
```

### Example 4 — Multiple Blocks

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

---

## 🔄 Reloading Without Restart

1. Edit any config file and save
2. Open **Singleplayer** world list
3. **Hover** your mouse over the target world
4. Click **`↻ BBM`** at the bottom-right of the world entry
5. A green chat message confirms: config is now live

On **dedicated servers**, the global config loads at startup. A `/bbm reload` command is planned for a future release.

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
│   ├── BlockBreakModifier.java              # Mod entrypoint
│   ├── BlockBreakConfig.java               # YAML loader, per-world/global merge, data store
│   ├── client/
│   │   └── BlockBreakModifierClient.java   # Client entrypoint + world join/leave hooks
│   └── mixin/
│       ├── MiningSpeedMixin.java           # Intercepts PlayerEntity#getBlockBreakingSpeed
│       ├── BlastResistanceMixin.java       # Intercepts AbstractBlockState#getBlastResistance
│       ├── WorldJoinMixin.java             # Detects joinWorld / disconnect (client-only)
│       └── WorldListEntryMixin.java        # Injects the ↻ BBM button into world list
├── src/main/resources/
│   ├── fabric.mod.json
│   ├── blockbreakmodifier.mixins.json      # Server+client mixins
│   ├── blockbreakmodifier.client.mixins.json  # Client-only mixins
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

Output: `build/libs/blockbreakmodifier-1.0.0.jar`

---

## 🤝 Contributing

Pull requests and issues are welcome. Please open an issue before any large change.

---

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.

---

<p align="center">
  Made with ❤️ by <strong>ELITE Studios</strong> &nbsp;|&nbsp;
  <a href="https://plugincenter.store">plugincenter.store</a>
</p>
