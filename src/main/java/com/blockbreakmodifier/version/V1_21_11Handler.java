package com.blockbreakmodifier.version;

/**
 * MC 1.21.11+ — Protocol 776+
 * Open-ended catch-all: handles 1.21.11 and any future 1.21.x release.
 * Since MojangMappings method names don't change within an era,
 * this handler will work correctly for unknown future versions too.
 */
public class V1_21_11Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.11+"; }
    @Override public int minProtocolVersion()    { return 776; }
    @Override public int maxProtocolVersion()    { return Integer.MAX_VALUE; }
}
