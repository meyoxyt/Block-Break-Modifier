package com.blockbreakmodifier.version;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base implementation using MojangMappings API names.
 * Compiled against 1.21.5 — stable across all 1.21.x at runtime.
 * All version handlers extend this.
 */
public abstract class BaseVersionHandler implements VersionHandler {

    @Override
    public String getBlockId(BlockState state) {
        Block block = state.getBlock();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return id != null ? id.toString() : "minecraft:air";
    }

    @Override
    public String getToolId(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return "minecraft:air";
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null ? id.toString() : "minecraft:air";
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion >= minProtocolVersion()
                && protocolVersion <= maxProtocolVersion();
    }
}
