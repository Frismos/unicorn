package com.frismos.unicorn.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.PowerBar;

/**
 * Created by eavanyan on 4/2/16.
 */
public class UIStage extends SimpleStage {

    public Label scoreLabel;
    public Label comboLabel;
    public Label timeLabel;

    private Label.LabelStyle style;
    public BitmapFont font;

    public PowerBar powerBar;

    public UIStage(final UnicornGame game) {
        super(new StretchViewport(1920, 1215), new PolygonSpriteBatch());
        this.game = game;
        addLabels();
    }

    public void addLabels() {
        reset();
        font = game.fontsManager.getFont(100);
        style = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("score: 0", style);
        scoreLabel.setPosition(200, 1050);
        addActor(scoreLabel);

        timeLabel = new Label("0:00", style);
        timeLabel.setPosition(1500, 1050);
        addActor(timeLabel);

        style.fontColor = Color.YELLOW;
        comboLabel = new Label("combo: x0", style);
        comboLabel.setPosition(getWidth() / 2 - comboLabel.getWidth() / 2, 1000);
        comboLabel.setAlignment(Align.center);

        powerBar = new PowerBar(this);
        powerBar.setPosition(getWidth() / 2 - powerBar.getWidth() / 2, 1000);
        addActor(powerBar);
//        addActor(comboLabel);
    }

    public void reset() {
        if(scoreLabel != null) {
            scoreLabel.remove();
        }
        if(timeLabel != null) {
            timeLabel.remove();
        }
        if(comboLabel != null) {
            comboLabel.remove();
        }
        if(powerBar != null) {
            powerBar.remove();
        }
    }
}
