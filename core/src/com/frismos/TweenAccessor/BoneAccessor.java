package com.frismos.TweenAccessor;

import aurelienribon.tweenengine.TweenAccessor;
import com.esotericsoftware.spine.Bone;

/**
 * Created by edgaravanyan on 1/21/16.
 */
public class BoneAccessor implements TweenAccessor<Bone> {

    public static final int ROTATION = 0;

    @Override
    public int getValues(Bone target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ROTATION :
                returnValues[0] = target.getRotation();
                return 1;
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
            default:
                assert false;
                break;
        }
    }
}
