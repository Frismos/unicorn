package com.frismos.unicorn.actor;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgaravanyan on 1/26/16.
 */
public class Rhino extends MainCharacter {

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

        for (int i = 0; i < 5; i++) {
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
        this.touchX = x;
        this.touchY = y;
        skeletonActor.getAnimationState().setAnimation(0, "fire", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(fireAnimationListener);
//        }
    }

    @Override
    public void useAbility() {

    }

    @Override
    public BezierBullet getNextBullet() {
        if(++nextBulletIndex >= gameBullets.size) {
            nextBulletIndex = 0;
        }
        return (BezierBullet)gameBullets.get(nextBulletIndex);
    }
}
