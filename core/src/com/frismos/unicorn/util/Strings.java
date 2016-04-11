package com.frismos.unicorn.util;

/**
 * Created by edgar on 11/18/2015.
 */
public class Strings {

    public static final String BLUE = "3e98f5";
    public static final String GREEN = "83d92d";
    public static final String YELLOW = "ece000";
    public static final String RED = "ef3232";

    public static final String UNICORN = "@left/creature";
    public static final String RHINO = "rhino";
    public static final String MINI_UNICORN = "unicorn";
    public static final String BULLET = "@bullets/simple";
    public static final String ATTACKING_ENEMY = "@monsters/slime-footer";
    public static final String SHOOTING_ENEMY = "@monsters/slime-shooter";
    public static final String WALKING_ENEMY = "@monsters/slime";
    public static final String CHEWING_ENEMY = "@monsters/slime-chewer";
    public static final String RUNNING_ENEMY = "@monsters/slime-leap";
    public static final String MOTHER_BOSS = "@monsters/boss1";
    public static final String HEALTHY_ENEMY = "@monsters/slime-healthy";
    public static final String SHOOTING_BOSS = "@monsters/boss2";
    public static final String BOUNCING_ENEMY = "@monsters/slime-jumper";
    public static final String BOMB = "bomb";
    public static final String FIELD_BACKGROUND = "worlds/1/bg";
    public static final String HALL_BACKGROUND = "worlds/2/bg";
    public static final String CRYSTAL_BACKGROUND = "worlds/3/bg";
    public static final String HALL_TILE = "worlds/2/tiles";
    public static final String FIELD_TILE = "worlds/1/tiles";
    public static final String STAR = "star";
    public static final String COLORS_PLATFORM = "@left";
    public static final String RAINBOW_SPELL = "@powers/rainbow";
    public static final String UNICORN_SPELL = "@powers/unicorn";
    public static final String HEALTH_SPELL = "@powers/heal";
    public static final String COMPLETE_DIALOG = "@dialog";
    public static final String SPLASH_SCREEN = "menuscreen";

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
