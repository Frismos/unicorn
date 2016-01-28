package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/10/2015.
 */
public class AttackingEnemy extends Enemy {
    public AttackingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        hitPoints = 15;
        showProgressBar();
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
        if(!isAttackingOnUnicorn) {
            attack();
        }
    }

    @Override
    public void attack() {
        isAttackingOnUnicorn = true;
        skeletonActor.getAnimationState().setTimeScale(1.5f);
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
