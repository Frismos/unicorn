package com.frismos.unicorn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.frismos.unicorn.manager.AtlasManager;
import com.frismos.unicorn.manager.FontsManager;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

public class UnicornGame extends Game {

	public Strings strings;
    public AtlasManager atlasManager;
    public FontsManager fontsManager;

	@Override
	public void create () {
		Debug.Log("CREATE");
		Gdx.input.setCatchBackKey(true);
        strings = new Strings();
        atlasManager = new AtlasManager();
        fontsManager = new FontsManager();
        setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}
}
