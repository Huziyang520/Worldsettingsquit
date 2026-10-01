package com.huziyang520.worldsettingsquit.neoforge;

import com.huziyang520.worldsettingsquit.Constants;
import com.huziyang520.worldsettingsquit.config.WorldSettingsQuitConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class WorldSettingsQuitNeoForge {

    public WorldSettingsQuitNeoForge(IEventBus eventBus) {

        Constants.LOG.info("World Settings Quick Exit loaded: ESC in the world options screen applies changes and exits.");
        WorldSettingsQuitConfig.registerScreen();
    }
}
