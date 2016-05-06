package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 2/18/16.
 */
public class FieldBackground extends Background {
    public FieldBackground(GameStage stage) {
        super(stage);
    }

    @Override
    protected void setResourcesPath() {
        float prob = MathUtils.random(1, 100);
        Debug.log("prob = " + prob);
        if(prob  <= 33.3f) {
            path = Strings.FIELD_BACKGROUND;
        } else if(prob  <= 66.6f) {
            path = Strings.HALL_BACKGROUND;
        } else {
            path = Strings.CRYSTAL_BACKGROUND;
        }
    }
}
