package com.frismos.unicorn.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class GameScreen extends ScreenAdapter {

    private GameStage stage;
    public UnicornGame game;

    public GameScreen(UnicornGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new GameStage(game);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        if (stage.restartGame) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
