package com.frismos.unicorn.actor;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

import java.util.ArrayList;

/**
 * Created by edgar on 12/14/2015.
 */
public class MotherBoss extends Boss {

    private int waveIndex;
    private WaveType waveType;

    private ArrayList<Enemy> sons = new ArrayList<>();

    public MotherBoss(GameStage gameStage, ColorType colorType, boolean isTutorial) {
        super(gameStage, colorType, isTutorial);
        TIME_STEP = 0.1f;
        FIRE_CHANCE = 100;
        if(isTutorial) {
            maxHitPoints = hitPoints = 25;
        } else {
            maxHitPoints = hitPoints = 100;
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
    }

    @Override
    public void fireEvent() {
        final Enemy enemySonOfMother;
        enemySonOfMother = new BossSon(gameStage, ColorType.getRandomColor());
//        enemy.hitPoints = 2;
        enemySonOfMother.hideProgressBar();
        enemySonOfMother.isSonOfABoss = true;
        gameStage.addActor(enemySonOfMother);
//        sons.add(enemySonOfMother);
        gameStage.collisionDetector.addListener(enemySonOfMother);
        enemySonOfMother.setZIndex(getZIndex() + 1);
    }

    @Override
    public void attack() {
        TIME_STEP += TIME_STEP;
        if(TIME_STEP > 5f) {
            TIME_STEP = 0.3f;
        }
        if(isAttackAnimationPlaying) {
            fireEvent();
        }
        super.attack();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.MOTHER_BOSS;
    }

}
