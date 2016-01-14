package com.frismos.unicorn.actor;

import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/10/2015.
 */
public class AttackingEnemy extends Enemy {
    public AttackingEnemy(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);
        animationState.setAnimation(0, "walk", true);
        hitPoints = 5;
        showProgressBar();
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
        attack();
    }

    @Override
    public void attack() {
        isAttackingOnUnicorn = true;
        animationState.setTimeScale(1.5f);
        gameStage.unicorn.addPositionChangeListener(this);
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.65f;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.ATTACKING_ENEMY;
    }

}
