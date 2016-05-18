package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 3/2/16.
 */
public class BossSon extends WalkingEnemy {
    public BossSon(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        maxHitPoints = hitPoints = 1;
        showProgressBar();
        moveSpeed = INITIAL_MOVE_SPEED + stage.unicorn.getCombo() / 20.0f + (float)Math.sqrt(gameStage.gameTime / 4);
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
