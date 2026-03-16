package com.blockbreakmodifier.client;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockBreakModifierClient implements ClientModInitializer {

    private static String currentWorldName = null;

    @Override
    public void onInitializeClient() {
        BlockBreakConfig.loadGlobal();
        BlockBreakModifier.LOGGER.info("BlockBreakModifier client initialized.");
    }

    public static void onWorldJoin(String worldFolderName) {
        currentWorldName = worldFolderName;
        BlockBreakConfig.loadForWorld(worldFolderName);
    }

    public static void onWorldLeave() {
        currentWorldName = null;
        BlockBreakConfig.loadGlobal();
    }

    public static void reloadForWorld(String worldFolderName) {
        BlockBreakConfig.loadForWorld(worldFolderName);
        BlockBreakModifier.LOGGER.info(
                "BlockBreakModifier: in-game reload triggered for world '{}'.", worldFolderName
        );
    }

    public static String getCurrentWorldName() {
        return currentWorldName;
    }
}
