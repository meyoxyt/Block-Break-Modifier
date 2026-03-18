package com.blockbreakmodifier.version;

import com.blockbreakmodifier.BlockBreakModifier;
import net.minecraft.SharedConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Detects the running MC version once at startup and selects
 * the best-matching VersionHandler.
 *
 * Selection logic:
 *  - Find all handlers whose [min, max] range covers the current protocol.
 *  - Pick the one with the highest minProtocolVersion (most specific).
 *  - If none match, fall back to V1_21_11Handler (open-ended latest).
 *
 * Full coverage:
 *  1.21     → protocol 767
 *  1.21.1   → protocol 767
 *  1.21.2   → protocol 768
 *  1.21.3   → protocol 768
 *  1.21.4   → protocol 769
 *  1.21.5   → protocol 770
 *  1.21.6   → protocol 771  (Chase the Skies)
 *  1.21.7   → protocol 772  (Chase the Skies hotfix)
 *  1.21.8   → protocol 773  (Chase the Skies hotfix 2)
 *  1.21.9   → protocol 774  (Mounts of Mayhem)
 *  1.21.10  → protocol 775  (Mounts of Mayhem hotfix)
 *  1.21.11+ → protocol 776+ (catch-all, future-proof)
 */
public class VersionHandlerRegistry {

    private static VersionHandler ACTIVE;

    private static final List<VersionHandler> HANDLERS = new ArrayList<>();

    static {
        HANDLERS.add(new V1_21Handler());
        HANDLERS.add(new V1_21_1Handler());
        HANDLERS.add(new V1_21_2Handler());
        HANDLERS.add(new V1_21_4Handler());
        HANDLERS.add(new V1_21_5Handler());
        HANDLERS.add(new V1_21_6Handler());
        HANDLERS.add(new V1_21_7Handler());
        HANDLERS.add(new V1_21_8Handler());
        HANDLERS.add(new V1_21_9Handler());
        HANDLERS.add(new V1_21_10Handler());
        HANDLERS.add(new V1_21_11Handler()); // catch-all, must be last
    }

    public static void init() {
        int protocol = SharedConstants.getCurrentVersion().getProtocolVersion();
        String mcVersion = SharedConstants.getCurrentVersion().getName();

        ACTIVE = HANDLERS.stream()
                .filter(h -> h.supportsVersion(protocol))
                .max(Comparator.comparingInt(VersionHandler::minProtocolVersion))
                .orElseGet(() -> {
                    BlockBreakModifier.LOGGER.warn(
                            "[BBM] No specific handler for MC {} (protocol {}). Using 1.21.11+ handler.",
                            mcVersion, protocol
                    );
                    return new V1_21_11Handler();
                });

        BlockBreakModifier.LOGGER.info(
                "[BBM] MC {} (protocol {}) \u2192 using handler: {}",
                mcVersion, protocol, ACTIVE.getVersionLabel()
        );
    }

    public static VersionHandler get() {
        if (ACTIVE == null)
            throw new IllegalStateException("[BBM] VersionHandlerRegistry.init() was not called!");
        return ACTIVE;
    }

    public static boolean isInitialized() {
        return ACTIVE != null;
    }
}
