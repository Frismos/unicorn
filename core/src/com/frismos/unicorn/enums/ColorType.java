package com.frismos.unicorn.enums;

import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public enum ColorType {
    YELLOW,
    RED,
    GREEN,
    BLUE,
    RAINBOW;

    public static ColorType getRandomColor() {
        int length = GameStage.ROW_LENGTH > ColorType.values().length ? ColorType.values().length : GameStage.ROW_LENGTH;
        return values()[(int) (Math.random() * length)];
    }
}
