package com.frismos.unicorn.util;

import com.badlogic.gdx.physics.box2d.Body;
import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.UserDataType;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public class BodyUtils {

    public static boolean bodyIsGround(Body body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }

    public static boolean bodyIsUnicorn(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.UNICORN;
    }

    public static boolean bodyIsMiniUnicorn(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.MINI_UNICORN;
    }

    public static boolean bodyIsEnemy(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.ENEMY;
    }

    public static boolean bodyIsBullet(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.BULLET;
    }

    public static boolean bodyIsCannonBullet(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.CANNON_BULLET;
    }

    public static boolean bodyIsPlatform(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.PLATFORM;
    }

    public static boolean bodyIsBoss(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.BOSS;
    }

    public static boolean bodyIsEnemyBullet(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.ENEMY_BULLET;
    }

    public static boolean bodyIsSpell(GameActor body) {
        UserData userData = (UserData)body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.SPELL;
    }
}
