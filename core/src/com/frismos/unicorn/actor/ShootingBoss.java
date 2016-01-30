package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.enums.ActorDataType;
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

    public ShootingBoss(GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 1.5f;
        FIRE_CHANCE = 100;
        if(isTutorial) {
            hitPoints = 30;
        } else {
            hitPoints = 75;
        }
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
    public void attack() {
        spawnPoint.x = gameStage.unicorn.tile.getX() + gameStage.grid.tileWidth / 2;
        spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        super.attack();
    }
}
