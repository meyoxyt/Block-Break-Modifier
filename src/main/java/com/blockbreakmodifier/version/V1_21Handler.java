package com.blockbreakmodifier.version;

/**
 * MC 1.21 — Protocol 767
 * "Tricky Trials" — June 2024
 * Note: 1.21 and 1.21.1 share protocol 767.
 * The registry picks V1_21_1Handler as more specific (same minProtocol, registered later).
 */
public class V1_21Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21"; }
    @Override public int minProtocolVersion()    { return 767; }
    @Override public int maxProtocolVersion()    { return 767; }
}
