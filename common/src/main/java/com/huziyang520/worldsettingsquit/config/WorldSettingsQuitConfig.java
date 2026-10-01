package com.huziyang520.worldsettingsquit.config;

import com.huziyang520.keenlib.config.KeenConfig;
import com.huziyang520.keenlib.config.KeenConfigApi;
import com.huziyang520.keenlib.text.KeenText;
import com.huziyang520.worldsettingsquit.Constants;

/**
 * 本模组的配置文件（{@code config/worldsettingsquit.json}）。
 *
 * <p>目前只有一个选项：**功能是否启用**。该选项在 **KeenLib 的 Cloth Config 配置界面**里开关
 * （KeenLib → 纯客户端业务模组 → World Settings Quick Exit）。
 */
public final class WorldSettingsQuitConfig {

    /** 配置键：功能是否启用。 */
    public static final String KEY_ENABLED = "enabled";

    private static final KeenConfig FILE = KeenConfig.create(Constants.MOD_ID);

    private WorldSettingsQuitConfig() {
    }

    /** 功能是否启用（默认启用）。 */
    public static boolean enabled() {
        return FILE.getBoolean(KEY_ENABLED, true);
    }

    /**
     * 把本模组的配置注册进 KeenLib 的配置界面。只在客户端调用；
     * 未安装 Cloth Config 时调用同样安全（只是没有界面）。
     */
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
