package com.blockbreakmodifier;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockBreakModifier implements ModInitializer {

    public static final String MOD_ID = "blockbreakmodifier";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("BlockBreakModifier initialized.");
    }
}
