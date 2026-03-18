package com.blockbreakmodifier.version;

/**
 * MC 1.21 — Protocol 767
 * 1.21 and 1.21.1 share the same protocol version.
 */
public class V1_21Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21"; }
    @Override public int minProtocolVersion()    { return 767; }
    @Override public int maxProtocolVersion()    { return 767; }
}
