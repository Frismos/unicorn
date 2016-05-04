package com.frismos.unicorn.actor;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class MotherBoss extends Boss {

    private int waveIndex;
    private WaveType waveType;

    private Array<Enemy> sons = new Array<>();

    public MotherBoss(GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 0.1f;
        FIRE_CHANCE = 100;
        if(isTutorial) {
            hitPoints = 25;
        } else {
            hitPoints = 100;
        }
    }

    @Override
    public void setSpawnEnemyType(WaveType type) {
        this.waveType = type;

        int index = -1;
        for (int i = 0; i < waveType.values().length; i++) {
            if(waveType == WaveType.values()[i]) {
                index = i;
                break;
            }
        }
        hitPoints = 10 + index * 2;
//        FIRE_CHANCE = 100 - index * 10;
        this.waveIndex = index;
        showProgressBar();
    }

    @Override
    public void fireEvent() {
        final Enemy enemy;
        enemy = new BossSon(gameStage, ColorType.getRandomColor());
//        enemy.hitPoints = 2;
        enemy.hideProgressBar();
        enemy.isSonOfABoss = true;
        gameStage.addActor(enemy);
        float enemyY = enemy.getY();
        enemy.setScale(0);
        enemy.isAttacking = false;
        enemy.setX(getX() + getWidth() * getScaleX() / 2);
        enemy.setY(getY() + getHeight() * getScaleY() / 2);
        float speed = 16.469818f;
        float time = (float) Math.sqrt(Math.pow(enemy.getX() - (getX() - enemy.getWidth()), 2) + Math.pow(enemy.getY() - enemyY, 2)) / speed;
        sons.add(enemy);
        enemy.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(getX() - enemy.getWidth(), enemyY, time), Actions.scaleTo(1, 1, time + 0.1f)), Actions.run(new Runnable() {
            @Override
            public void run() {
                enemy.isAttacking = true;
            }
        })));
        gameStage.collisionDetector.addListener(enemy);
        enemy.setZIndex(getZIndex() + 1);
    }

    @Override
    public void attack() {
        TIME_STEP += TIME_STEP;
//        if(TIME_STEP > 1.5f) {
//            TIME_STEP = 0.1f;
//        }
        if(isAttackAnimationPlaying) {
            fireEvent();
        }
        super.attack();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.MOTHER_BOSS;
    }

    @Override
    public void die(AnimationState.AnimationStateListener dieListener) {
        super.die(dieListener);
        for (int i = 0; i < sons.size; i++) {
            sons.get(i).die();
        }
    }
}
