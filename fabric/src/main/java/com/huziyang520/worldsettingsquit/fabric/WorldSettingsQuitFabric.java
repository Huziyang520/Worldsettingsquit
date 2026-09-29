package com.huziyang520.worldsettingsquit.fabric;

import com.huziyang520.worldsettingsquit.Constants;
import net.fabricmc.api.ClientModInitializer;

public class WorldSettingsQuitFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        Constants.LOG.info("World Settings Quick Exit loaded: ESC in the world options screen applies changes and exits.");
    }
}
