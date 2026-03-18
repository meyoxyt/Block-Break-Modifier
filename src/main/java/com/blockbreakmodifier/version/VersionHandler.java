package com.blockbreakmodifier.version;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Version-agnostic API surface for BlockBreakModifier.
 *
 * Each supported MC version group has its own implementation.
 * Mixins and core logic NEVER import version-specific classes directly —
 * they always go through VersionHandlerRegistry.get().
 *
 * Adding support for a new version:
 *  1. Create a new class extending the nearest base handler
 *  2. Override only what changed
 *  3. Register it in VersionHandlerRegistry
 */
public interface VersionHandler {

    /** Human-readable label, e.g. "1.21", "1.21.4", "1.21.11+" */
    String getVersionLabel();

    /**
     * Returns the block's registry ID string, e.g. "minecraft:obsidian".
     * Uses MojangMappings method names which are stable across 1.21.x.
     */
    String getBlockId(BlockState state);

    /**
     * Returns the player's main-hand item registry ID, e.g. "minecraft:diamond_pickaxe".
     * Returns "minecraft:air" when hand is empty.
     */
    String getToolId(Player player);

    /** Returns true if this handler handles the given protocol version. */
    boolean supportsVersion(int protocolVersion);

    /** Lowest protocol version this handler covers (inclusive). */
    int minProtocolVersion();

    /**
     * Highest protocol version this handler covers (inclusive).
     * Use Integer.MAX_VALUE for open-ended (catch-all for future versions).
     */
    int maxProtocolVersion();
}
