package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

import java.util.Observable;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public class Star extends MainCharacter {

    private static final int MINI_UNICORNS_COUNT = 43;

    private static boolean rainbowMode = false;
    private static final float RAINBOW_TIME = 11.0f;
    private static float rainbowTimer = 0.0f;

    private static int miniUnicornsIndex = 0;
    private static final float WAVE_TIME_STEP = 0.1f;
    private static float waveTimeCounter = 0.0f;

    private static Array<MiniUnicorn> miniUnicorns = new Array<>();

    private float defaultRotation;
    private Bone gunBone;

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
            Bullet bullet = new Bullet(gameStage, ActorDataType.BULLET);
            bullet.subject.addObserver(this);
            gameBullets.add(bullet);
        }

        gunBone = skeletonActor.getSkeleton().findBone("gun");
        defaultRotation = gunBone.getRotation();

//        skeletonActor.getAnimationState().getData().setMix("fire", "idle", 0.1f);
//        skeletonActor.getAnimationState().getData().setMix("idle", "fire", 0.1f);
//        skeletonActor.getAnimationState().getData().setMix("fire", "hit", 0.1f);
//        skeletonActor.getAnimationState().getData().setMix("hit", "fire", 0.1f);
//        skeletonActor.getAnimationState().getData().setMix("idle", "hit", 0.1f);
//        skeletonActor.getAnimationState().getData().setMix("hit", "idle", 0.1f);
    }

    @Override
    public void playFireAnimation(float x, float y) {
        float stageX = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * x;
        float stageY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * y;

        float angle = (float) Math.toDegrees(Math.atan2(getFirePoint().x + getX() - stageX, getFirePoint().y + getY() - stageY));
        angle = 90 - angle;

        gunBone.setRotation(angle + defaultRotation + 180);
//        Tween.to(gunBone, BoneAccessor.ROTATION, 0.5f).targetRelative(180-angle).start(gameStage.game.tweenManager);
//        skeletonActor.getSkeleton().updateWorldTransform();
//        touchX = x;
//        touchY = y;
//        if(touchX != Float.MIN_VALUE) {
//            gameStage.fireBullet(touchX, touchY);
//            touchX = Float.MIN_VALUE;
//        } else if(bulletAngle != Float.MIN_VALUE) {
//            gameStage.fireBullet(bulletAngle);
//        }
        super.playFireAnimation(x, y);
    }

    public void setRainbowMode() {
        setColorType(ColorType.RAINBOW);
        rainbowMode = true;
        rainbowTimer = 0.0f;
    }

    public void callUnicorns() {
        if(miniUnicornsIndex == 0) {
            miniUnicornsIndex = MINI_UNICORNS_COUNT;
        }
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.UNICORN;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.UNICORN_SCALE_RATIO;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        waveTimeCounter += delta;
        if(waveTimeCounter >= WAVE_TIME_STEP) {
            waveTimeCounter = 0.0f;
            if (miniUnicornsIndex > 0) {
                gameStage.collisionDetector.addListener(miniUnicorns.get(miniUnicornsIndex - 1));
                gameStage.addActor(miniUnicorns.get(miniUnicornsIndex - 1));
//                miniUnicorns.get(miniUnicornsIndex - 1).resetPosition();
                miniUnicornsIndex--;
            }
        }

        rainbowTimer += delta;
        if(rainbowMode && rainbowTimer >= RAINBOW_TIME) {
            rainbowMode = false;
            setColorType(tile.colorType);
        }
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        super.hit(damage, bullet);
        if(bullet != null) {
            Gdx.input.vibrate(100);
        }
//        skeletonActor.getAnimationState().setAnimation(0, "hit", false);
//        skeletonActor.getAnimationState().clearListeners();
//        skeletonActor.getAnimationState().addListener(hitAnimationStateListener);
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

    @Override
    public void setColorType(ColorType colorType) {
        if(!rainbowMode || colorType == ColorType.RAINBOW) {
            super.setColorType(colorType);
        }
    }

    @Override
    public void useAbility() {

    }

    @Override
    public void update(Observable o, Object arg) {
        Bullet bullet = (Bullet)arg;
        if(!bullet.isHit && !bullet.isSubBullet) {
            resetCombo();
        }
    }
}
