package com.blockbreakmodifier.command;

import com.blockbreakmodifier.BlockBreakConfig;
import com.blockbreakmodifier.BlockBreakModifier;
import com.blockbreakmodifier.client.BlockBreakModifierClient;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ReloadCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(ReloadCommand::registerCommands);
    }

    private static void registerCommands(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandBuildContext buildContext
    ) {
        dispatcher.register(
            literal("blockbreakmodifier")
                .then(
                    literal("reload")
                        .executes(ctx -> {
                            String worldId = BlockBreakModifierClient.getCurrentWorldId();
                            if (worldId != null && !worldId.isBlank()) {
                                BlockBreakConfig.loadForWorld(worldId);
                                ctx.getSource().sendFeedback(
                                    Component.literal("[BBM] ✅ Config reloaded for world: " + worldId)
                                );
                                BlockBreakModifier.LOGGER.info("[BBM] /blockbreakmodifier reload -> world: {}", worldId);
                            } else {
                                BlockBreakConfig.loadGlobal();
                                ctx.getSource().sendFeedback(
                                    Component.literal("[BBM] ✅ Global config reloaded.")
                                );
                                BlockBreakModifier.LOGGER.info("[BBM] /blockbreakmodifier reload -> global config.");
                            }
                            return 1;
                        })
                )
        );
    }
}
