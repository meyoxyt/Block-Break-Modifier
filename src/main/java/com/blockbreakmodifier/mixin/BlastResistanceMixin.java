package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlastResistanceMixin {

    @Inject(
            method = "getBlastResistance",
            at = @At("RETURN"),
            cancellable = true
    )
    private void blockbreakmodifier$overrideBlastResistance(
            CallbackInfoReturnable<Float> cir
    ) {
        AbstractBlock.AbstractBlockState self = (AbstractBlock.AbstractBlockState) (Object) this;
        Block block = self.getBlock();
        Identifier blockId = Registries.BLOCK.getId(block);
        BlockBreakConfig.getBlastResistance(blockId.toString()).ifPresent(cir::setReturnValue);
    }
}
