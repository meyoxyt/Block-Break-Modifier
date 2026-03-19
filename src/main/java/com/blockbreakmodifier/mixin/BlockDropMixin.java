package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When droppable: true is set for a block in the config, this mixin
 * forces a drop of the block itself (as if mined with Silk Touch) regardless
 * of vanilla loot table restrictions (e.g. stone without Silk Touch normally
 * drops cobblestone, not stone; this forces a stone drop).
 *
 * When droppable: false, it suppresses ALL drops for that block.
 * When droppable is not set (null), vanilla drop logic runs untouched.
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
            net.minecraft.world.level.block.entity.BlockEntity blockEntity,
            ItemStack tool,
            CallbackInfo ci
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        Boolean droppable = BlockBreakConfig.getDroppable(blockId);
        if (droppable == null) return; // not configured — let vanilla run

        // Cancel the vanilla playerDestroy (which handles loot table drops)
        ci.cancel();

        if (!droppable) {
            // droppable: false — remove block silently, no drops
            level.removeBlock(pos, false);
            return;
        }

        // droppable: true — remove block and force drop the block item itself
        level.removeBlock(pos, false);
        if (level instanceof ServerLevel serverLevel) {
            Block block = (Block)(Object) this;
            Block.popResource(serverLevel, pos, new ItemStack(block.asItem()));
        }
    }
}
