package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks Block.playerDestroy — called server-side after a player breaks a block.
 * This method is responsible for triggering loot table drops.
 * We cancel it and handle drops ourselves based on the droppable config.
 *
 * Note: playerDestroy is an instance method on Block (not static), called as
 * block.playerDestroy(level, player, pos, state, blockEntity, tool).
 * The mixin is on Block (non-static), which is correct for instance injection.
 */
@Mixin(value = Block.class, priority = 1100)
public abstract class BlockDropMixin {

    @Inject(
            method = "playerDestroy",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void blockbreakmodifier$overrideDrop(
            net.minecraft.world.level.Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            BlockEntity blockEntity,
            ItemStack tool,
            CallbackInfo ci
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(player);
        Boolean droppable = BlockBreakConfig.getToolDroppable(blockId, toolId);
        if (droppable == null) return; // not configured — vanilla runs

        ci.cancel(); // suppress vanilla loot table

        if (!droppable) return; // droppable: false — no drops

        // droppable: true — force-drop the block item itself
        Block.popResource(serverLevel, pos, new ItemStack(state.getBlock().asItem()));
    }
}
