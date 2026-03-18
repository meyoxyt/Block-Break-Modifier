package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
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
@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin {

    @Shadow @Final private LevelSummary summary;

    @Unique
    private Button blockbreakmodifier$reloadButton;

    @Inject(method = "render", at = @At("TAIL"))
    private void blockbreakmodifier$renderReloadButton(
            GuiGraphics graphics,
            int index,
            int top,
            int left,
            int width,
            int height,
            int mouseX,
            int mouseY,
            boolean hovered,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!hovered) return;
        String worldId = summary.getLevelId();
        if (blockbreakmodifier$reloadButton == null) {
            blockbreakmodifier$reloadButton = Button.builder(
                    Component.literal("\u21BB BBM"),
                    btn -> {
                        BlockBreakModifierClient.reloadForWorld(worldId);
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.player != null) {
                            mc.player.sendSystemMessage(
                                    Component.literal("§a[BBM] §7Reloaded config for world: §e" + worldId)
                            );
                        }
                    }
            ).bounds(left + width - 62, top + height - 20, 60, 18).build();
        } else {
            blockbreakmodifier$reloadButton.setX(left + width - 62);
            blockbreakmodifier$reloadButton.setY(top + height - 20);
        }
        blockbreakmodifier$reloadButton.render(graphics, mouseX, mouseY, partialTick);
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
