package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class WorldJoinMixin {

    @Shadow
    public abstract boolean isIntegratedServerRunning();

    @Inject(
            method = "joinWorld",
            at = @At("HEAD")
    )
    private void blockbreakmodifier$onJoinWorld(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.isIntegratedServerRunning() && client.getServer() != null) {
            String worldName = client.getServer().getSaveProperties().getLevelName();
            BlockBreakModifierClient.onWorldJoin(worldName);
        }
    }

    @Inject(
            method = "disconnect()V",
            at = @At("HEAD")
    )
    private void blockbreakmodifier$onDisconnect(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.isIntegratedServerRunning()) {
            BlockBreakModifierClient.onWorldLeave();
        }
    }
}
