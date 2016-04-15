package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.stage.SimpleStage;

/**
 * Created by Arman on 4/15/2016.
 */
public class Trail extends GameActor {


    public Trail(GameStage stage, ColorType colorType) {
        super(stage, colorType);
    }

    @Override
    protected void setResourcesPath() {
        path = "@monsters/trail";
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = .05f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "animation", false);

    }
}
