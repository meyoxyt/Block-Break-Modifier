package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * CRITICAL: Must use value = BlockBehaviour.BlockStateBase.class (class reference),
 * NOT targets = "..." (string). String-based targets bypass Loom's remapper
 * so Mojang-mapped method names like "getExplosionResistance" never resolve
 * at runtime against the intermediary/obfuscated jar.
 *
 * Using the class reference ensures Loom rewrites the method descriptor to
 * intermediary at compile time, which then resolves correctly at runtime.
 *
 * @Pseudo is required because BlockStateBase is a protected inner class —
 * without it Mixin refuses to apply to non-public inner classes.
 */
@Pseudo
@Mixin(value = BlockBehaviour.BlockStateBase.class, priority = 1100)
public abstract class BlastResistanceMixin {

    @Inject(
            method = "getExplosionResistance",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void bbm$overrideBlastResistance(
            BlockGetter world,
            BlockPos pos,
            Explosion explosion,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!VersionHandlerRegistry.isInitialized()) return;
        BlockState self = (BlockState) (Object) this;
        String blockId = VersionHandlerRegistry.get().getBlockId(self);
        BlockBreakConfig.getBlastResistance(blockId).ifPresent(cir::setReturnValue);
    }
}
