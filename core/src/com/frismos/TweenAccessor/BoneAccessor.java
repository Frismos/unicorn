package com.frismos.TweenAccessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.Bone;

/**
 * Created by edgaravanyan on 1/21/16.
 */
public class BoneAccessor implements TweenAccessor<Bone> {

    public static final int ROTATION = 0;
    public static final int SCALE = 1;
    public static final int POSITION = 2;
    public static final int ALPHA = 3;

    @Override
    public int getValues(Bone target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ROTATION :
                returnValues[0] = target.getRotation();
                return 1;
            case SCALE:
                returnValues[0] = target.getScaleX();
                return 1;
            case ALPHA:
                returnValues[0] = target.getData().getColor().a;
                return 1;
            case POSITION:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Bone target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case SCALE:
                target.setScale(newValues[0]);
                break;
            case ALPHA:
                target.getData().getColor().a = newValues[0];
                break;
            case POSITION:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            default:
                assert false;
                break;
        }
    }
}
