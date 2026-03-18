package com.blockbreakmodifier.version;

/**
 * MC 1.21.1 — Protocol 767
 * "Tricky Trials" hotfix — August 2024
 * Shares protocol 767 with 1.21. Separate class for clarity and future-proofing.
 * The registry selects this one over V1_21Handler since it is registered later
 * and both have equal minProtocol — max() on a stable comparator picks the last equal.
 */
public class V1_21_1Handler extends BaseVersionHandler {

    @Override public String getVersionLabel()    { return "1.21.1"; }
    @Override public int minProtocolVersion()    { return 767; }
    @Override public int maxProtocolVersion()    { return 767; }
}
