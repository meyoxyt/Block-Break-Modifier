package com.blockbreakmodifier.version;

/**
 * MC 1.21.1 — Protocol 767
 * Identical API to 1.21. Separate class for clarity and future-proofing.
 */
public class V1_21_1Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.1"; }
    @Override public int minProtocolVersion()    { return 767; }
    @Override public int maxProtocolVersion()    { return 767; }
}
