package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class WorldJoinMixin {

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void blockbreakmodifier$onWorldJoin(ClientLevel level, CallbackInfo ci) {
        BlockBreakModifierClient.onWorldJoin();
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void blockbreakmodifier$onWorldLeave(net.minecraft.client.gui.screens.Screen screen, CallbackInfo ci) {
        BlockBreakModifierClient.onWorldLeave();
    }
}
