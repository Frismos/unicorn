package com.frismos.unicorn.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class GameScreen extends ScreenAdapter {

    public GameStage stage;
    public UnicornGame game;

    public GameScreen(UnicornGame game) {
        this.game = game;
        this.game.restartGame = false;
    }

    @Override
    public void show() {
        stage = new GameStage(game);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
