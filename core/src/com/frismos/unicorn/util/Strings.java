package com.frismos.unicorn.util;

import com.frismos.unicorn.UnicornGame;

/**
 * Created by edgar on 11/18/2015.
 */
public class Strings {

    public static final String BLUE = "4a5bcd";
    public static final String GREEN = "5ab742";
    public static final String YELLOW = "ece000";
    public static final String RED = "e74546";

    public static final String UNICORN = "unicorn";
    public static final String RHINO = "rhino";
    public static final String MINI_UNICORN = "unicorn";
    public static final String BULLET = "bullet";

    public static final String ATTACKING_ENEMY = "attacking-enemy";
    public static final String SHOOTING_ENEMY = "shooting-enemy";
    public static final String WALKING_ENEMY = "walking-enemy";
    public static final String MOTHER_BOSS = "boss";
    public static final String SHOOTING_BOSS = "boss";
    public static final String BOUNCING_ENEMY = "bouncing-enemy";
    public static final String BOMB = "bomb";

    private StringBuilder builder;

    public Strings() {
        builder = new StringBuilder();
    }

    public Strings addString(Object str) {
        builder.append(str);
        return this;
    }

    @Override
    public String toString() {
        String str = builder.toString();
        builder.delete(0, builder.length());
        return str;
    }
}
