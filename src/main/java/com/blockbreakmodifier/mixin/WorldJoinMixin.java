package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class WorldJoinMixin {

    @Inject(
            method = "joinWorld",
            at = @At("HEAD")
    )
    private void blockbreakmodifier$onJoinWorld(ClientWorld world, CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.isIntegratedServerRunning() && client.getServer() != null) {
            String worldName = client.getServer().getSaveProperties().getLevelName();
            BlockBreakModifierClient.onWorldJoin(worldName);
        }
    }

    @Inject(
            method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V",
            at = @At("HEAD")
    )
    private void blockbreakmodifier$onDisconnect(
            net.minecraft.client.gui.screen.Screen screen,
            boolean transferring,
            CallbackInfo ci
    ) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.isIntegratedServerRunning()) {
            BlockBreakModifierClient.onWorldLeave();
        }
    }
}
