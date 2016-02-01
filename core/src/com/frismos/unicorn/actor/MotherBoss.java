package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class MotherBoss extends Boss {

    private AnimationState.AnimationStateListener attackAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
            final Enemy enemy;
            if(MathUtils.random(100) < 10 * gameStage.level) {
                enemy = new ShootingEnemy(gameStage, ColorType.getRandomColor());
            } else {
                enemy = new WalkingEnemy(gameStage, ColorType.getRandomColor());
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
            skeletonActor.getAnimationState().removeListener(this);
            skeletonActor.getAnimationState().setAnimation(0, "walk", true);
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public MotherBoss(GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
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
        skeletonActor.getAnimationState().setAnimation(0, "attack", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(attackAnimationListener);
    }


    @Override
    protected void setResourcesPath() {
        path = Strings.MOTHER_BOSS;
    }
}
