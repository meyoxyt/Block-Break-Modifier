package com.blockbreakmodifier.version;

/**
 * MC 1.21.10 — Protocol 775
 * "Mounts of Mayhem" hotfix
 */
public class V1_21_10Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.10"; }
    @Override public int minProtocolVersion()    { return 775; }
    @Override public int maxProtocolVersion()    { return 775; }
}
