package com.blockbreakmodifier;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BlockBreakConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("blockbreakmodifier-config.yml");

    private static Map<String, BlockEntry> blockEntries = new HashMap<>();

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            writeDefault();
            return;
        }
        try (InputStream is = Files.newInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(is);
            blockEntries = parse(root);
            BlockBreakModifier.LOGGER.info("BlockBreakModifier: loaded {} block override(s).", blockEntries.size());
        } catch (IOException e) {
            BlockBreakModifier.LOGGER.error("BlockBreakModifier: failed to read config.", e);
        }
    }

    private static Map<String, BlockEntry> parse(Map<String, Object> root) {
        Map<String, BlockEntry> result = new HashMap<>();
        if (root == null) return result;
        Object blocksObj = root.get("blocks");
        if (!(blocksObj instanceof Map)) return result;
        @SuppressWarnings("unchecked")
        Map<String, Object> blocks = (Map<String, Object>) blocksObj;
        for (Map.Entry<String, Object> blockEntry : blocks.entrySet()) {
            String blockId = blockEntry.getKey().replace(".", ":");
            if (!(blockEntry.getValue() instanceof Map)) continue;
            @SuppressWarnings("unchecked")
            Map<String, Object> blockData = (Map<String, Object>) blockEntry.getValue();
            Map<String, Float> toolSpeeds = new HashMap<>();
            Object breakingToolsObj = blockData.get("breaking-tools");
            if (breakingToolsObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tools = (Map<String, Object>) breakingToolsObj;
                for (Map.Entry<String, Object> toolEntry : tools.entrySet()) {
                    String toolId = toolEntry.getKey().replace(".", ":");
                    float speed = toFloat(toolEntry.getValue(), 1.0f);
                    toolSpeeds.put(toolId, speed);
                }
            }
            Float blastResistance = null;
            if (blockData.containsKey("blast-resistance")) {
                blastResistance = toFloat(blockData.get("blast-resistance"), -1f);
                if (blastResistance < 0) blastResistance = null;
            }
            result.put(blockId, new BlockEntry(toolSpeeds, blastResistance));
        }
        return result;
    }

    private static float toFloat(Object obj, float fallback) {
        if (obj instanceof Number) return ((Number) obj).floatValue();
        try { return Float.parseFloat(obj.toString()); } catch (Exception e) { return fallback; }
    }

    private static void writeDefault() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (InputStream template = BlockBreakConfig.class
                    .getResourceAsStream("/blockbreakmodifier-config.yml")) {
                if (template != null) {
                    Files.copy(template, CONFIG_PATH);
                }
            }
            BlockBreakModifier.LOGGER.info("BlockBreakModifier: default config created.");
        } catch (IOException e) {
            BlockBreakModifier.LOGGER.error("BlockBreakModifier: failed to write default config.", e);
        }
    }

    public static Optional<Float> getToolSpeed(String blockId, String toolId) {
        BlockEntry entry = blockEntries.get(blockId);
        if (entry == null) return Optional.empty();
        return Optional.ofNullable(entry.toolSpeeds().get(toolId));
    }

    public static boolean hasBlockOverride(String blockId) {
        return blockEntries.containsKey(blockId);
    }

    public static Optional<Float> getBlastResistance(String blockId) {
        BlockEntry entry = blockEntries.get(blockId);
        if (entry == null || entry.blastResistance() == null) return Optional.empty();
        return Optional.of(entry.blastResistance());
    }

    public static Map<String, BlockEntry> getBlockEntries() {
        return Collections.unmodifiableMap(blockEntries);
    }

    public record BlockEntry(Map<String, Float> toolSpeeds, Float blastResistance) {}
}
