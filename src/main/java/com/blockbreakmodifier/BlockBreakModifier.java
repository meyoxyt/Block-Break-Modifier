package com.blockbreakmodifier;

import com.blockbreakmodifier.command.ReloadCommand;
import com.blockbreakmodifier.version.VersionHandlerRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockBreakModifier implements ModInitializer {

    public static final String MOD_ID = "blockbreakmodifier";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        VersionHandlerRegistry.init();
        BlockBreakConfig.loadGlobal();
        ReloadCommand.register();
        LOGGER.info("[BBM] BlockBreakModifier initialized.");
    }
}
