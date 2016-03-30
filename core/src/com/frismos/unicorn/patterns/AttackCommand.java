package com.frismos.unicorn.patterns;

import com.badlogic.gdx.Gdx;
import com.frismos.unicorn.actor.Bullet;
import com.frismos.unicorn.actor.MainCharacter;
import com.frismos.unicorn.enums.ColorType;

/**
 * Created by eavanyan on 3/14/16.
 */
public class AttackCommand implements Command {

    private MainCharacter character;

    private float x, y;
    private float fireTimer;
    private boolean fire;
    private int bulletsShoot = 0;

    public AttackCommand(MainCharacter character) {
        this.character = character;
    }

    public void setTouchCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void fireBullet(float x, float y) {
        if(character.colorType != null) {
            Bullet bullet = character.getNextBullet();
            bullet.destPoint.x = x;
            bullet.destPoint.y = y;
            bullet.destPoint = character.gameStage.screenToStageCoordinates(bullet.destPoint);
            bullet.setX(character.getFirePoint().x + character.getX());
            bullet.setY(character.getFirePoint().y + character.getY());
            float angle = bullet.calculateAngle();
            ColorType colorType = character.colorType;
            bullet.isSubBullet = false;
            bullet.isHit = false;
            bullet.mark = false;
            if(bulletsShoot > 1) {
                if(bulletsShoot == 2) {
                    bullet.setAngle(angle + 12.5f);
                } else if(bulletsShoot == 3) {
                    bullet.setAngle(angle - 12.5f);
                } else if(bulletsShoot == 4) {
                    bullet.setAngle(angle + 25);
                } else if(bulletsShoot == 5) {
                    bullet.setAngle(angle - 25);
                }
                bullet.isSubBullet = true;
//                colorType = ColorType.RAINBOW;
                bullet.skeletonActor.getSkeleton().getRootBone().setScale(1f);
            } else {
                float scale = 1.5f;
                int combo = (int)(character.combo / MainCharacter.COMBO_VALUE);
                if(combo > 29) {
                    scale = 1.5f;
                } else if(combo > 14) {
                    scale = 1.25f;
                } else if(combo > 8) {
                    scale = 1.0f;
                }else if(combo > 4) {
                    scale = 0.75f;
                } else {
                    scale = 0.5f;
                }
//                if (scale > 0.5f) {
//                    scale = 0.5f;
//                }
                bullet.skeletonActor.getSkeleton().getRootBone().setScale(scale);

            }
            bullet.setColorType(colorType);
            bullet.fire();
            character.gameStage.collisionDetector.addListener(bullet);
            character.gameStage.addActor(bullet);
        }
    }

    public void update() {
        fireTimer += Gdx.graphics.getDeltaTime();
        if(fire) {
            if (fireTimer >= character.attackSpeed) {
                ++bulletsShoot;
                character.playFireAnimation(x, y);
                fireBullet(x, y);
                if (character.bulletsToShootCount == 1) {
                    fireTimer = 0.0f;
                } else if (character.bulletsToShootCount == 2) {
                    fireTimer = character.attackSpeed - 0.05f;
                }
                if (bulletsShoot >= character.bulletsToShootCount) {
                    fire = false;
                    bulletsShoot = 0;
                    fireTimer = 0.0f;
                }
            }
        }
    }

    @Override
    public void execute() {
        fire = true;
    }

    public void cancelTask() {
        fire = false;
    }
}
