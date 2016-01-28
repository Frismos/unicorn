package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Utils;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public class Bullet extends GameActor {

    public static final int UP_DIAGONAL = 0;
    public static final int RIGHT = 1;
    public static final int DOWN_DIAGONAL = 2;
    public static final int DOWN_BACK_DIAGONAL = 3;
    public static final int LEFT = 4;
    public static final int UP_BACK_DIAGONAL = 5;
    public static final int UP = 6;
    public static final int DOWN = 7;

    protected float directionX = 0.5f;
    protected float directionY = 0.1f;
    public Vector2 destPoint = new Vector2();

    protected boolean isFiring;
    private float changeColorTimer = 0.0f;
    private static final float CHANGE_COLOR_TIME = 0.1f;
    private int nextIndex = 0;

    protected float speed = 0;

    protected boolean isDestroyed;

    public int damage;

    private AnimationState.AnimationStateListener bulletDestroyAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {

        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            isDestroyed = true;
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public Bullet(GameStage stage, ActorDataType actorDataType) {
        this(stage, 0, actorDataType);
    }

    public Bullet(GameStage stage, float x, float y, ActorDataType actorDataType) {
        this(stage, 0, actorDataType);
        destPoint.x = x;
        destPoint.y = y;
        destPoint = gameStage.screenToStageCoordinates(destPoint);
        calculateAngle();
    }

    public Bullet(GameStage stage, float angle, ActorDataType actorDataType) {
        super(stage, null);

        if(actorDataType == ActorDataType.BULLET) {
            speed = GameStage.BULLET_MOVE_SPEED;
            damage = 5;
        } else if(actorDataType == ActorDataType.ENEMY_BULLET) {
            speed = GameStage.ENEMY_BULLET_MOVE_SPEED;
            damage = 1;
        }

        setColorType(colorType);
        setAngle(angle);
        setUserObject(actorDataType);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "life", true);
    }

    public float calculateAngle() {
        float angle = (float)Math.toDegrees(Math.atan2(destPoint.x - getX(), destPoint.y - getY()));
        angle = 90 - angle;
        setAngle(angle);
        return angle;
    }

    public void setAngle(float angle) {
        skeletonActor.getSkeleton().getRootBone().setRotation(angle);

        directionX = MathUtils.cosDeg(angle);
        directionY = MathUtils.sinDeg(angle);
        setRotation(angle);
    }

    public void fire() {
        isFiring = true;
    }

    @Override
    protected void setResourcesPath() {
        this.path = Strings.BULLET;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.75f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        changeColorTimer += delta;
        if(colorType == ColorType.RAINBOW) {
            if(changeColorTimer >= CHANGE_COLOR_TIME) {
                Utils.colorActor(this, Utils.colors.get(nextIndex++));
                if(nextIndex == Utils.colors.size) {
                    nextIndex = 0;
                }
                changeColorTimer = 0;
            }
        }
        if(isFiring) {
            move(delta);
        }
        if(isDestroyed ||
                this.getX() > Constants.VIEWPORT_WIDTH + this.getWidth() || this.getX() < 0 ||
                this.getY() > Constants.VIEWPORT_HEIGHT * 2 || this.getY() < -Constants.VIEWPORT_HEIGHT) {
            isDestroyed = false;
            this.remove();
        }
    }

    public void move(float delta) {
        moveBy(speed * delta * directionX, speed * delta * directionY);
    }

    public void destroy() {
        gameStage.collisionDetector.collisionListeners.removeValue(this, false);
        isFiring = false;
        skeletonActor.getAnimationState().setAnimation(0, "destroy", false);
        skeletonActor.getAnimationState().removeListener(bulletDestroyAnimationListener);
        skeletonActor.getAnimationState().addListener(bulletDestroyAnimationListener);
    }

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    @Override
    public boolean remove() {
        resetPosition();
        return super.remove();
    }

    public void resetPosition() {
        skeletonActor.getAnimationState().removeListener(bulletDestroyAnimationListener);
        skeletonActor.getAnimationState().setAnimation(0, "life", true);
        this.setX(Constants.VIEWPORT_WIDTH + 10);
        this.setY(Constants.VIEWPORT_HEIGHT + 10);
        super.act(Gdx.graphics.getDeltaTime());
    }
}
