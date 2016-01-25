package com.frismos.unicorn.manager;

import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;

/**
 * Created by edgaravanyan on 1/18/16.
 */
public class TutorialManager {

    public boolean isTutorialMode;
    public boolean pauseGame;
    public TutorialStep currentStep;
    public boolean isTutorialEnemyOnStage;

    public boolean isFirstEnemySend;
    public ColorType firstStepColor = ColorType.YELLOW;

    public boolean isSecondEnemySend;
    public int secondStepEnemies;
    public ColorType secondStepColor = ColorType.GREEN;

    public boolean isThirdEnemySend;
    public boolean isThirdEnemiesInit;
    public int thirdStepEnemies;
    public ColorType thirdStepColor = ColorType.RED;

    public static final int SECOND_STEP_ENEMIES_COUNT = 3;
    public static final float SECOND_STEP_ENEMIES_SEND_TIME = 2f;

    public static final int THIRD_STEP_ENEMIES_COUNT = 5;
    public static final float THIRD_STEP_ENEMIES_SEND_TIME = 0.1f;

    public Array<Enemy> enemies;

    private UnicornGame game;

    public TutorialManager(UnicornGame game) {
        this.game = game;
        isTutorialMode = false;//s!game.dataManager.isTutorialPassed();
        if(isTutorialMode) {
            currentStep = TutorialStep.FIRST;
            enemies = new Array<Enemy>();
        }
    }

}
