package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts Player.getDestroySpeed to override mining speed per block+tool.
 * Injects at RETURN and overrides the return value so our speed always
 * takes effect regardless of what vanilla or other mixins computed.
 */
@Mixin(value = Player.class, priority = 1100)
public abstract class MiningSpeedMixin {

    @Inject(
            method = "getDestroySpeed",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void bbm$overrideMiningSpeed(
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
