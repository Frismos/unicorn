package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class WalkingEnemy extends Enemy {
    public WalkingEnemy(GameStage stage, ColorType colorType) {
        this(stage, colorType, false);
    }

    public WalkingEnemy(GameStage stage, ColorType colorType, boolean isTutorial) {
        super(stage, colorType, isTutorial);
        hitPoints = 1;
        Debug.Log("Walking Enemy ctr()");
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
