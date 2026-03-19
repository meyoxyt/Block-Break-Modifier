package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides blast resistance by targeting BlockState.getExplosionResistance.
 *
 * In 1.21.x the Explosion class calls blockState.getExplosionResistance(...)
 * on every block it checks. Targeting BlockState directly is the most reliable
 * approach — it works regardless of which Block subclass is involved and
 * avoids the intermediate descriptor issues with BlockBehaviour.
 */
@Mixin(value = BlockState.class, priority = 1100)
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
        BlockState self = (BlockState)(Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(self);
        BlockBreakConfig.getBlastResistance(blockId).ifPresent(cir::setReturnValue);
    }
}
