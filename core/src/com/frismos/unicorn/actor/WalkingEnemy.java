package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;

/**
 * Created by edgar on 12/14/2015.
 */
public class WalkingEnemy extends Enemy {

    private Timer attackTimer;

    protected AnimationState.AnimationStateListener attackListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {

        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().setAnimation(0, "walk", true);
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public WalkingEnemy(GameStage stage, ColorType colorType) {
        this(stage, colorType, false);
    }

    public WalkingEnemy(GameStage stage, ColorType colorType, boolean isTutorial) {
        super(stage, colorType, isTutorial);
        maxHitPoints = hitPoints = AIManager.WALKING_ENEMY_HP;
        showProgressBar();
        TIME_STEP = MathUtils.random(1.3f, 2.5f);
        attackTimer = gameStage.game.timerManager.loop(TIME_STEP, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                if(isAttacking && !isEating && !isWaiting && getX() > gameStage.colorsPlatform.getRight() + gameStage.grid.tileWidth / 2 + 7.5f) {
                    attack();
                }
            }
        });
    }

    @Override
    public void die() {
        if(attackTimer != null) {
            gameStage.game.timerManager.removeTimer(attackTimer);
        }
        super.die();
    }

    @Override
    public void wallAttackingAnimation() {
        if(!skeletonActor.getAnimationState().getCurrent(0).getAnimation().getName().contains("attack")) {
            if (MathUtils.randomBoolean()) {
                if (skeletonActor.getAnimationState().getData().getSkeletonData().findAnimation("attack1") != null) {
                    skeletonActor.getAnimationState().setAnimation(0, "attack1", true);
                } else {
                    skeletonActor.getAnimationState().setAnimation(0, "attack", true);
                }
            } else {
                skeletonActor.getAnimationState().setAnimation(0, "attack", true);
            }
        }
    }

    @Override
    protected void startDefaultAnimation() {
        if (MathUtils.randomBoolean()) {
            skeletonActor.getAnimationState().setAnimation(0, "walk1", true);
        } else {
            skeletonActor.getAnimationState().setAnimation(0, "walk", true);
        }
    }

    @Override
    public void attack() {

    }

    @Override
    protected void setResourcesPath() {
        path = Strings.WALKING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.WALKING_ENEMY_SCALE_RATIO;
    }
}
