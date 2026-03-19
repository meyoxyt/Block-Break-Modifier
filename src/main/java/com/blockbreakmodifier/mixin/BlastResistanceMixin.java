package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Targets BlockBehaviour.BlockStateBase — the actual non-final superclass
 * of all BlockState objects in 1.21.x. BlockState itself is a generated
 * final class and cannot be mixined directly.
 *
 * BlockStateBase.getExplosionResistance() is the method called by
 * Explosion when evaluating whether each block gets destroyed.
 */
@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase", priority = 1100)
public abstract class BlastResistanceMixin {

    @Inject(
            method = "getExplosionResistance",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void blockbreakmodifier$overrideBlastResistance(
            BlockGetter world,
            BlockPos pos,
            Explosion explosion,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        // "this" is a BlockStateBase which IS a BlockState at runtime
        BlockState self = (BlockState) (Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(self);
        BlockBreakConfig.getBlastResistance(blockId).ifPresent(cir::setReturnValue);
    }
}
