package com.blockbreakmodifier.client;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockBreakModifierClient implements ClientModInitializer {

    private static String currentWorldId = null;

    @Override
    public void onInitializeClient() {
        BlockBreakModifier.LOGGER.info("[BBM] Client initialized.");
    }

    public static void onWorldJoin() {
        if (currentWorldId != null) {
            BlockBreakConfig.loadForWorld(currentWorldId);
        }
    }

    public static void onWorldLeave() {
        currentWorldId = null;
        BlockBreakConfig.loadGlobal();
    }

    public static void setCurrentWorldId(String worldId) {
        currentWorldId = worldId;
    }

    public static void reloadForWorld(String worldId) {
        currentWorldId = worldId;
        BlockBreakConfig.loadForWorld(worldId);
        BlockBreakModifier.LOGGER.info("[BBM] Reloaded config for world: {}", worldId);
    }

    public static String getCurrentWorldId() {
        return currentWorldId;
    }
}
