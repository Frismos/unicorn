package com.frismos.unicorn.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by eavanyan on 4/14/16.
 */
public class TutorialLabel extends Actor {
    private Label label;
    private Image blackImage;

    public TutorialLabel(UnicornGame game) {
        Label.LabelStyle style = new Label.LabelStyle(game.uiScreen.stage.font, Color.WHITE);
        label = new Label("", style);
        blackImage = new Image(GameStage.blackPixel);
        blackImage.setSize(game.uiScreen.stage.getWidth(), game.uiScreen.stage.getHeight() / 5);
        blackImage.setColor(Color.BLACK);
        blackImage.getColor().a = 0.3f;
        label.setSize(blackImage.getWidth(), blackImage.getHeight());
        label.setAlignment(Align.center);
        label.setWrap(true);
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        blackImage.setX(getX());
        blackImage.setY(getY());
        label.setX(getX());
        label.setY(getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        blackImage.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
    }
}
