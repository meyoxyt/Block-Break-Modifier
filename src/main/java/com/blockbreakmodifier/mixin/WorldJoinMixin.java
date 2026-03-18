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

    /**
     * Called when a world is loaded on the client.
     * We set the world ID from the level's dimension/server info.
     * Uses the level's own string representation as a unique ID per-session.
     */
    @Inject(method = "setLevel", at = @At("HEAD"))
    private void blockbreakmodifier$onWorldJoin(ClientLevel level, CallbackInfo ci) {
        if (level != null) {
            // Use the server's brand / connection info as a stable world ID
            // This is available via the IntegratedServer for singleplayer
            Minecraft mc = Minecraft.getInstance();
            String worldId = null;

            // Singleplayer: get the world from the integrated server
            if (mc.getSingleplayerServer() != null) {
                worldId = mc.getSingleplayerServer().getWorldData().getLevelName();
            }

            // Multiplayer: use the server IP as world ID
            if (worldId == null && mc.getCurrentServer() != null) {
                worldId = mc.getCurrentServer().ip;
            }

            if (worldId != null && !worldId.isBlank()) {
                BlockBreakModifierClient.setCurrentWorldId(worldId);
            }
        }
        BlockBreakModifierClient.onWorldJoin();
    }

    @Inject(
            method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At("HEAD"),
            require = 0
    )
    private void blockbreakmodifier$onWorldLeave(
            net.minecraft.client.gui.screens.Screen screen,
            CallbackInfo ci
    ) {
        BlockBreakModifierClient.onWorldLeave();
    }
}
