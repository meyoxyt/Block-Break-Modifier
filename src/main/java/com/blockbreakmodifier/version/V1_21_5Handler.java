package com.blockbreakmodifier.version;

/**
 * MC 1.21.5 — Protocol 770
 * "Spring to Life" — March 2025
 */
public class V1_21_5Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.5"; }
    @Override public int minProtocolVersion()    { return 770; }
    @Override public int maxProtocolVersion()    { return 770; }
}
