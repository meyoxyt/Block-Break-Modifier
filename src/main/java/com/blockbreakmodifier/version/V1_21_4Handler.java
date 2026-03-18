package com.blockbreakmodifier.version;

/**
 * MC 1.21.4 — Protocol 769
 */
public class V1_21_4Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.4"; }
    @Override public int minProtocolVersion()    { return 769; }
    @Override public int maxProtocolVersion()    { return 769; }
}
