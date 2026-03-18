package com.blockbreakmodifier.version;

/**
 * Handler for Minecraft 1.21.4 (protocol 769).
 * API is identical to 1.21.1 for our use-case (same Yarn method names).
 * Extend and override if any method signatures change in future mappings.
 */
public class V1_21_4Handler extends V1_21_1Handler {

    // 1.21.4 protocol = 769
    private static final int PROTOCOL_MIN = 769;
    private static final int PROTOCOL_MAX = 769;

    @Override
    public String getVersionLabel() {
        return "1.21.4";
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion >= PROTOCOL_MIN && protocolVersion <= PROTOCOL_MAX;
    }

    @Override
    public int minProtocolVersion() {
        return PROTOCOL_MIN;
    }

    @Override
    public int maxProtocolVersion() {
        return PROTOCOL_MAX;
    }
}
