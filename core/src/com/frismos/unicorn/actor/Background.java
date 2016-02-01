package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.Vector2;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;

/**
 * Created by edgaravanyan on 10/19/15.
 */

public class Background extends SpineActor {

    private Vector2 zero;

    public Background(GameStage stage) {
        super(stage);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "animation", true);
    }

    @Override
    protected void setResourcesPath() {
        path = "background";
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BACKGROUND_SCALE_RATIO;
    }

    public Vector2 getZero() {
        if(zero == null) {
            zero = new Vector2(skeletonActor.getSkeleton().findBone("zero").getX(), skeletonActor.getSkeleton().findBone("zero").getY());
        }
        return zero;
    }
}
