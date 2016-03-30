package com.frismos.unicorn.actor;

import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 2/18/16.
 */
public class HallBackground extends Background {
    public HallBackground(GameStage stage) {
        super(stage);
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.HALL_BACKGROUND;
    }
}
