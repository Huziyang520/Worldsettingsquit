package com.huziyang520.worldsettingsquit.mixin;

import com.huziyang520.keenlib.gui.AutoSaveScreen;
import com.huziyang520.worldsettingsquit.Constants;
import com.huziyang520.worldsettingsquit.config.WorldSettingsQuitConfig;
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
        if (event.isEscape() && WorldSettingsQuitConfig.enabled()) {
            if (this.keenlib$applyChanges()) {
                this.onClose();
            }
            return true;
        }
        return super.keyPressed(event);
    }
}
