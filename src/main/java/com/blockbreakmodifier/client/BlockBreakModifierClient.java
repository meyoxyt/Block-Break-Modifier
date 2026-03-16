package com.blockbreakmodifier.client;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class BlockBreakModifierClient implements ClientModInitializer {

    private static String currentWorldName = null;

    @Override
    public void onInitializeClient() {
        BlockBreakConfig.loadGlobal();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            BlockBreakConfig.loadGlobal();
        });
    }

    public static void onWorldJoin(String worldFolderName) {
        currentWorldName = worldFolderName;
        BlockBreakConfig.loadForWorld(worldFolderName);
        BlockBreakModifier.LOGGER.info("BlockBreakModifier: active world set to '{}'.", worldFolderName);
    }

    public static void onWorldLeave() {
        currentWorldName = null;
        BlockBreakConfig.loadGlobal();
    }

    public static void reloadForWorld(String worldFolderName) {
        BlockBreakConfig.loadForWorld(worldFolderName);
        BlockBreakModifier.LOGGER.info("BlockBreakModifier: reloaded config for world '{}'.", worldFolderName);
    }

    public static void reloadGlobal() {
        BlockBreakConfig.loadGlobal();
        BlockBreakModifier.LOGGER.info("BlockBreakModifier: reloaded global config.");
    }

    public static String getCurrentWorldName() {
        return currentWorldName;
    }
}
