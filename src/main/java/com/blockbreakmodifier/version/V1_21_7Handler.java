package com.blockbreakmodifier.version;

/**
 * MC 1.21.7 — Protocol 772
 * "Chase the Skies" hotfix
 */
public class V1_21_7Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.7"; }
    @Override public int minProtocolVersion()    { return 772; }
    @Override public int maxProtocolVersion()    { return 772; }
}
