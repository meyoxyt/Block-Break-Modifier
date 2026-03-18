package com.blockbreakmodifier.version;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Handler for Minecraft 1.21.1 (protocol 767).
 * Covers 1.21 and 1.21.1 which are API-identical.
 *
 * This is the BASE implementation all other handlers extend.
 * Override only what changes between versions.
 */
public class V1_21_1Handler implements VersionHandler {

    // 1.21 / 1.21.1 share protocol 767
    private static final int PROTOCOL_MIN = 767;
    private static final int PROTOCOL_MAX = 767;

    @Override
    public String getVersionLabel() {
        return "1.21.1";
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion >= PROTOCOL_MIN && protocolVersion <= PROTOCOL_MAX;
    }

    @Override
    public int minProtocolVersion() {
        return PROTOCOL_MIN;
    }

    @Override
    public int maxProtocolVersion() {
        return PROTOCOL_MAX;
    }

    @Override
    public String getBlockId(BlockState state) {
        Block block = state.getBlock();
        Identifier id = Registries.BLOCK.getId(block);
        return id.toString();
    }

    @Override
    public String getToolId(PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return "minecraft:air";
        Identifier id = Registries.ITEM.getId(stack.getItem());
        return id.toString();
    }
}
