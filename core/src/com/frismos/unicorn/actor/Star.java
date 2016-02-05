package com.frismos.unicorn.actor;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.frismos.TweenAccessor.BoneAccessor;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public class Star extends MainCharacter {

    private static final int MINI_UNICORNS_COUNT = 23;

    private static int miniUnicornsIndex = 0;
    private static final float WAVE_TIME_STEP = 0.2f;
    private static float waveTimeCounter = 0.0f;

    private static Array<MiniUnicorn> miniUnicorns = new Array<>();

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

    public Star(GameStage stage, UnicornType unicornType) {
        super(stage, unicornType);

        for (int i = 0; i < MINI_UNICORNS_COUNT; i++) {
            miniUnicorns.add(new MiniUnicorn(gameStage));
        }

        for (int i = 0; i < 20; i++) {
            gameBullets.add(new Bullet(gameStage, ActorDataType.BULLET));
        }

        skeletonActor.getAnimationState().getData().setMix("fire", "idle", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("idle", "fire", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("fire", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("hit", "fire", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("idle", "hit", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("hit", "idle", 0.1f);
    }

    @Override
    public void playFireAnimation(float x, float y) {
        float stageX = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * x;
        float stageY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * y;

        Bone gunBone = skeletonActor.getSkeleton().findBone("gun");
        float angle = (float) Math.toDegrees(Math.atan2(getFirePoint().x + getX() - stageX, getFirePoint().y + getY() - stageY));
        angle = 90 - angle;

        gunBone.setRotation(angle + 180);
        float rotateAngle = MathUtils.random(3) != 3 ? 180 : -180;
        Tween.to(gunBone, BoneAccessor.ROTATION, 0.5f).targetRelative(rotateAngle - angle).start(gameStage.game.tweenManager);
        super.playFireAnimation(x, y);
    }

    @Override
    public void useAbility() {
        if(miniUnicornsIndex == 0) {
            miniUnicornsIndex = MINI_UNICORNS_COUNT;
        }
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.STAR;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.STAR_SCALE_RATIO;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        waveTimeCounter += delta;
        if(waveTimeCounter >= WAVE_TIME_STEP) {
            waveTimeCounter = 0.0f;
            if (miniUnicornsIndex > 0) {
                gameStage.collisionDetector.collisionListeners.add(miniUnicorns.get(miniUnicornsIndex - 1));
                gameStage.addActor(miniUnicorns.get(miniUnicornsIndex - 1));
//                miniUnicorns.get(miniUnicornsIndex - 1).resetPosition();
                miniUnicornsIndex--;
            }
        }
    }

    @Override
    public void hit(float damage) {
        super.hit(damage);
        skeletonActor.getAnimationState().setAnimation(0, "hit", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(hitAnimationStateListener);
    }

    @Override
    public Bullet getNextBullet() {
        if(++nextBulletIndex >= gameBullets.size) {
            nextBulletIndex = 0;
        }
        return gameBullets.get(nextBulletIndex);
    }

    @Override
    public void reset() {
        //todo implement method
    }
}
