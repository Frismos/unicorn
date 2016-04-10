package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;

/**
 * Created by eavanyan on 2/11/16.
 */
public class RunningEnemy extends WalkingEnemy {
    private final static float ACCELERATED_MOVE_SPEED = 25f;
    private static final float ACCELERATION_DURATION = 0.5f;
    private Timer attackRunnable;

    public RunningEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        maxHitPoints = hitPoints = AIManager.RUNNING_ENEMY_HP;
        showProgressBar();
    }

    public void playDieSound() {
        gameStage.game.soundManager.playMusic(SoundManager.RUNNING_DIE, Sound.class, true);
    }

    @Override
    public void attack() {
        super.attack();
        gameStage.game.soundManager.playMusic(SoundManager.TEETH_CHATTER, Sound.class, true, false);
        float destX = getX() - 7.5f;
        skeletonActor.getAnimationState().setAnimation(0, "attack", false);
        skeletonActor.getAnimationState().removeListener(attackListener);
        skeletonActor.getAnimationState().addListener(attackListener);
        Array<Enemy> enemies = gameStage.game.aiManager.getEnemies();
        for(int i = 0; i < enemies.size; i++) {
            if(enemies.get(i).positionY == positionY) {
                if((enemies.get(i).getX() <= destX && enemies.get(i).getRight() >= destX) ||
                        (enemies.get(i).getX() <= destX + getWidth() && enemies.get(i).getRight() >= destX + getWidth())) {
                    destX = enemies.get(i).getX() - getWidth();
                    break;
                }
            }
        }
        if(destX < gameStage.colorsPlatform.getRight()) {
            destX = gameStage.colorsPlatform.getRight();
        }

        addAction(Actions.sequence(Actions.moveTo(destX, getY(), ACCELERATION_DURATION, Interpolation.sineOut), Actions.run(new Runnable() {
            @Override
            public void run() {
                gameStage.layerStage(null);
            }
        })));
    }

    @Override
    public void wallAttackingAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "attack", true);
        super.wallAttackingAnimation();
    }

    @Override
    public void die() {
        clearActions();
        super.die();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.RUNNING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.RUNNING_ENEMY_SCALE_RATIO;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    public void eatWall() {
        gameStage.game.soundManager.playMusic(SoundManager.TEETH_CHATTER, Sound.class, true);
        super.eatWall();
    }
}
