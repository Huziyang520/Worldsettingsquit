package com.huziyang520.worldsettingsquit.config;

import com.huziyang520.keenlib.config.KeenConfig;
import com.huziyang520.keenlib.config.KeenConfigApi;
import com.huziyang520.keenlib.text.KeenText;
import com.huziyang520.worldsettingsquit.Constants;

public final class WorldSettingsQuitConfig {

    public static final String KEY_ENABLED = "enabled";

    private static final KeenConfig FILE = KeenConfig.create(Constants.MOD_ID);

    private WorldSettingsQuitConfig() {
    }

    public static boolean enabled() {
        return FILE.getBoolean(KEY_ENABLED, true);
    }

    public static void registerScreen() {
        KeenConfigApi.business(Constants.MOD_ID,
                        KeenText.trans("gui.worldsettingsquit.title", "World Settings Quick Exit"))
                .booleanToggle(KEY_ENABLED, true,
                        KeenText.trans("gui.worldsettingsquit.enabled", "Enable quick exit"),
                        KeenText.trans("gui.worldsettingsquit.enabled.tooltip",
                                "When off, ESC in the world options screen behaves like vanilla."))
                .submit();
    }
}
