package com.blockbreakmodifier.version;

import com.blockbreakmodifier.BlockBreakModifier;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Detects the running MC version at startup using FabricLoader (no MC internals).
 * Selects the best-matching VersionHandler by parsing the MC version string.
 *
 * Version -> protocol mapping (used internally for handler selection):
 *  1.21 / 1.21.1  -> protocol 767
 *  1.21.2 / 1.21.3 -> protocol 768
 *  1.21.4         -> protocol 769
 *  1.21.5         -> protocol 770
 *  1.21.6         -> protocol 771
 *  1.21.7         -> protocol 772
 *  1.21.8         -> protocol 773
 *  1.21.9         -> protocol 774
 *  1.21.10        -> protocol 775
 *  1.21.11+       -> protocol 776+
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

    /**
     * Parses the Minecraft version string from FabricLoader and maps it to a
     * synthetic protocol integer for handler selection.
     * Never touches MC internals — safe across all 1.21.x versions.
     */
    private static int detectProtocol(String mcVersion) {
        // Strip any trailing suffix like "-pre1", "-rc1", etc.
        String clean = mcVersion.split("-")[0];
        String[] parts = clean.split("\\.");
        try {
            int major = parts.length > 0 ? Integer.parseInt(parts[0]) : 1;   // 1
            int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 21;  // 21
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;   // 0

            if (major != 1 || minor != 21) {
                // Unknown future version — use max known
                return 776;
            }

            return switch (patch) {
                case 0, 1 -> 767;
                case 2, 3  -> 768;
                case 4     -> 769;
                case 5     -> 770;
                case 6     -> 771;
                case 7     -> 772;
                case 8     -> 773;
                case 9     -> 774;
                case 10    -> 775;
                default    -> 776; // 1.21.11 and beyond
            };
        } catch (NumberFormatException e) {
            return 776;
        }
    }

    public static void init() {
        String mcVersion = FabricLoader.getInstance()
                .getModContainer("minecraft")
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("1.21.11");

        int protocol = detectProtocol(mcVersion);

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
                "[BBM] MC {} (protocol {}) -> using handler: {}",
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
