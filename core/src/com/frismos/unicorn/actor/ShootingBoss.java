package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.userdata.BossUserData;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 11/29/2015.
 */
public class ShootingBoss extends Boss {

    public ShootingBoss(GameStage stage, BossUserData userData, ColorType colorType) {
        this(stage, userData, colorType, false);
    }

    public ShootingBoss(GameStage gameStage, BossUserData boss, ColorType colorType, boolean isTutorial) {
        super(gameStage, boss, colorType, isTutorial);
        TIME_STEP = 1.5f;
        FIRE_CHANCE = 100;
        if(isTutorial) {
            hitPoints = 30;
        } else {
            hitPoints = 75;
        }
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.BOSS;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.6f;
    }

    @Override
    public void attack() {
        spawnPoint.x = gameStage.unicorn.tile.getX() + gameStage.grid.tileWidth / 2;
        spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        super.attack();
    }
}
