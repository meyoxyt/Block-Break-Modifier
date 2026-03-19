package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 1100)
public abstract class MiningSpeedMixin {

    /**
     * Intercept getDestroySpeed and override it when a config entry exists.
     *
     * We inject at HEAD and call setReturnValue so we short-circuit vanilla
     * entirely — this avoids the RETURN injection racing with other mixins and
     * guarantees our value actually takes effect on 1.21.x Fabric.
     */
    @Inject(
            method = "getDestroySpeed",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void blockbreakmodifier$overrideMiningSpeed(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        Player self = (Player) (Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(state);
        String toolId  = VersionHandlerRegistry.get().getToolId(self);
        BlockBreakConfig.getToolSpeed(blockId, toolId).ifPresent(speed -> {
            // Override the return value unconditionally so the speed always applies.
            cir.setReturnValue(speed);
        });
    }
}
