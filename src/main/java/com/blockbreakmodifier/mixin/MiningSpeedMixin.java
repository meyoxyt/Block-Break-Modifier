package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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
        PlayerEntity self = (PlayerEntity) (Object) this;
        Identifier blockId = Registries.BLOCK.getId(state.getBlock());
        String blockKey = blockId.toString();

        if (!BlockBreakConfig.hasBlockOverride(blockKey)) return;

        ItemStack held = self.getMainHandStack();
        Identifier toolId = Registries.ITEM.getId(held.getItem());
        String toolKey = toolId.toString();

        BlockBreakConfig.getToolSpeed(blockKey, toolKey).ifPresent(cir::setReturnValue);
    }
}
