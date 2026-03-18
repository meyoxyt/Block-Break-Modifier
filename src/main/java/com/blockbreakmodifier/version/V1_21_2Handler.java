package com.blockbreakmodifier.version;

/**
 * MC 1.21.2 / 1.21.3 — Protocol 768
 * "Bundles of Bravery" — October 2024
 * Both versions share protocol 768 — one handler covers both.
 */
public class V1_21_2Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.2-1.21.3"; }
    @Override public int minProtocolVersion()    { return 768; }
    @Override public int maxProtocolVersion()    { return 768; }
}
