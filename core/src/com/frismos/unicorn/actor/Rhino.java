package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgaravanyan on 1/26/16.
 */
public class Rhino extends MainCharacter {

    public static final float ABILITY_TIME = 2;

    public boolean isAbilityMode = false;
    private int bulletsIndex = 0;
    private int bulletsCount;

    private AnimationState.AnimationStateListener hitAnimationStateListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().removeListener(this);
            skeletonActor.getAnimationState().setAnimation(0, "idle", true);
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public Rhino(GameStage stage, UnicornType unicornType) {
        super(stage, unicornType);

        for (int i = 0; i < 10; i++) {
            gameBullets.add(new BezierBullet(stage));
        }
    }

    @Override
    protected void setResourcesPath() {
        this.path = Strings.RHINO;
    }

    @Override
    protected void setScaleRatio() {
        this.scaleRatio = Constants.RHINO_SCALE_RATIO;
        //0.075f;
    }

    @Override
    public void hit(float damage) {
        super.hit(damage);
        skeletonActor.getAnimationState().setAnimation(0, "hit", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(hitAnimationStateListener);
    }


    public void playFireAnimation(float x, float y) {
//        if(isFiring) {
//            gameStage.fireBullet(x, y);
//            touchX = Float.MIN_VALUE;
//        }
//        if(!isFiring) {
//            isFiring = true;
        if(isAbilityMode) {
            Debug.Log("bullets index = " + bulletsIndex);
            Debug.Log("bullets count = " + bulletsCount);
            bulletsIndex++;
            if(bulletsIndex >= bulletsCount) {
                bulletsIndex = 0;
                attackSpeed = CANNON_ATTACK_SPEED;
                gameStage.game.timerManager.run(CANNON_ATTACK_SPEED, new Runnable() {
                    @Override
                    public void run() {
                        if(isAbilityMode) {
                            bulletsCount = MathUtils.random(2, 3);
                            attackSpeed = CANNON_ATTACK_SPEED * 0.1f;
                        }
                    }
                });
            }
        }
        this.touchX = x;
        this.touchY = y;
        skeletonActor.getAnimationState().setAnimation(0, "fire", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(fireAnimationListener);
//        }
    }

    @Override
    public void useAbility() {
        if(isAbilityMode) {
            gameStage.game.timerManager.removeTimer(ABILITY_TIME);
            gameStage.game.timerManager.run(ABILITY_TIME, new Runnable() {
                @Override
                public void run() {
                    bulletsIndex = 0;
                    isAbilityMode = false;
                    attackSpeed = CANNON_ATTACK_SPEED;
                }
            });
        } else {
            bulletsCount = MathUtils.random(2, 3);
            attackSpeed = CANNON_ATTACK_SPEED * 0.1f;
            isAbilityMode = true;
            gameStage.game.timerManager.run(ABILITY_TIME, new Runnable() {
                @Override
                public void run() {
                    bulletsIndex = 0;
                    isAbilityMode = false;
                    attackSpeed = CANNON_ATTACK_SPEED;
                }
            });
        }
    }

    @Override
    public BezierBullet getNextBullet() {
        if(++nextBulletIndex >= gameBullets.size) {
            nextBulletIndex = 0;
        }
        return (BezierBullet)gameBullets.get(nextBulletIndex);
    }

    @Override
    public void reset() {
        if(isAbilityMode) {
            gameStage.game.timerManager.removeTimer(ABILITY_TIME);
            isAbilityMode = false;
        }
    }
}
