package com.frismos.unicorn.actor;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.esotericsoftware.spine.Bone;
import com.frismos.TweenAccessor.BoneAccessor;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/10/2015.
 */
public class ShootingEnemy extends Enemy {

    protected float accumulator = 2;
    protected float FIRE_CHANCE = 80;
    private boolean fire = false;
    private Bullet bullet;

    public ShootingEnemy(GameStage stage, ColorType colorType) {
        this(stage, colorType, false);
    }

    public ShootingEnemy(GameStage stage, ColorType colorType, boolean isTutorial) {
        super(stage, colorType, isTutorial);
        maxHitPoints = hitPoints = AIManager.SHOOTING_ENEMY_HP;
        showProgressBar();
        TIME_STEP = 2.0f;
    }

    @Override
    protected void startDefaultAnimation() {
        if(!(this instanceof Boss)) {
            gameStage.game.soundManager.playMusic(SoundManager.FIRING_LAUGH, Sound.class, true);
            if (MathUtils.randomBoolean()) {
                skeletonActor.getAnimationState().setAnimation(0, "walk1", true);
            } else {
                skeletonActor.getAnimationState().setAnimation(0, "walk", true);
            }
        }
    }

    @Override
    public void attack() {
        if(isAttacking) {
            skeletonActor.getAnimationState().setTimeScale(1.0f);
            if (!(gameStage.boss != null && !isSonOfABoss) || this instanceof Boss) {
                spawnPoint.x = gameStage.unicorn.getX();
                spawnPoint.y = MathUtils.random(gameStage.unicorn.getY(), gameStage.unicorn.getY() + gameStage.unicorn.getHeight());

                if (!(this instanceof Boss)) {
                    Bone gunBone = skeletonActor.getSkeleton().findBone("iron-man");
                    skeletonActor.getAnimationState().setAnimation(1, "attack", false);
                    float angle = (float) Math.toDegrees(Math.atan2(spawnPoint.x - gunBone.getWorldX() - getX(), spawnPoint.y - gunBone.getWorldY() - getY()));
                    angle = 90 - angle;

                    gunBone.setRotation(angle);
                    Tween.to(gunBone, BoneAccessor.ROTATION, 0.5f).targetRelative(-angle + 180).start(gameStage.game.tweenManager);

                }
                fire = true;

                bullet = new Bullet(gameStage, spawnPoint.x, spawnPoint.y, ActorDataType.ENEMY_BULLET);
                bullet.setColorType(ColorType.RAINBOW);
                gameStage.game.soundManager.playMusic(SoundManager.FIRE, Sound.class, true);
            }
        }
    }

    @Override
    public void wallAttackingAnimation() {
        super.wallAttackingAnimation();
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.SHOOTING_ENEMY_SCALE_RATIO;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.SHOOTING_ENEMY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAttacking && !isEating && !isWaiting) {
            if(fire) {
                fire = false;
                bullet.fire();
                gameStage.collisionDetector.addListener(bullet);
                gameStage.addActor(bullet);
                float offsetX = skeletonActor.getSkeleton().findBone("center-spawn").getWorldX();
                float offsetY = skeletonActor.getSkeleton().findBone("center-spawn").getWorldY();

                bullet.setX(getX() + offsetX);
                bullet.setY(getY() + offsetY);
                bullet.calculateAngle();
            }
            float startX = this instanceof Boss ? getX() : getX() + getWidth();
            if(startX < gameStage.background.getWidth() + gameStage.background.getZero().x) {
                if (!gameStage.game.tutorialManager.isTutorialMode || !gameStage.game.tutorialManager.pauseGame) {
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
    }
}
