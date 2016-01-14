package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class WalkingEnemy extends Enemy {
    public WalkingEnemy(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);
        if (MathUtils.randomBoolean()) {
            animationState.setAnimation(0, "walk1", true);
        } else {
            animationState.setAnimation(0, "walk", true);
        }
        hitPoints = 1;
    }

    @Override
    public void attack() {

    }

    @Override
    protected void setResourcesPath() {
        path = Strings.ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.55f;
    }
}
