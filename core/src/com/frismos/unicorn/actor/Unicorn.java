package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

import java.util.Observable;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class Unicorn extends MainCharacter {

    public Unicorn(GameStage stage, UnicornType unicornType) {
        super(stage, unicornType);

        for (int i = 0; i < 30; i++) {
            gameBullets.add(new Bullet(gameStage, ActorDataType.AUTO_BULLET));
        }
    }

    @Override
    public void setColorType(ColorType colorType) {
//        if(!rainbowMode || colorType == ColorType.RAINBOW) {
            super.setColorType(colorType);
//        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void useAbility() {
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

    @Override
    public void update(Observable o, Object arg) {

    }
}


