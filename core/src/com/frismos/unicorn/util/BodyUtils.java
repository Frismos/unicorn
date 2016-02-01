package com.frismos.unicorn.util;

import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.enums.ActorDataType;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public class BodyUtils {

    public static boolean bodyIsUnicorn(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.UNICORN;
    }

    public static boolean bodyIsMiniUnicorn(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.MINI_UNICORN;
    }

    public static boolean bodyIsEnemy(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.ENEMY;
    }

    public static boolean bodyIsBullet(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.BULLET || actorDataType == ActorDataType.AUTO_BULLET;
    }

    public static boolean bodyIsCannonBullet(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.CANNON_BULLET;
    }

    public static boolean bodyIsPlatform(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.PLATFORM;
    }

    public static boolean bodyIsBoss(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.BOSS;
    }

    public static boolean bodyIsEnemyBullet(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.ENEMY_BULLET;
    }

    public static boolean bodyIsSpell(GameActor body) {
        ActorDataType actorDataType = (ActorDataType)body.getUserObject();
        return actorDataType == ActorDataType.SPELL;
    }
}
