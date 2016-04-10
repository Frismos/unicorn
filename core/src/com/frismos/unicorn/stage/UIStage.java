package com.frismos.unicorn.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.util.Timer;

/**
 * Created by eavanyan on 4/2/16.
 */
public class UIStage extends SimpleStage {

    public Label scoreLabel;
    public Label comboLabel;
    public Label timeLabel;

    public UIStage(final UnicornGame game) {
        super(new FitViewport(1920, 1215), new PolygonSpriteBatch());
        this.game = game;
        addLabels();
    }

    public void addLabels() {
        BitmapFont font = game.fontsManager.getFont(100);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("score: 0", style);
        scoreLabel.setPosition(100, 1050);
        addActor(scoreLabel);

        timeLabel = new Label("0:00", style);
        timeLabel.setPosition(1700, 1050);
        addActor(timeLabel);

        style.fontColor = Color.YELLOW;
        comboLabel = new Label("combo: x0", style);
        comboLabel.setPosition(getWidth() / 2 - comboLabel.getWidth() / 2, 1000);
        comboLabel.setAlignment(Align.center);
        addActor(comboLabel);
    }
}
