package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class Unicorn extends MainCharacter {

    private static boolean rainbowMode = false;
    private static final float RAINBOW_TIME = 5.0f;
    private static float rainbowTimer = 0.0f;

    public Unicorn(GameStage stage, UnicornType unicornType) {
        super(stage, unicornType);

        for (int i = 0; i < 30; i++) {
            gameBullets.add(new Bullet(gameStage, ActorDataType.AUTO_BULLET));
        }
    }

    @Override
    public void setColorType(ColorType colorType) {
        if(!rainbowMode || colorType == ColorType.RAINBOW) {
            super.setColorType(colorType);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        rainbowTimer += delta;
        if(rainbowMode && rainbowTimer >= RAINBOW_TIME) {
            rainbowMode = false;
            setColorType(tile.colorType);
        }
    }

    @Override
    public void useAbility() {
        setColorType(ColorType.RAINBOW);
        rainbowMode = true;
        rainbowTimer = 0.0f;
    }

    @Override
    public Bullet getNextBullet() {
        if(++nextBulletIndex >= gameBullets.size) {
            nextBulletIndex = 0;
        }
        return gameBullets.get(nextBulletIndex);
    }

    @Override
    public void reset() {
        //todo implement method
    }

    @Override
    protected void setResourcesPath() {
        this.path = Strings.UNICORN;
    }

    @Override
    protected void setScaleRatio() {
        this.scaleRatio = Constants.UNICORN_SCALE_RATIO;
    }
}


