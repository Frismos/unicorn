package com.frismos.unicorn.enums;

import com.badlogic.gdx.math.MathUtils;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgar on 12/15/2015.
 */
public enum SpellType {
    HEALTH,
    RAINBOW_MODE,
    CALL_UNICORNS;

    private static int CALL_UNICORNS_CHANCE = 33;
    private static int RAINBOW_MODE_CHANCE = 33;
    private static int HEALTH_CHANCE = 33;

    public static SpellType getRandomValue(boolean isHealthLow) {
        int index = 0;
        int prob = MathUtils.random(100);
        if(isHealthLow) {
            HEALTH_CHANCE = 50;
            CALL_UNICORNS_CHANCE = 25;
            RAINBOW_MODE_CHANCE = 25;
        }
        if(prob < HEALTH_CHANCE) {
            index = 0;
        } else if(prob < HEALTH_CHANCE + CALL_UNICORNS_CHANCE) {
            index = 1;
        } else if(prob < HEALTH_CHANCE + CALL_UNICORNS_CHANCE + RAINBOW_MODE_CHANCE) {
            index = 2;
        }
        resetChances();
        return values()[index];
    }

    private static void resetChances() {
        HEALTH_CHANCE = 33;
        CALL_UNICORNS_CHANCE = 33;
        RAINBOW_MODE_CHANCE = 33;
    }
}
