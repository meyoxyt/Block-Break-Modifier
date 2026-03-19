package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {

    protected SelectWorldScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void blockbreakmodifier$addReloadButton(CallbackInfo ci) {
        int btnWidth  = 110;
        int btnHeight = 20;
        int btnX = this.width - btnWidth - 4;
        int btnY = 4;

        this.addRenderableWidget(
            Button.builder(
                Component.literal("\u21ba BBM Reload"),
                btn -> {
                    String worldId = BlockBreakModifierClient.getCurrentWorldId();
                    if (worldId != null && !worldId.isBlank()) {
                        BlockBreakConfig.loadForWorld(worldId);
                        BlockBreakModifier.LOGGER.info("[BBM] Reloaded config for world: {}", worldId);
                    } else {
                        BlockBreakConfig.loadGlobal();
                        BlockBreakModifier.LOGGER.info("[BBM] Reloaded global config (no active world).");
                    }
                }
            )
            .bounds(btnX, btnY, btnWidth, btnHeight)
            .build()
        );
    }
}
