package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldListWidget.WorldEntry.class)
public abstract class WorldListEntryMixin {

    @Shadow @Final private LevelSummary level;
    @Shadow @Final private SelectWorldScreen screen;

    @Unique
    private ButtonWidget blockbreakmodifier$reloadButton;

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void blockbreakmodifier$renderReloadButton(
            DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
            int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci
    ) {
        if (blockbreakmodifier$reloadButton == null) {
            String worldId = level.getName();
            blockbreakmodifier$reloadButton = ButtonWidget.builder(
                    Text.literal("\u21BB BBM"),
                    btn -> {
                        BlockBreakModifierClient.reloadForWorld(worldId);
                        MinecraftClient.getInstance().player.sendMessage(
                                Text.literal("§a[BlockBreakModifier] Config reloaded for world: §e" + worldId), false
                        );
                    }
            ).dimensions(x + entryWidth - 58, y + entryHeight - 18, 56, 16).build();
        } else {
            blockbreakmodifier$reloadButton.setX(x + entryWidth - 58);
            blockbreakmodifier$reloadButton.setY(y + entryHeight - 18);
        }

        if (hovered) {
            blockbreakmodifier$reloadButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockbreakmodifier$handleReloadClick(
            double mouseX, double mouseY, int button, CallbackInfo ci
    ) {
        if (blockbreakmodifier$reloadButton != null && blockbreakmodifier$reloadButton.mouseClicked(mouseX, mouseY, button)) {
            ci.cancel();
        }
    }
}
