package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.grid.Grid;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.FontsManager;
import com.frismos.unicorn.stage.GameStage;

import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 12/9/2015.
 */
public abstract class Creature extends GameActor {

    protected float moveSpeed = 10.0f;

    public int hitPoints;
//    protected Array<Label> hitLabels = new Array<Label>();

    private ProgressBar pb;
    private int hitLabelIndex = 0;

    public Creature(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);
    }

    public void showProgressBar() {
        if(pb == null) {
            pb = new ProgressBar(gameStage, 3, hitPoints);
        }
        gameStage.addActor(pb);
        pb.setX(getX());
        pb.setY(getY());

//        BitmapFont font = gameStage.game.fontsManager.getFont(Color.DARK_GRAY, 15);
//        Label.LabelStyle style = new Label.LabelStyle(font, Color.DARK_GRAY);
//
//        style.fontColor = Color.RED;
//        for (int i = 0; i < 5; i++) {
//            hitLabels.add(new Label("1", style));
//            Label hitLabel = hitLabels.get(i);
//            hitLabel.setFontScale(0.1f);
//            hitLabel.setSize(getWidth() / 2, 1);
//            hitLabel.setAlignment(Align.center);
//        }
    }

    public void hit(int damage) {
//        if(hitLabels.size > 0) {
//            final Label hitLabel = hitLabels.get(hitLabelIndex);
//            if (hitLabel.hasParent()) {
//                hitLabel.clearActions();
//                hitLabel.remove();
//            }
//            hitLabel.setText(damage + "");
//            hitLabel.setPosition(pb.getX(), pb.getY());
//            gameStage.addActor(hitLabel);
//            hitLabel.addAction(Actions.sequence(Actions.moveBy(-moveSpeed * 0.25f, 3, 0.25f), Actions.run(new Runnable() {
//                @Override
//                public void run() {
//                    hitLabel.setPosition(pb.getX(), pb.getY());
//                    hitLabel.remove();
//                }
//            })));
//            hitLabelIndex++;
//            if (hitLabelIndex >= hitLabels.size) {
//                hitLabelIndex = 0;
//            }
//        }

        if(gameStage.game.tutorialManager.isTutorialMode && gameStage.game.tutorialManager.currentStep == TutorialStep.FINISH) {
            Debug.Log("creature hit = " + 78);
            gameStage.game.tutorialManager.isTutorialMode = false;
            gameStage.game.tutorialManager.pauseGame = false;
        }
        gameStage.score++;
        gameStage.scoreLabel.setText(String.format("score: %s", String.valueOf(gameStage.score)));
        hitPoints-= damage;
        if(hitPoints <= 0) {
            die();
        } else {
            Color color = getColor();
            addAction(Actions.sequence(Actions.color(Color.BLACK, 0.2f), Actions.color(color, 0.2f)));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(pb != null) {
            pb.setProgress(hitPoints);
            pb.setPosition(getX() + getWidth() * getScaleX() / 2 - pb.getWidth() / 2, getY() + getHeight() * getScaleY() + 1);
            pb.setZIndex(getZIndex() + 1);
        }
    }

    public void dispose() {
        if(pb != null) {
            pb.remove();
        }
    }

    public abstract void die();
}
