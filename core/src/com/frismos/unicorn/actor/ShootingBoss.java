package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;
import com.frismos.unicorn.util.Utils;

/**
 * Created by edgar on 11/29/2015.
 */
public class ShootingBoss extends Boss {

    private final Timer soundTimer;
    private final Timer colorChangeTimer;

    public ShootingBoss(final GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 1.0f;
        FIRE_CHANCE = 100;
        maxHitPoints = hitPoints = 110;

        skeletonActor.getAnimationState().getData().setMix("hit", "attack", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("attack", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("hit", "walk", 0.1f);

        showProgressBar();
        gameStage.game.soundManager.playMusic(SoundManager.BOSS_VOICE, Sound.class, true);
        soundTimer = gameStage.game.timerManager.loop(4, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                gameStage.game.soundManager.playMusic(SoundManager.BOSS_VOICE, Sound.class, true);
            }
        });
        colorChangeTimer = gameStage.game.timerManager.loop(3, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                ColorType ct = ColorType.getRandomColor();
                while (ct.equals(ShootingBoss.this.colorType)) {
                    ct = ColorType.getRandomColor();
                }
                final ColorType colorType = ct;
                final Timer timer1 = gameStage.game.timerManager.loop(0.3f, new TimerRunnable() {
                    @Override
                    public void run(Timer timer) {
                        Utils.colorActor(ShootingBoss.this, Utils.colorMap.get(colorType));
                        addAction(Actions.sequence(Actions.delay(0.06f), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                Utils.colorActor(ShootingBoss.this, getColor());
                            }
                        })));
                    }
                });
                gameStage.game.timerManager.run(1.2f, new TimerRunnable() {
                    @Override
                    public void run(Timer timer) {
                        setColorType(colorType);
                        gameStage.game.timerManager.removeTimer(timer1);
                    }
                });
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
        gameStage.game.timerManager.removeTimer(colorChangeTimer);
        gameStage.game.soundManager.playMusic(SoundManager.BOSS_TAIL, Sound.class, true);
        super.die(dieListener);
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        super.hit(damage, bullet);
        if(isAttacking && !isAttackAnimationPlaying) {
            skeletonActor.getAnimationState().setAnimation(0, "hit", false);
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().addListener(hitAnimationStateListener);
        }
    }
}
