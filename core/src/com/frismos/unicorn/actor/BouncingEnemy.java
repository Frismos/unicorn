package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class BouncingEnemy extends ShootingEnemy {
    private boolean invulnerable = false;

    private AnimationState.AnimationStateListener jumpListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
            gameStage.game.soundManager.playMusic(SoundManager.JUMP, Sound.class, true, false);
            addAction(Actions.sequence(Actions.moveTo(getX(), destY, 0.6f, Interpolation.swingOut), Actions.run(moveListener)));
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

    private Runnable moveListener = new Runnable() {
        @Override
        public void run() {
            if(isAttacking) {
                invulnerable = false;
//            animationState.setTimeScale(1.0f);
                skeletonActor.getAnimationState().setAnimation(0, "walk", true);
            }
        }
    };
    private float destY;

    public BouncingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType, false);
        skeletonActor.getAnimationState().setTimeScale(1.0f);
        skeletonActor.getAnimationState().getData().setMix("jump-down", "walk", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("jump-up", "walk", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "jump-down", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "jump-up", 0.1f);
        TIME_STEP = 2f;
        FIRE_CHANCE = 70;
        maxHitPoints = hitPoints = AIManager.BOUNCING_ENEMY_HP;
        setHeight(getHeight() + 1);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        if(!invulnerable) {
            super.hit(damage, bullet);
        }
    }

    @Override
    public void attack() {
        skeletonActor.getAnimationState().setTimeScale(1.0f);
        int positionY = MathUtils.random(GameStage.ROW_LENGTH - 1);
        if(this.positionY != positionY) {
            this.positionY = positionY;
//        invulnerable = true;
            int prob = MathUtils.random(2);
            float offsetY = prob == 2 ? getHeight() / 2 : prob == 1 ? getHeight() / 4 : getHeight() / 1.5f;
            destY = gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + offsetY;
//            animationState.setTimeScale(0.6f);

            if(destY > getY()) {
                skeletonActor.getAnimationState().setAnimation(0, "jump-up", true);
            } else {
                skeletonActor.getAnimationState().setAnimation(0, "jump-down", true);
            }
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().addListener(jumpListener);
        }
    }

    @Override
    public void eatWall() {
        gameStage.game.soundManager.playMusic(SoundManager.KNOCK_BOUNCE, Sound.class, true);
    }

    @Override
    public void wallAttackingAnimation() {
        skeletonActor.getAnimationState().setTimeScale(1.0f);
        skeletonActor.getAnimationState().setAnimation(0, "attack", true);
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.BOUNCING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BOUNCING_ENEMY_SCALE_RATIO;
    }
}
