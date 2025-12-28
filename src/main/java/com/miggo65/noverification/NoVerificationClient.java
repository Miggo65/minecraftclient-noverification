package com.miggo65.noverification;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main client initializer for the No Verification mod.
 * This mod allows connecting to offline servers without authentication.
 */
public class NoVerificationClient implements ClientModInitializer {
    public static final String MOD_ID = "noverification";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("No Verification Client initialized!");
        LOGGER.info("This client can connect to servers with online-mode=false");
    }
}
