package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Hooks into Block.dropResources — the static method called by the server
 * to actually spawn item drops after a block is broken in 1.21.x.
 *
 * Per-tool droppable logic:
 *   true  -> cancel vanilla drops, pop the block item itself
 *   false -> cancel vanilla drops, nothing spawns
 *   null  -> vanilla loot table runs untouched
 */
@Mixin(value = Block.class, priority = 1100)
public abstract class BlockDropMixin {

    /**
     * Intercept the dropResources overload that receives the breaking tool.
     * Signature: dropResources(BlockState, ServerLevel, BlockPos, BlockEntity, Entity, ItemStack)
     */
    @Inject(
            method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private static void blockbreakmodifier$overrideDrop(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            net.minecraft.world.level.block.entity.BlockEntity blockEntity,
            net.minecraft.world.entity.Entity entity,
            ItemStack tool,
            CallbackInfo ci
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        if (!(entity instanceof Player player)) return;

        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(player);
        Boolean droppable = BlockBreakConfig.getToolDroppable(blockId, toolId);
        if (droppable == null) return; // not configured — let vanilla run

        ci.cancel(); // suppress vanilla loot table drop

        if (!droppable) return; // droppable: false — no drops at all

        // droppable: true — force-drop the block item itself (Silk Touch style)
        Block.popResource(level, pos, new ItemStack(state.getBlock().asItem()));
    }
}
