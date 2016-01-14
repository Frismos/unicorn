package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.userdata.MiniUnicornUserData;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 11/28/2015.
 */
public class MiniUnicorn extends GameActor {

    public int damage;

    public MiniUnicorn(GameStage stage, UserData userData) {
        super(stage, userData, ColorType.getRandomColor());

        setColorType(colorType);
        int positionY = MathUtils.random(GameStage.ROW_LENGTH * 2 - 1);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight / 2 + getHeight() / 2);
        animationState.setAnimation(0, "hors", true);
        damage = 3;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
//        this.getUserData().colorType = colorType;

        Color color = Color.valueOf(Strings.BLUE);//blue
        if (colorType == ColorType.GREEN) {
            color = Color.valueOf(Strings.GREEN);
        } else if (colorType == ColorType.YELLOW) {
            color = Color.valueOf(Strings.YELLOW);
        } else if (colorType == ColorType.RED) {
            color = Color.valueOf(Strings.RED);
        }
        for (int i = 0; i < skeleton.getSlots().size; i++) {
            if(skeleton.getSlots().get(i).getData().getName().contains("color")) {
                skeleton.getSlots().get(i).getColor().r = color.r;
                skeleton.getSlots().get(i).getColor().g = color.g;
                skeleton.getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    @Override
    public MiniUnicornUserData getUserData() {
        return (MiniUnicornUserData)userData;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.MINI_UNICORN;
    }

    @Override
    protected void setScaleRatio() {
        this.scaleRatio = 0.033f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveBy(13 * delta, 0);
        if(getX() > Constants.VIEWPORT_WIDTH) {
            remove();
        }
    }

    @Override
    public boolean remove() {
        resetPosition();
        return super.remove();
    }

    public void resetPosition() {
        int positionY = MathUtils.random(GameStage.ROW_LENGTH * 2 - 1);
        this.setX(Constants.MINI_UNICORN_X);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight / 2 + getHeight() / 2);
        super.act(Gdx.graphics.getDeltaTime());
    }
}
