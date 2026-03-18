package com.blockbreakmodifier.version;

/**
 * MC 1.21.8 — Protocol 773
 * "Chase the Skies" hotfix 2
 */
public class V1_21_8Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.8"; }
    @Override public int minProtocolVersion()    { return 773; }
    @Override public int maxProtocolVersion()    { return 773; }
}
