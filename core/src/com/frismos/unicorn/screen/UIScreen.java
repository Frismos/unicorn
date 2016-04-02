package com.frismos.unicorn.screen;

import com.badlogic.gdx.Screen;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.stage.UIStage;

/**
 * Created by eavanyan on 4/2/16.
 */
public class UIScreen implements Screen {

    public UIStage stage;
    public UnicornGame game;

    public UIScreen(UnicornGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new UIStage(game);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
