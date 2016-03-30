package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 11/29/2015.
 */
public class ShootingBoss extends Boss {

    public ShootingBoss(GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 1.0f;
        FIRE_CHANCE = 100;
        maxHitPoints = hitPoints = 110;
        showProgressBar();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.SHOOTING_BOSS;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.SHOOTING_BOSS_SCALE_RATIO;
    }

    @Override
    public void fireEvent() {
        spawnPoint.x = gameStage.unicorn.tile.getX() + gameStage.grid.tileWidth / 2;
        spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        super.fireEvent();
    }

    @Override
    public void die(AnimationState.AnimationStateListener dieListener) {
        super.die(dieListener);
    }
}
