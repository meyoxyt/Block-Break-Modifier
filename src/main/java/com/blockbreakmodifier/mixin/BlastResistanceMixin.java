package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides explosion/blast resistance per-block as configured.
 *
 * Targets BlockBehaviour.getExplosionResistance which is the actual
 * implementation called by Block in 1.21.x. Block extends BlockBehaviour,
 * so this fires for all blocks including vanilla ones.
 */
@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour", priority = 1100)
public abstract class BlastResistanceMixin {

    @Inject(
            method = "getExplosionResistance(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;)F",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void blockbreakmodifier$overrideBlastResistance(
            BlockState state,
            BlockGetter world,
            BlockPos pos,
            Explosion explosion,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        BlockBreakConfig.getBlastResistance(blockId).ifPresent(cir::setReturnValue);
    }
}
