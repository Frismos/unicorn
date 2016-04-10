package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;

/**
 * Created by edgar on 11/29/2015.
 */
public class ShootingBoss extends Boss {

    private final Timer soundTimer;

    public ShootingBoss(final GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 1.0f;
        FIRE_CHANCE = 100;
        maxHitPoints = hitPoints = 110;
        showProgressBar();
        gameStage.game.soundManager.playMusic(SoundManager.BOSS_VOICE, Sound.class, true);
        soundTimer = gameStage.game.timerManager.loop(4, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                gameStage.game.soundManager.playMusic(SoundManager.BOSS_VOICE, Sound.class, true);
            }
        });
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.SHOOTING_BOSS;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.SHOOTING_BOSS_SCALE_RATIO;
    }

    @Override
    public void fireEvent() {
        gameStage.game.soundManager.playMusic(SoundManager.BOSS_HIT, Sound.class, true);
        spawnPoint.x = gameStage.unicorn.tile.getX() + gameStage.grid.tileWidth / 2;
        spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        super.fireEvent();
    }

    @Override
    public void die(AnimationState.AnimationStateListener dieListener) {
        gameStage.game.timerManager.removeTimer(soundTimer);
        gameStage.game.soundManager.playMusic(SoundManager.BOSS_TAIL, Sound.class, true);
        super.die(dieListener);
    }
}
