package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public abstract class Boss extends ShootingEnemy {

    private AnimationState.AnimationStateListener hitAnimationStateListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().removeListener(this);
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
        setX(Constants.ENEMY_X);
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
        gameStage.nextLevel();
        gameStage.boss = null;
        super.die();
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BOSS_SCALE_RATIO;
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
        if(isAttacking) {
            skeletonActor.getAnimationState().setAnimation(0, "hit", false);
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().addListener(hitAnimationStateListener);
        }
    }
}
