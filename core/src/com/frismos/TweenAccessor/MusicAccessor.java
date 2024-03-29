package com.frismos.TweenAccessor;

import com.badlogic.gdx.audio.Music;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by eavanyan on 3/30/16.
 */
public class MusicAccessor implements TweenAccessor<Music> {
    public static final int VOLUME = 0;
    @Override
    public int getValues(Music target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case VOLUME:
                returnValues[0] = target.getVolume();
                return 1;
            default:
                assert false;
                return -1;
        }

    }

    @Override
    public void setValues(Music target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case VOLUME:
                target.setVolume(newValues[0]);
                break;
            default:
                assert false;
                break;
        }
    }
}
