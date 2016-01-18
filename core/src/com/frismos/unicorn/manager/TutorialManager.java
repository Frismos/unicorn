package com.frismos.unicorn.manager;

import com.frismos.unicorn.UnicornGame;
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
    public boolean isSecondEnemySend;

    public static final int SECOND_STEP_ENEMIES_COUNT = 3;

    public int secondStepEnemies;

    private UnicornGame game;

    public TutorialManager(UnicornGame game) {
        this.game = game;
        isTutorialMode = !game.dataManager.isTutorialPassed();
        currentStep = TutorialStep.FIRST;
    }

}
