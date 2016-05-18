package com.frismos.unicorn.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.PowerBar;
import com.frismos.unicorn.util.Debug;

/**
 * Created by eavanyan on 4/2/16.
 */
public class UIStage extends SimpleStage {

    public Label scoreLabel;
    public Label comboLabel;
    public Label timeLabel;
    public Label defendLabel;

    private Label.LabelStyle style;
    public BitmapFont font;

    public PowerBar powerBar;

    public UIStage(final UnicornGame game) {
        super(new FillViewport(1920, 1215), new PolygonSpriteBatch());
        this.game = game;
        addLabels();
    }

    public void addLabels() {
        reset();

        font = game.fontsManager.getFont(100);
        style = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("Monsters killed: 0", style);
        scoreLabel.setPosition(200, 1050);
        scoreLabel.setFontScale(0.8f);
//        addActor(scoreLabel);

        Vector2 size = screenToStageCoordinates(new Vector2(Gdx.graphics.getWidth() - 10, 0));
        defendLabel = new Label("time", style);
        defendLabel.setPosition(size.x - 400, 1010);
        defendLabel.setFontScale(0.7f);
        addActor(defendLabel);

        timeLabel = new Label("0:00", style);
        timeLabel.setPosition(size.x - 400, 940);
        timeLabel.setFontScale(0.8f);
        addActor(timeLabel);

        powerBar = new PowerBar(this);
        powerBar.setPosition(getWidth() / 2 - powerBar.getWidth() / 2, 1000);
        addActor(powerBar);

        style.fontColor = Color.WHITE;
        comboLabel = new Label("POWER", style);
        comboLabel.setFontScale(0.6f);
        comboLabel.setPosition(powerBar.getX() - 25, powerBar.getY() - powerBar.getHeight() / 8);
        comboLabel.setAlignment(Align.center);
        addActor(comboLabel);
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
