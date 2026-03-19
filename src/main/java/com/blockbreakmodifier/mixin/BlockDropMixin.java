package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects into Block.playerDestroy — the instance method called by the server
 * after a player successfully breaks a block. This is where vanilla triggers
 * the loot table drop sequence.
 *
 * Using value = Block.class (class reference) is essential so Loom remaps
 * "playerDestroy" from Mojang name to the correct intermediary descriptor.
 * Without this remapping, require=0 silently swallows the failed inject.
 *
 * Logic:
 *  - If no BBM config entry exists for this block+tool → return, vanilla runs.
 *  - If droppable=false → cancel, nothing drops.
 *  - If droppable=true → cancel vanilla, manually pop the block item.
 */
@Mixin(value = Block.class, priority = 1100)
public abstract class BlockDropMixin {

    @Inject(
            method = "playerDestroy",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void bbm$overrideDrop(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity,
            ItemStack tool,
            CallbackInfo ci
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        // drops only exist server-side; client-side this is a no-op
        if (!(level instanceof ServerLevel serverLevel)) return;

        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(player);

        Boolean droppable = BlockBreakConfig.getToolDroppable(blockId, toolId);
        if (droppable == null) return; // no config for this pair → vanilla

        ci.cancel(); // suppress vanilla loot table entirely

        if (Boolean.FALSE.equals(droppable)) return; // droppable: false → no drops

        // droppable: true → force-drop the block's own item (like silk touch)
        ItemStack drop = new ItemStack(state.getBlock().asItem());
        if (!drop.isEmpty()) {
            Block.popResource(serverLevel, pos, drop);
        }
    }
}
