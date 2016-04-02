package com.frismos.unicorn.ui;

import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.UIStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 4/2/16.
 */
public class CompleteDialog extends SpineActor {

    public CompleteDialog(UIStage stage) {
        super(stage);
        Debug.log("width = " + getWidth());
        setPosition(stage.getWidth() / 2 - getWidth(), stage.getHeight() / 2 - getHeight());
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.COMPLETE_DIALOG;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.75f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "show", false);
    }
}
