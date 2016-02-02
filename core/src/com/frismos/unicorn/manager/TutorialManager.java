package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

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
    private Image arrowImage;

    public TutorialManager(UnicornGame game) {
        this.game = game;
        isTutorialMode = !game.dataManager.isTutorialPassed();
        if(isTutorialMode) {
            currentStep = TutorialStep.FIRST;
            enemies = new Array<>();
            arrowImage = new Image(new Texture(Gdx.files.internal("gfx/Arrow_down.png")));
            arrowImage.setScale(0.01f);
        }
    }

    public void showSlideArrows(GameStage gameStage) {
        Image arrow = new Image(new Texture(Gdx.files.internal("gfx/Arrow_down.png")));
        arrow.setX(5);
        arrow.setY(6);
        arrow.setScale(0.01f);
        gameStage.addActor(arrow);

        Image arrow1 = new Image(new Texture(Gdx.files.internal("gfx/Arrow_down.png")));
        arrow1.setX(5);
        arrow1.setY(26);
        arrow1.setScale(0.01f, -0.01f);
        gameStage.addActor(arrow1);
    }

    public void showArrowOnActor(final Actor actor) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(actor.getX() + (actor.getWidth() * actor.getScaleX() - arrowImage.getWidth() * arrowImage.getScaleX()) / 2);
            arrowImage.setY(actor.getY() + actor.getHeight() + 2);
            actor.getParent().addActor(arrowImage);
            arrowImage.addAction(Actions.repeat(Integer.MAX_VALUE, Actions.sequence(Actions.moveBy(0, -actor.getHeight() / 2, 0.4f, Interpolation.sine), Actions.run(new Runnable() {
                @Override
                public void run() {
                    arrowImage.addAction(Actions.moveBy(0, actor.getHeight() / 2, 0.4f, Interpolation.sine));
                }
            }))));
        }
    }

    public boolean removeArrow() {
        return arrowImage.remove();
    }
}
