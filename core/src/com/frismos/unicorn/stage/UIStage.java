package com.frismos.unicorn.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.frismos.unicorn.UnicornGame;

/**
 * Created by eavanyan on 4/2/16.
 */
public class UIStage extends SimpleStage {

    public Label scoreLabel;
    public Label comboLabel;

    public UIStage(UnicornGame game) {
        super(new FitViewport(1920, 1215), new PolygonSpriteBatch());
        this.game = game;


        BitmapFont font = game.fontsManager.getFont(100);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        comboLabel = new Label("combo: 0", style);
        comboLabel.setPosition(900, 1000);
        addActor(comboLabel);

        scoreLabel = new Label("score: 0", style);
        scoreLabel.setPosition(100, 1100);
        addActor(scoreLabel);
    }
}
