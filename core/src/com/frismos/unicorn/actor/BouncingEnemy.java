package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class BouncingEnemy extends ShootingEnemy {
    private boolean invulnerable = false;

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

    public BouncingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType, false);
        skeletonActor.getAnimationState().setTimeScale(1.0f);
        skeletonActor.getAnimationState().getData().setMix("jump", "walk", 0.1f);
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
        TIME_STEP = 2f;
        FIRE_CHANCE = 60;
        hitPoints = 1;
        setHeight(getHeight() + 1);
//        showProgressBar();
    }

    @Override
    public void hit(int damage) {
        if(!invulnerable) {
            super.hit(damage);
        }
    }

    @Override
    public void attack() {
        int positionY = MathUtils.random(GameStage.ROW_LENGTH - 1);
        if(this.positionY != positionY) {
            this.positionY = positionY;
//        invulnerable = true;
            float destY = gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + getHeight() / 4;
//            animationState.setTimeScale(0.6f);

            skeletonActor.getAnimationState().setAnimation(0, "jump", true);
            Interpolation pol = Interpolation.bounceIn;
            addAction(Actions.sequence(Actions.moveTo(getX(), destY, 0.6f, Interpolation.swingOut), Actions.run(moveListener)));
        }
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.BOUNCING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.6f;
    }
}
