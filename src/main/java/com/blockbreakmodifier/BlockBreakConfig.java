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

    private static Map<String, BlockEntry> parse(Map<String, Object> root) {
        Map<String, BlockEntry> result = new HashMap<>();
        if (root == null) return result;
        Object blocksObj = root.get("blocks");
        if (!(blocksObj instanceof Map<?, ?> rawBlocks)) return result;
        for (Map.Entry<?, ?> entry : rawBlocks.entrySet()) {
            String blockId = entry.getKey().toString().replace(".", ":");
            if (!(entry.getValue() instanceof Map<?, ?> rawBlockData)) continue;
            Map<String, Float> toolSpeeds = new HashMap<>();
            Object toolsObj = rawBlockData.get("breaking-tools");
            if (toolsObj instanceof Map<?, ?> rawTools) {
                for (Map.Entry<?, ?> toolEntry : rawTools.entrySet()) {
                    String toolId = toolEntry.getKey().toString().replace(".", ":");
                    toolSpeeds.put(toolId, toFloat(toolEntry.getValue(), 1.0f));
                }
            }
            Float blastResistance = null;
            if (rawBlockData.containsKey("blast-resistance")) {
                float val = toFloat(rawBlockData.get("blast-resistance"), -1f);
                if (val >= 0) blastResistance = val;
            }
            result.put(blockId, new BlockEntry(toolSpeeds, blastResistance));
        }
        return result;
    }

    private static float toFloat(Object obj, float fallback) {
        if (obj instanceof Number n) return n.floatValue();
        try { return Float.parseFloat(obj.toString()); }
        catch (Exception e) { return fallback; }
    }

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

    public static Map<String, BlockEntry> getActiveEntries() {
        return Collections.unmodifiableMap(activeEntries);
    }

    public record BlockEntry(Map<String, Float> toolSpeeds, Float blastResistance) {}
}
