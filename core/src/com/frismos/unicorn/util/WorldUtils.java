package com.frismos.unicorn.util;

import com.frismos.unicorn.userdata.BossUserData;
import com.frismos.unicorn.userdata.BulletUserData;
import com.frismos.unicorn.userdata.EnemyBulletUserData;
import com.frismos.unicorn.userdata.EnemyUserData;
import com.frismos.unicorn.userdata.MiniUnicornUserData;
import com.frismos.unicorn.userdata.UnicornUserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.userdata.UserData;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class WorldUtils {

    public static UnicornUserData createUnicorn() {
        return new UnicornUserData();
    }

    public static EnemyUserData createEnemy() {
        EnemyUserData userData = new EnemyUserData();
        userData.colorType = ColorType.getRandomColor();
        return userData;
    }

    public static BossUserData createBoss() {
        BossUserData userData = new BossUserData();
        userData.colorType = ColorType.getRandomColor();
        return userData;
    }

    public static BulletUserData createBullet() {
        BulletUserData userData = new BulletUserData();
        return userData;
    }

    public static EnemyBulletUserData createEnemyBullet() {
        EnemyBulletUserData userData = new EnemyBulletUserData();
        return userData;
    }

    public static MiniUnicornUserData createMiniUnicorn() {
        MiniUnicornUserData userData = new MiniUnicornUserData();
        return userData;
    }

    public static CannonBulletUserData createCannonBullet() {
        return new CannonBulletUserData();
    }
}
