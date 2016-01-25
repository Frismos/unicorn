package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.userdata.BossUserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.WorldUtils;

/**
 * Created by edgar on 12/14/2015.
 */
public class MotherBoss extends Boss {

    private AnimationState.AnimationStateListener attackAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
            final Enemy enemy;
            if(MathUtils.random(100) < 10 * gameStage.level) {
                enemy = new ShootingEnemy(gameStage, WorldUtils.createEnemy(), ColorType.getRandomColor());
            } else {
                enemy = new WalkingEnemy(gameStage, WorldUtils.createEnemy(), ColorType.getRandomColor());
            }
            enemy.isSonOfABoss = true;
            gameStage.addActor(enemy);
            float enemyY = enemy.getY();
            enemy.setScale(0);
            enemy.isAttacking = false;
            enemy.setX(getX() + getWidth() * getScaleX() / 2);
            enemy.setY(getY() + getHeight() * getScaleY() / 2);
            float speed = 16.469818f;
            float time = (float)Math.sqrt(Math.pow(enemy.getX() - (getX() - enemy.getWidth()), 2) + Math.pow(enemy.getY() - enemyY, 2)) / speed;
            enemy.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(getX() - enemy.getWidth(), enemyY, time), Actions.scaleTo(1, 1, time + 0.1f)), Actions.run(new Runnable() {
                @Override
                public void run() {
                    enemy.isAttacking = true;
                }
            })));
            gameStage.collisionDetector.collisionListeners.add(enemy);
            enemy.setZIndex(gameStage.background.getZIndex() + 1);
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            animationState.removeListener(this);
            animationState.setAnimation(0, "walk", true);
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public MotherBoss(GameStage stage, BossUserData userData, ColorType colorType) {
        this(stage, userData, colorType, false);
    }

    public MotherBoss(GameStage gameStage, BossUserData boss, ColorType colorType, boolean isTutorial) {
        super(gameStage, boss, colorType, isTutorial);
        TIME_STEP = 2.0f;
        FIRE_CHANCE = 80;
        if(isTutorial) {
            hitPoints = 25;
        } else {
            hitPoints = 50;
        }
    }

    @Override
    public void attack() {
        animationState.setAnimation(0, "attack", false);
        animationState.clearListeners();
        animationState.addListener(attackAnimationListener);
    }

}
