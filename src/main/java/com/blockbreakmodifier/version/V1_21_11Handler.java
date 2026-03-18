package com.blockbreakmodifier.version;

/**
 * Handler for Minecraft 1.21.11+ (protocol 771+).
 * This is the catch-all for all versions 1.21.11 and above.
 * As the "latest" handler it has an open-ended max protocol version.
 */
public class V1_21_11Handler extends V1_21_1Handler {

    // 1.21.11 protocol = 771
    private static final int PROTOCOL_MIN = 771;

    @Override
    public String getVersionLabel() {
        return "1.21.11+";
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion >= PROTOCOL_MIN;
    }

    @Override
    public int minProtocolVersion() {
        return PROTOCOL_MIN;
    }

    @Override
    public int maxProtocolVersion() {
        return Integer.MAX_VALUE;
    }
}
