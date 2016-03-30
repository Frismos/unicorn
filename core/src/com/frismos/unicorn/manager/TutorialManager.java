package com.frismos.unicorn.manager;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.frismos.TweenAccessor.ActorAccessor;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.Direction;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.stage.GameStage;

import static com.frismos.unicorn.enums.TutorialStep.FIRST;

/**
 * Created by edgaravanyan on 1/18/16.
 */
public class TutorialManager implements Updatable {

    public boolean isTutorialMode;
    public boolean pauseGame;
    public TutorialStep currentStep;
    public boolean isTutorialEnemyOnStage;

    public boolean isFirstEnemySend;
    public ColorType firstStepColor = ColorType.RED;

    public boolean isSecondEnemySend;
    public int secondStepEnemies;
    public ColorType secondStepColor = ColorType.GREEN;

    public boolean isThirdEnemySend;
    public boolean isThirdEnemiesInit;
    public int thirdStepEnemies;
    public ColorType thirdStepColor = ColorType.RED;

    public boolean isFourthStepEnemySend;
    public int fourthStepEnemies;

    public static final int SECOND_STEP_ENEMIES_COUNT = 3;
    public static final float SECOND_STEP_ENEMIES_SEND_TIME = 2f;

    public static final int THIRD_STEP_ENEMIES_COUNT = 5;
    public static final float THIRD_STEP_ENEMIES_SEND_TIME = 0.1f;

    public static final int FOURTH_STEP_ENEMIES_COUNT = 5;

    public Array<Enemy> enemies;

    private UnicornGame game;
    private Image arrowImage;

    public TutorialManager(UnicornGame game) {
        this.game = game;
        isTutorialMode = false;//!game.dataManager.isTutorialPassed();
        if(isTutorialMode) {
            currentStep = FIRST;
            enemies = new Array<>();
            arrowImage = new Image(new Texture(Gdx.files.internal("gfx/Arrow_down.png")));
            arrowImage.setScale(0.01f);
        }
    }

    public void showSlideArrow(GameStage gameStage, Direction direction) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(5);
            arrowImage.setY(10);
            float scaleY = direction == Direction.DOWN ? 0.01f : -0.01f;
            arrowImage.setScale(0.01f, scaleY);
            gameStage.addActor(arrowImage);
        }
    }

    public void showArrowOnActor(final Actor actor) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(actor.getX() + (actor.getWidth() * actor.getScaleX() - arrowImage.getWidth() * arrowImage.getScaleX()) / 2);
            arrowImage.setY(actor.getY() + actor.getHeight() + 2);
            actor.getParent().addActor(arrowImage);
            arrowImage.toFront();
            Tween.to(arrowImage, ActorAccessor.POSITION_XY, 0.2f).targetRelative(0, -actor.getHeight() / 2).repeatYoyo(Tween.INFINITY, 0.01f).ease(Sine.INOUT).start(game.tweenManager);
        }
    }

    public boolean removeArrow() {
        arrowImage.setScale(0.01f);
        game.tweenManager.killTarget(arrowImage);
        return arrowImage.remove();
    }

    @Override
    public void update(float delta) {

    }
}
