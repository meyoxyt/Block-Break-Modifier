package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MiningSpeedMixin {

    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void blockbreakmodifier$overrideMiningSpeed(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        PlayerEntity self = (PlayerEntity) (Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(self);
        BlockBreakConfig.getToolSpeed(blockId, toolId).ifPresent(cir::setReturnValue);
    }
}
