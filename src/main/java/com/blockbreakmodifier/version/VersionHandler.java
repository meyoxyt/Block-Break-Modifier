package com.blockbreakmodifier.version;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Version-agnostic API surface.
 * Each supported MC version range has its own implementation.
 * All version-sensitive logic is routed through here.
 */
public interface VersionHandler {

    /** Human-readable version label e.g. "1.21", "1.21.1", "1.21.4", "1.21.11" */
    String getVersionLabel();

    /**
     * Returns the block identifier string for a given BlockState.
     * e.g. "minecraft:stone"
     */
    default String getBlockId(BlockState state) {
        Block block = state.getBlock();
        Identifier id = Registries.BLOCK.getId(block);
        return id.toString();
    }

    /**
     * Returns the tool identifier string for the player's main-hand item.
     * Returns "minecraft:air" if hand is empty.
     */
    default String getToolId(PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return "minecraft:air";
        Identifier id = Registries.ITEM.getId(stack.getItem());
        return id.toString();
    }

    /**
     * Returns true if this handler supports the given Minecraft protocol version.
     * Protocol version is obtained from SharedConstants.
     */
    boolean supportsVersion(int protocolVersion);

    /**
     * Returns the minimum protocol version this handler supports (inclusive).
     */
    int minProtocolVersion();

    /**
     * Returns the maximum protocol version this handler supports (inclusive).
     * Use Integer.MAX_VALUE for open-ended (latest+).
     */
    int maxProtocolVersion();
}
