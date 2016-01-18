package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UserDataType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.WorldUtils;

/**
 * Created by edgar on 12/10/2015.
 */
public class ShootingEnemy extends Enemy {

    protected float TIME_STEP = 4f;
    protected float accumulator = TIME_STEP;
    protected float FIRE_CHANCE = 90;
    private boolean fire = false;
    private Bullet bullet;

    public ShootingEnemy(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);
        if(userData.getUserDataType() != UserDataType.BOSS) {
            if (MathUtils.randomBoolean()) {
                animationState.setAnimation(0, "walk1", true);
            } else {
                animationState.setAnimation(0, "walk", true);
            }
        }
        hitPoints = 1;
//        showProgressBar();
    }

    @Override
    public void attack() {
        if(isAttacking) {
            if (!(gameStage.boss != null && !isSonOfABoss) || getUserData().getUserDataType() == UserDataType.BOSS) {
                if (!isSonOfABoss) {
                    spawnPoint.x = gameStage.unicorn.tile.getX() + gameStage.grid.tileWidth / 2;
                    spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());
                }
                if (!(this instanceof Boss)) {
                    animationState.setAnimation(1, "attack", false);
                    float angle = (float) Math.toDegrees(Math.atan2(spawnPoint.x - skeleton.findBone("gun").getWorldX() - getX(), spawnPoint.y - skeleton.findBone("gun").getWorldY() - getY()));
                    angle = 90 - angle;
                    if (!isSonOfABoss) {
                        skeleton.findBone("gun").setRotation(angle);
                    }
                }
                fire = true;
                if (!isSonOfABoss) {
                    spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
                }
                bullet = new Bullet(gameStage, WorldUtils.createEnemyBullet(), spawnPoint.x, spawnPoint.y);
                bullet.setColorType(ColorType.RAINBOW);
//            bullet.p0 = new Vector2(bullet.getX(), bullet.getY());
//            bullet.p3 = new Vector2(bullet.destPoint.x, bullet.destPoint.y);
//            bullet.p1 = new Vector2(bullet.getX() - 3, bullet.getY() + 10);
//            bullet.p2 = new Vector2((bullet.getX() - bullet.destPoint.x) / 4 + bullet.destPoint.x, bullet.destPoint.y + 10);
            }
        }
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.55f;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.SHOOTING_ENEMY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAttacking) {
            if(fire) {
                fire = false;
                bullet.fire();
                gameStage.collisionDetector.collisionListeners.add(bullet);
                gameStage.addActor(bullet);
                float offsetX = getWidth() * getScaleX() / 2;
                float offsetY = getHeight() * getScaleY() / 2;
                if(!(this instanceof Boss)) {
                    offsetX = skeleton.findBone("bullet-spawn").getWorldX();
                    offsetY = skeleton.findBone("bullet-spawn").getWorldY();
                }
                bullet.setX(getX() + offsetX);
                bullet.setY(getY() + offsetY);
                bullet.calculateAngle();
            }
            accumulator += delta;
            if (accumulator >= TIME_STEP) {
                accumulator = 0;
                if (MathUtils.random(100) <= FIRE_CHANCE) {
                    attack();
                }
                if (FIRE_CHANCE < 50) {
                    FIRE_CHANCE += 1;
                }
            }
        }
    }
}
