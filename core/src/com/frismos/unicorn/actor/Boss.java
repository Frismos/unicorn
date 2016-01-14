package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Constants;

/**
 * Created by edgar on 12/14/2015.
 */
public abstract class Boss extends ShootingEnemy {
    public Boss(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);
        setX(Constants.ENEMY_X);
        setSize(10, 15);
        int positionY = MathUtils.random(GameStage.ROW_LENGTH - 2);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + gameStage.grid.tileHeight / 10);
        moveSpeed = GameStage._BOSS_MOVE_SPEED;
        animationState.setAnimation(0, "animation", true);
    }

    @Override
    public void die() {
        gameStage.nextLevel();
        gameStage.boss = null;
        super.die();
    }
}
