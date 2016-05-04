package com.frismos.unicorn.enums;

import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public enum ColorType {
    BLUE,
    GREEN,
    RED,
    RAINBOW,
    DEBUG;

    public static ColorType getRandomColor() {
        int length = GameStage.ROW_LENGTH > ColorType.values().length ? ColorType.values().length : GameStage.ROW_LENGTH;
        return values()[(int) (Math.random() * length)];
    }
}
