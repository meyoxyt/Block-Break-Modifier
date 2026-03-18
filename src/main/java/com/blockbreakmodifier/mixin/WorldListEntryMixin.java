package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(WorldListWidget.WorldEntry.class)
public abstract class WorldListEntryMixin {

    @Shadow @Final LevelSummary level;

    @Unique
    private ButtonWidget blockbreakmodifier$reloadButton;

    @Inject(method = "render", at = @At("TAIL"))
    private void blockbreakmodifier$renderReloadButton(
            DrawContext context,
            int index,
            int y,
            int x,
            int entryWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!hovered) return;
        String worldId = level.getName();
        if (blockbreakmodifier$reloadButton == null) {
            blockbreakmodifier$reloadButton = ButtonWidget.builder(
                    Text.literal("\u21BB BBM"),
                    btn -> {
                        BlockBreakModifierClient.reloadForWorld(worldId);
                        MinecraftClient mc = MinecraftClient.getInstance();
                        if (mc.player != null) {
                            mc.player.sendMessage(
                                    Text.literal("§a[BBM] §7Reloaded config for world: §e" + worldId),
                                    false
                            );
                        }
                    }
            ).dimensions(x + entryWidth - 62, y + entryHeight - 20, 60, 18).build();
        } else {
            blockbreakmodifier$reloadButton.setX(x + entryWidth - 62);
            blockbreakmodifier$reloadButton.setY(y + entryHeight - 20);
        }
        blockbreakmodifier$reloadButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void blockbreakmodifier$handleReloadClick(
            double mouseX,
            double mouseY,
            int button,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (blockbreakmodifier$reloadButton != null
                && blockbreakmodifier$reloadButton.mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }
}
