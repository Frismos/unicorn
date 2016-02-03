package com.frismos.TweenAccessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by edgaravanyan on 2/3/16.
 */
public class ActorAccessor implements TweenAccessor<Actor> {

    public static final int ROTATION = 0;
    public static final int POSITION_XY = 1;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ROTATION :
                returnValues[0] = target.getRotation();
                return 1;
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case POSITION_XY:
                target.setPosition(newValues[0], newValues[1]);
                break;
            default:
                assert false;
                break;
        }
    }
}
