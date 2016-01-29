package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.enums.ActorDataType;
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

    public MiniUnicorn(GameStage stage) {
        super(stage, ColorType.getRandomColor());

        setColorType(colorType);
        resetPosition();
        damage = 3;
        setUserObject(ActorDataType.MINI_UNICORN);
    }

    @Override
    protected void startDefaultAnimation() {
        setColorType(colorType);
        skeletonActor.getAnimationState().setAnimation(0, "hors", true);
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
        for (int i = 0; i < skeletonActor.getSkeleton().getSlots().size; i++) {
            if(skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color")) {
                skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }
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
        float yOffset = positionY % 2 == 0 ? getHeight() : getHeight() / 4;
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight / 2 + yOffset);
        super.act(Gdx.graphics.getDeltaTime());
    }
}
