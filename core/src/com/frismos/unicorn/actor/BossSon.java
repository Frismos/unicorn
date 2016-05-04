package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 3/2/16.
 */
public class BossSon extends WalkingEnemy {
    public BossSon(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        maxHitPoints = hitPoints = 1;
        showProgressBar();
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.WALKING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.WALKING_ENEMY_SCALE_RATIO / 1.2f;
    }
}
