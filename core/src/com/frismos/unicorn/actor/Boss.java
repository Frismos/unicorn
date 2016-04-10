package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;

/**
 * Created by edgar on 12/14/2015.
 */
public abstract class Boss extends ShootingEnemy {
    public boolean isAttackAnimationPlaying = false;

    private AnimationState.AnimationStateListener attackAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
            fireEvent();
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().removeListener(this);
            skeletonActor.getAnimationState().setAnimation(0, "walk", true);
            isAttackAnimationPlaying = false;
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    private AnimationState.AnimationStateListener hitAnimationStateListener = new AnimationState.AnimationStateListener() {
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

    public Boss(GameStage stage, ColorType colorType, boolean isTutorial) {
        super(stage, colorType, isTutorial);
        int positionY = MathUtils.random(GameStage.ROW_LENGTH - 2);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + gameStage.grid.tileHeight / 10);
        if(!isTutorial) {
            moveSpeed = GameStage._BOSS_MOVE_SPEED;
        }
        skeletonActor.getAnimationState().getData().setMix("attack", "walk", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("attack", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "attack", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("hit", "attack", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("hit", "walk", 0.1f);
        setUserObject(ActorDataType.BOSS);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    public void die() {
        gameStage.boss = null;
        super.die();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BOSS_SCALE_RATIO;
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

    public void fireEvent() {
        super.attack();
    }

    public void setSpawnEnemyType(WaveType type) {

    }

    @Override
    public void attack() {
        skeletonActor.getAnimationState().setTimeScale(1.0f);
        skeletonActor.getAnimationState().setAnimation(0, "attack", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(attackAnimationListener);
        isAttackAnimationPlaying = true;
    }

    @Override
    protected void onDieComplete() {
        gameStage.nextLevel();
    }
}
