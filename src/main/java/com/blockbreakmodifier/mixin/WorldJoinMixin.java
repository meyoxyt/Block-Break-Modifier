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
     * Fired when a world/level is set on the Minecraft client.
     * We extract the level storage name and pass it to the client handler
     * so per-world configs load correctly.
     *
     * For singleplayer: ClientLevel has a levelData name that matches the save folder.
     * For multiplayer: levelData name is the server address — we use that as the world ID
     *                  so each server gets its own config folder.
     */
    @Inject(method = "setLevel", at = @At("HEAD"))
    private void blockbreakmodifier$onWorldJoin(ClientLevel level, CallbackInfo ci) {
        if (level != null) {
            // getLevelData().getLevelName() returns the world folder name in singleplayer
            // and the server address string in multiplayer — both are valid unique IDs
            String worldId = level.getLevelData().getLevelName();
            if (worldId != null && !worldId.isBlank()) {
                BlockBreakModifierClient.setCurrentWorldId(worldId);
            }
        }
        BlockBreakModifierClient.onWorldJoin();
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void blockbreakmodifier$onWorldLeave(net.minecraft.client.gui.screens.Screen screen, CallbackInfo ci) {
        BlockBreakModifierClient.onWorldLeave();
    }
}
