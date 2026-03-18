package com.blockbreakmodifier.version;

/**
 * Handler for Minecraft 1.21 (protocol 767).
 * API is identical to 1.21.1 for our purposes — same Yarn mappings apply.
 */
public class V1_21Handler extends V1_21_1Handler {

    // 1.21 protocol version = 767
    private static final int PROTOCOL_1_21   = 767;
    private static final int PROTOCOL_1_21_1 = 767; // 1.21 and 1.21.1 share protocol 767

    @Override
    public String getVersionLabel() {
        return "1.21";
    }

    @Override
    public int minProtocolVersion() {
        return PROTOCOL_1_21;
    }

    @Override
    public int maxProtocolVersion() {
        return PROTOCOL_1_21_1;
    }

    @Override
    public boolean supportsVersion(int protocolVersion) {
        return protocolVersion == PROTOCOL_1_21;
    }
}
