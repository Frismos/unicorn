package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Utils;

/**
 * Created by edgar on 12/10/2015.
 */
public class AttackingEnemy extends Enemy {
    public AttackingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        maxHitPoints = hitPoints = AIManager.ATTACKING_ENEMY_HP;
        showProgressBar();
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        if(isAttackingOnUnicorn) {
            super.hit(damage, bullet);
        } else {
            if(hitPoints > 0) {
                Utils.colorActor(this, Color.WHITE);
                addAction(Actions.sequence(Actions.delay(0.02f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Utils.colorActor(AttackingEnemy.this, getColor());
                    }
                })));
            }
            bullet.isHit = true;
            attack();
        }
    }

    @Override
    public void attack() {
        isAttackingOnUnicorn = true;
        moveSpeed = 7.5f;

        skeletonActor.getAnimationState().setTimeScale(2.0f);
        skeletonActor.getAnimationState().setAnimation(1, "attack", true);
        gameStage.unicorn.addPositionChangeListener(this);
    }

    @Override
    public void wallAttackingAnimation() {

    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.ATTACKING_ENEMY_SCALE_RATIO;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.ATTACKING_ENEMY;
    }

    @Override
    public void die() {
        skeletonActor.getAnimationState().clearTrack(1);
        super.die();
    }
}
