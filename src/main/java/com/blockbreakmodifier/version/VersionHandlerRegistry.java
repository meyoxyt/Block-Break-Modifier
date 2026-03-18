package com.blockbreakmodifier.version;

import com.blockbreakmodifier.BlockBreakModifier;
import net.minecraft.SharedConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Detects the running Minecraft version at mod init and selects
 * the best matching VersionHandler implementation.
 *
 * Version detection uses the game's protocol version integer which is
 * always available via SharedConstants.getGameVersion().getProtocolVersion().
 *
 * Handler selection order:
 *  1. Find all handlers whose [min, max] range covers the current protocol version.
 *  2. Pick the one with the highest minProtocolVersion (most specific match).
 *  3. If none match, fall back to the generic handler.
 */
public class VersionHandlerRegistry {

    private static VersionHandler ACTIVE;

    private static final List<VersionHandler> HANDLERS = new ArrayList<>();

    static {
        // Register all version handlers here — order does not matter
        HANDLERS.add(new V1_21Handler());
        HANDLERS.add(new V1_21_1Handler());
        HANDLERS.add(new V1_21_4Handler());
        HANDLERS.add(new V1_21_11Handler());
    }

    public static void init() {
        int protocol = SharedConstants.getGameVersion().getProtocolVersion();
        String mcVersion = SharedConstants.getGameVersion().getName();

        ACTIVE = HANDLERS.stream()
                .filter(h -> h.supportsVersion(protocol))
                .max(Comparator.comparingInt(VersionHandler::minProtocolVersion))
                .orElseGet(() -> {
                    BlockBreakModifier.LOGGER.warn(
                            "[BBM] No specific handler for MC {} (protocol {}), using generic 1.21.11 handler.",
                            mcVersion, protocol
                    );
                    return new V1_21_11Handler();
                });

        BlockBreakModifier.LOGGER.info(
                "[BBM] Detected MC {} (protocol {}), using handler: {}",
                mcVersion, protocol, ACTIVE.getVersionLabel()
        );
    }

    public static VersionHandler get() {
        if (ACTIVE == null) {
            throw new IllegalStateException("[BBM] VersionHandlerRegistry not initialized! Call init() first.");
        }
        return ACTIVE;
    }

    /** Returns true if a handler was successfully selected for the current version. */
    public static boolean isInitialized() {
        return ACTIVE != null;
    }
}
