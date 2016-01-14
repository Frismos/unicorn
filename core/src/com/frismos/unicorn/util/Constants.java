package com.frismos.unicorn.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class Constants {

    public static final float SCALE_RATIO = 32.0f;

    public static final float VIEWPORT_WIDTH = 60.0f;//49.18618, 27.964397
    public static final float VIEWPORT_HEIGHT = 33.75f;

    public static final float SCALE_RATIO_X = 800.0f / VIEWPORT_WIDTH;
    public static final float SCALE_RATIO_Y = 480.0f / VIEWPORT_HEIGHT;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0.0f, -0.0f);

    public static final float UNICORN_X = 5.0f;
    public static final float UNICORN_Y = 10.0f;
    public static final float UNICORN_WIDTH = 2.5f;
    public static final float UNICORN_HEIGHT = 4f;
    public static final float UNICORN_DENSITY = 0.0f;
    public static final float UNICORN_GRAVITY_SCALE = 0.0f;

    public static final float ENEMY_WIDTH = 3f;
    public static final float ENEMY_HEIGHT = 4.5f;
    public static final float ENEMY_X = VIEWPORT_WIDTH - ENEMY_WIDTH;
    public static final float ENEMY_Y = 10.0f;
    public static final float ENEMY_DENSITY = 5.0f;
    public static final float ENEMY_GRAVITY_SCALE = 0.0f;

    public static final float PLATFORM_WIDTH = 5.0f;
    public static final float PLATFORM_HEIGHT = 1.0f;
    public static final float PLATFORM_X = 0.0f;
    public static final float PLATFORM_Y = 2.5f;
    public static final float PLATFORM_DENSITY = 0.0f;
    public static final float PLATFORM_GRAVITY_SCALE = 0.0f;

    public static final float MINI_UNICORN_WIDTH = 1f;
    public static final float MINI_UNICORN_HEIGHT = 1.5f;
    public static final float MINI_UNICORN_X = UNICORN_X + UNICORN_WIDTH;
    public static final float MINI_UNICORN_Y = PLATFORM_Y + UNICORN_HEIGHT;
    public static final float MINI_UNICORN_DENSITY = 0.0f;
    public static final float MINI_UNICORN_GRAVITY_SCALE = 0.0f;

    public static final float BOSS_WIDTH = 9f;
    public static final float BOSS_HEIGHT = 15f;
    public static final float BOSS_X = VIEWPORT_WIDTH - BOSS_WIDTH;
    public static final float BOSS_Y = 10.0f;
    public static final float BOSS_DENSITY = 0.0f;
    public static final float BOSS_GRAVITY_SCALE = 0.0f;

    public static final float BULLET_WIDTH = 0.5f;
    public static final float BULLET_HEIGHT = 1.25f;
    public static final float BULLET_X = UNICORN_X + UNICORN_WIDTH;
    public static final float BULLET_Y = PLATFORM_Y + UNICORN_HEIGHT;
    public static final float BULLET_DENSITY = 0.0f;
    public static final float BULLET_GRAVITY_SCALE = 0.0f;
    
    public static final float GROUND_X = VIEWPORT_WIDTH / 4f;
    public static final float GROUND_Y = PLATFORM_Y;
    public static final float GROUND_WIDTH = 50.0f;
    public static final float GROUND_HEIGHT = 0.25f;
    public static final float GROUND_DENSITY = 0.0f;

    public static final float TILE_WIDTH = 256.0f / SCALE_RATIO;
}
