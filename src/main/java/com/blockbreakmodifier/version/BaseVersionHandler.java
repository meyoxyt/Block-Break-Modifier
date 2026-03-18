package com.blockbreakmodifier.version;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base implementation using MojangMappings API names.
 * These names are STABLE across all 1.21.x versions — Mojang does not
 * rename methods between minor versions in the same era.
 *
 * All version handlers extend this. Override a method only if Mojang
 * actually changed the API between versions.
 */
public abstract class BaseVersionHandler implements VersionHandler {

    @Override
    public String getBlockId(BlockState state) {
        Block block = state.getBlock();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return id.toString();
    }

    @Override
    public String getToolId(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return "minecraft:air";
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.toString();
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion >= minProtocolVersion()
                && protocolVersion <= maxProtocolVersion();
    }
}
