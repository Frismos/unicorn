package com.frismos.unicorn;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.TweenAccessor.BoneAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.spine.Bone;
import com.frismos.unicorn.manager.*;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.util.Strings;

public class UnicornGame extends Game {

	public Strings strings;
    public AtlasManager atlasManager;
    public FontsManager fontsManager;
	public DataManager dataManager;
    public TutorialManager tutorialManager;

	public GameCenterController gameCenterController;
    public TweenManager tweenManager;

    public UnicornGame(GameCenterController gameCenterController) {
		this.gameCenterController = gameCenterController;
	}

	@Override
	public void create () {
		if(this.gameCenterController != null) {
			this.gameCenterController.setKeyWindowRootViewController(Gdx.app);
			this.gameCenterController.login();
		}

		Gdx.input.setCatchBackKey(true);
        strings = new Strings();
        atlasManager = new AtlasManager();
        fontsManager = new FontsManager();
		dataManager = new DataManager();
        tutorialManager =new TutorialManager(this);
        tweenManager = new TweenManager();
        Tween.registerAccessor(Bone.class, new BoneAccessor());
        setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
        tweenManager.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}

	public void submitScore() {
		//// TODO: 1/15/16 submit player score to gamecenter here
	}
}
