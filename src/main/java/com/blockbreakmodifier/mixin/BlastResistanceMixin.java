package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlastResistanceMixin {

    @Inject(
            method = "getBlastResistance(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void blockbreakmodifier$overrideBlastResistance(
            BlockState state,
            BlockView world,
            BlockPos pos,
            Explosion explosion,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        BlockBreakConfig.getBlastResistance(blockId).ifPresent(cir::setReturnValue);
    }
}
