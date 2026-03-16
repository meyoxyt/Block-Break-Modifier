package com.blockbreakmodifier.mixin;

import com.blockbreakmodifier.client.BlockBreakModifierClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
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

    @Shadow
    @Final
    LevelSummary level;

    @Unique
    private ButtonWidget blockbreakmodifier$reloadButton;

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void blockbreakmodifier$renderReloadButton(
            DrawContext context,
            int mouseX,
            int mouseY,
            boolean hovered,
            float deltaTicks,
            CallbackInfo ci
    ) {
        if (!hovered) return;

        int entryX = this.getX();
        int entryY = this.getY();
        int entryWidth = this.getWidth();
        int entryHeight = this.getHeight();

        if (blockbreakmodifier$reloadButton == null) {
            String worldId = level.getName();
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
            ).dimensions(
                    entryX + entryWidth - 60,
                    entryY + entryHeight - 20,
                    58,
                    18
            ).build();
        } else {
            blockbreakmodifier$reloadButton.setX(entryX + entryWidth - 60);
            blockbreakmodifier$reloadButton.setY(entryY + entryHeight - 20);
        }

        blockbreakmodifier$reloadButton.render(context, mouseX, mouseY, deltaTicks);
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockbreakmodifier$handleReloadClick(
            Click click,
            boolean doubled,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (blockbreakmodifier$reloadButton != null
                && blockbreakmodifier$reloadButton.mouseClicked(
                        click.x(), click.y(), click.button()
                )) {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    public abstract int getX();

    @Shadow
    public abstract int getY();

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getHeight();
}
