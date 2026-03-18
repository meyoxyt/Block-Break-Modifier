package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MiningSpeedMixin {

    @Inject(
            method = "getDestroySpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void blockbreakmodifier$overrideMiningSpeed(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        Player self = (Player) (Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(self);
        BlockBreakConfig.getToolSpeed(blockId, toolId).ifPresent(cir::setReturnValue);
    }
}
