package com.huziyang520.worldsettingsquit.mixin;

import com.huziyang520.keenlib.gui.AutoSaveScreen;
import com.huziyang520.worldsettingsquit.Constants;
import net.minecraft.client.gui.components.PopupScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WorldOptionsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * 目标：26.3 原版世界设置界面（通用/多人游戏）。
 * 行为：按 ESC 等同于点击「应用更改」——先保存再退出；无修改时与原版一样直接返回。
 * 「取消」按钮走 onClose()，不经过本 mixin 的 ESC 分支，仍保持“放弃修改”的原版语义。
 * <p>
 * 实现依据探针（minecraft-patched-26.3.0.1-beta-sources）：
 * Screen.keyPressed 中 ESC → shouldCloseOnEsc() → onClose()；
 * WorldOptionsScreen 未声明 keyPressed，故以“继承 Screen 的 mixin”模式覆盖并以 super 回调原版链路；
 * hasChanges()/applyChanges(IntegratedServer) 为 protected，@Shadow 直调；
 * 难度锁定变更时的确认弹窗（PopupScreen）按原版按钮处理器复刻。
 */
@Mixin(WorldOptionsScreen.class)
public abstract class MixinWorldOptionsScreen extends Screen implements AutoSaveScreen {

    protected MixinWorldOptionsScreen(Component title) {
        super(title);
    }

    @Shadow
    @Final
    private Screen lastScreen;

    @Shadow
    @Final
    private Level level;

    @Shadow
    private Boolean wantedDifficultyLocked;

    @Shadow
    private Boolean initialDifficultyLocked;

    @Shadow
    private Difficulty wantedDifficulty;

    @Shadow
    protected abstract boolean hasChanges();

    @Shadow
    protected abstract void applyChanges(IntegratedServer server);

    @Override
    public boolean keenlib$applyChanges() {
        if (!this.hasChanges()) {
            return true;
        }
        IntegratedServer server = this.minecraft.getSingleplayerServer();
        boolean difficultyLockChanged = this.wantedDifficultyLocked != null
            && this.initialDifficultyLocked != null
            && !this.wantedDifficultyLocked.equals(this.initialDifficultyLocked);
        if (difficultyLockChanged) {
            // 复刻原版「应用更改」按钮的难度锁定确认弹窗；确认后再应用并退出
            Component displayName = this.wantedDifficulty != null
                ? this.wantedDifficulty.getDisplayName()
                : this.level.getDifficulty().getDisplayName();
            this.minecraft.gui.setScreen(new PopupScreen.Builder(this, Component.translatable("difficulty.lock.title"))
                .addMessage(Component.translatable("difficulty.lock.question", displayName))
                .addButton(CommonComponents.GUI_YES, button -> {
                    this.applyChanges(server);
                    this.minecraft.gui.setScreen(this.lastScreen);
                })
                .addButton(CommonComponents.GUI_NO, button -> this.minecraft.gui.setScreen(this))
                .build());
            return false;
        }
        this.applyChanges(server);
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            if (this.keenlib$applyChanges()) {
                this.onClose();
            }
            return true;
        }
        return super.keyPressed(event);
    }
}
