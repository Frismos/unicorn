package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.Vector2;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/19/15.
 */

public class Background extends SpineActor {

    private Vector2 zero;

    public Background(GameStage stage) {
        super(stage);
        animationState.setAnimation(0, "animation", true);
    }

    @Override
    protected void setResourcesPath() {
        path = "background";
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.5f;
    }

    public Vector2 getZero() {
        if(zero == null) {
            zero = new Vector2(skeleton.findBone("zero").getX(), skeleton.findBone("zero").getY());
        }
        return zero;
    }
}
