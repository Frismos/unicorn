package com.frismos.unicorn.screen;

import com.badlogic.gdx.Screen;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.SplashActor;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.util.Debug;

/**
 * Created by eavanyan on 4/11/16.
 */
public class SplashScreen implements Screen {

    private UnicornGame game;
    private SplashActor splashActor;

    public SplashScreen(UnicornGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        splashActor = new SplashActor(game.uiScreen.stage);
        game.uiScreen.stage.addActor(splashActor);
    }

    @Override
    public void render(float delta) {

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
