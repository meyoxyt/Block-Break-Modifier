package com.blockbreakmodifier;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BlockBreakConfig {

    private static final Path CONFIG_ROOT = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("blockbreakmodifier");

    private static final Path GLOBAL_CONFIG = CONFIG_ROOT.resolve("blockbreakmodifier-config.yml");

    private static volatile Map<String, BlockEntry> activeEntries = new HashMap<>();

    public static void loadGlobal() {
        ensureGlobalConfig();
        activeEntries = parseFile(GLOBAL_CONFIG);
        BlockBreakModifier.LOGGER.info(
                "[BBM] Loaded global config ({} override(s)).", activeEntries.size());
    }

    public static void loadForWorld(String worldFolderName) {
        ensureGlobalConfig();
        Map<String, BlockEntry> global = parseFile(GLOBAL_CONFIG);

        Path worldDir    = CONFIG_ROOT.resolve(worldFolderName);
        Path worldConfig = worldDir.resolve("blockbreakmodifier-config.yml");
        ensureWorldConfig(worldDir, worldConfig, worldFolderName);

        Map<String, BlockEntry> world  = parseFile(worldConfig);
        Map<String, BlockEntry> merged = new HashMap<>(world);
        global.forEach(merged::put);

        activeEntries = merged;
        BlockBreakModifier.LOGGER.info(
                "[BBM] Loaded config for '{}' — world={}, global={}, merged={}.",
                worldFolderName, world.size(), global.size(), merged.size());
    }

    // -------------------------------------------------------------------------
    // Parsing
    // -------------------------------------------------------------------------

    private static Map<String, BlockEntry> parseFile(Path path) {
        if (!Files.exists(path)) return new HashMap<>();
        try (InputStream is = Files.newInputStream(path)) {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(is);
            return parse(root);
        } catch (IOException e) {
            BlockBreakModifier.LOGGER.error("[BBM] Failed to read config at {}.", path, e);
            return new HashMap<>();
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, BlockEntry> parse(Map<String, Object> root) {
        Map<String, BlockEntry> result = new HashMap<>();
        if (root == null) return result;
        Object blocksObj = root.get("blocks");
        if (!(blocksObj instanceof Map<?, ?> rawBlocks)) return result;

        for (Map.Entry<?, ?> entry : rawBlocks.entrySet()) {
            String blockId = entry.getKey().toString().replace(".", ":");
            if (!(entry.getValue() instanceof Map<?, ?> rawBlockData)) continue;

            // --- blast-resistance (block level) ---
            Float blastResistance = null;
            if (rawBlockData.containsKey("blast-resistance")) {
                float val = toFloat(rawBlockData.get("blast-resistance"), -1f);
                if (val >= 0) blastResistance = val;
            }

            // --- block-level droppable default ---
            // Used as fallback when a tool entry doesn't specify its own droppable.
            Boolean blockDropDefault = null;
            if (rawBlockData.containsKey("droppable")) {
                blockDropDefault = parseBool(rawBlockData.get("droppable"));
            }

            // --- breaking-tools ---
            // Each tool entry can be:
            //   simple:  minecraft.iron_pickaxe: 100.0
            //   map:     minecraft.iron_pickaxe:
            //              speed: 100.0
            //              droppable: true
            Map<String, Float>   toolSpeeds    = new HashMap<>();
            Map<String, Boolean> toolDroppable = new HashMap<>();

            Object toolsObj = rawBlockData.get("breaking-tools");
            if (toolsObj instanceof Map<?, ?> rawTools) {
                for (Map.Entry<?, ?> toolEntry : rawTools.entrySet()) {
                    String toolId = toolEntry.getKey().toString().replace(".", ":");
                    Object toolVal = toolEntry.getValue();

                    if (toolVal instanceof Map<?, ?> toolMap) {
                        // Extended syntax: speed + optional droppable per tool
                        if (toolMap.containsKey("speed")) {
                            toolSpeeds.put(toolId, toFloat(toolMap.get("speed"), 1.0f));
                        }
                        if (toolMap.containsKey("droppable")) {
                            Boolean d = parseBool(toolMap.get("droppable"));
                            if (d != null) toolDroppable.put(toolId, d);
                        }
                    } else {
                        // Simple syntax: just a speed number
                        toolSpeeds.put(toolId, toFloat(toolVal, 1.0f));
                        // Inherit block-level droppable default if set
                        if (blockDropDefault != null) toolDroppable.put(toolId, blockDropDefault);
                    }
                }
            }

            result.put(blockId, new BlockEntry(toolSpeeds, blastResistance, toolDroppable, blockDropDefault));
        }
        return result;
    }

    private static float toFloat(Object obj, float fallback) {
        if (obj instanceof Number n) return n.floatValue();
        try { return Float.parseFloat(obj.toString()); }
        catch (Exception e) { return fallback; }
    }

    private static Boolean parseBool(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Boolean b) return b;
        String s = obj.toString().trim().toLowerCase(Locale.ROOT);
        if (s.equals("true") || s.equals("yes") || s.equals("1")) return true;
        if (s.equals("false") || s.equals("no") || s.equals("0")) return false;
        return null;
    }

    // -------------------------------------------------------------------------
    // File bootstrap helpers
    // -------------------------------------------------------------------------

    private static void ensureGlobalConfig() {
        if (!Files.exists(GLOBAL_CONFIG)) {
            try {
                Files.createDirectories(CONFIG_ROOT);
                try (InputStream src = BlockBreakConfig.class
                        .getResourceAsStream("/blockbreakmodifier-config.yml")) {
                    if (src != null) Files.copy(src, GLOBAL_CONFIG);
                }
                BlockBreakModifier.LOGGER.info("[BBM] Created default global config.");
            } catch (IOException e) {
                BlockBreakModifier.LOGGER.error("[BBM] Failed to create global config.", e);
            }
        }
    }

    private static void ensureWorldConfig(Path worldDir, Path worldConfig, String worldName) {
        if (!Files.exists(worldConfig)) {
            try {
                Files.createDirectories(worldDir);
                try (InputStream src = BlockBreakConfig.class
                        .getResourceAsStream("/blockbreakmodifier-world-config.yml")) {
                    if (src != null) Files.copy(src, worldConfig);
                }
                BlockBreakModifier.LOGGER.info("[BBM] Created default config for world '{}'.", worldName);
            } catch (IOException e) {
                BlockBreakModifier.LOGGER.error("[BBM] Failed to create world config for '{}'.", worldName, e);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public static Optional<Float> getToolSpeed(String blockId, String toolId) {
        BlockEntry entry = activeEntries.get(blockId);
        if (entry == null) return Optional.empty();
        return Optional.ofNullable(entry.toolSpeeds().get(toolId));
    }

    public static boolean hasBlockOverride(String blockId) {
        return activeEntries.containsKey(blockId);
    }

    public static Optional<Float> getBlastResistance(String blockId) {
        BlockEntry entry = activeEntries.get(blockId);
        if (entry == null || entry.blastResistance() == null) return Optional.empty();
        return Optional.of(entry.blastResistance());
    }

    /**
     * Returns the droppable override for a specific block+tool combination.
     * Lookup order:
     *   1. Per-tool droppable set in extended syntax
     *   2. Block-level droppable default
     *   3. null = vanilla drop logic
     */
    public static Boolean getToolDroppable(String blockId, String toolId) {
        BlockEntry entry = activeEntries.get(blockId);
        if (entry == null) return null;
        Boolean perTool = entry.toolDroppable().get(toolId);
        if (perTool != null) return perTool;
        return entry.blockDropDefault(); // may be null = vanilla
    }

    public static Map<String, BlockEntry> getActiveEntries() {
        return Collections.unmodifiableMap(activeEntries);
    }

    /**
     * @param toolSpeeds      map of toolId -> break speed
     * @param blastResistance override blast resistance, null = vanilla
     * @param toolDroppable   per-tool droppable overrides
     * @param blockDropDefault block-level droppable fallback, null = vanilla
     */
    public record BlockEntry(
            Map<String, Float>   toolSpeeds,
            Float                blastResistance,
            Map<String, Boolean> toolDroppable,
            Boolean              blockDropDefault
    ) {}
}
