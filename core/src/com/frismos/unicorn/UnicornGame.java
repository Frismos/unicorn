package com.frismos.unicorn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.frismos.TweenAccessor.ActorAccessor;
import com.frismos.TweenAccessor.BoneAccessor;
import com.frismos.TweenAccessor.CameraAccessor;
import com.frismos.TweenAccessor.MusicAccessor;
import com.frismos.unicorn.actor.AttackingEnemy;
import com.frismos.unicorn.actor.BouncingEnemy;
import com.frismos.unicorn.actor.MotherBoss;
import com.frismos.unicorn.actor.MotherEnemy;
import com.frismos.unicorn.actor.ShootingBoss;
import com.frismos.unicorn.actor.ShootingEnemy;
import com.frismos.unicorn.actor.Spell;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.actor.WalkingEnemy;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.AtlasManager;
import com.frismos.unicorn.manager.DataManager;
import com.frismos.unicorn.manager.FontsManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.patterns.GameCenterController;
import com.frismos.unicorn.manager.TimerManager;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.manager.Updatable;
import com.frismos.unicorn.patterns.GoogleAnalyticsController;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.screen.UIScreen;
import com.frismos.unicorn.stage.UIStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class UnicornGame extends Game {

	public UIScreen uiScreen;

	public Strings strings;
    public AtlasManager atlasManager;
    public FontsManager fontsManager;
	public DataManager dataManager;
    public TutorialManager tutorialManager;
    public TimerManager timerManager;
	public AIManager aiManager;

	public GameCenterController gameCenterController;
	public GoogleAnalyticsController googleAnalyticsController;
    public TweenManager tweenManager;

	public boolean restartGame = true;

    public Array<Updatable> updatableArray = new Array<>();
	public SoundManager soundManager;
	public InputMultiplexer multiplexer;

	public UnicornGame(ObjectMap<String, Object> controllers) {
		if(controllers != null) {
			if (controllers.containsKey("game_center")) {
				this.gameCenterController = (GameCenterController) controllers.get("game_center");
			}
			if (controllers.containsKey("google_analytics")) {
				this.googleAnalyticsController = (GoogleAnalyticsController) controllers.get("google_analytics");
			}
		}
	}

	@Override
	public void create () {
		if(strings == null) {
			if (this.gameCenterController != null) {
				this.gameCenterController.setKeyWindowRootViewController(Gdx.app);
				this.gameCenterController.login();
			}
			if (googleAnalyticsController != null) {
				googleAnalyticsController.sendEvent("Game Started", "Rainbow Defender", "UnicornGame.class", 0);
			}
			Gdx.input.setCatchBackKey(true);
			strings = new Strings();
			atlasManager = new AtlasManager();
			fontsManager = new FontsManager();
			dataManager = new DataManager();
			tutorialManager = new TutorialManager(this);
			updatableArray.add(tutorialManager);
			soundManager = new SoundManager(this);
			updatableArray.add(soundManager);
			timerManager = new TimerManager();
			aiManager = new AIManager();
			updatableArray.add(timerManager);
			tweenManager = new TweenManager();
			Tween.registerAccessor(Music.class, new MusicAccessor());
			Tween.registerAccessor(Bone.class, new BoneAccessor());
			Tween.registerAccessor(Camera.class, new CameraAccessor());
			Tween.registerAccessor(Actor.class, new ActorAccessor());

			preloadAssets();

			uiScreen = new UIScreen(this);
			uiScreen.show();
			uiScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			setScreen(new GameScreen(this));

			multiplexer = new InputMultiplexer();
			multiplexer.addProcessor(uiScreen.stage);
			multiplexer.addProcessor(((GameScreen)getScreen()).stage);
			Gdx.input.setInputProcessor(multiplexer);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.9f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(Gdx.graphics.getDeltaTime());
        for (int i= 0; i < updatableArray.size; i++) {
            updatableArray.get(i).update(Gdx.graphics.getDeltaTime());
        }
		super.render();
		uiScreen.render(Gdx.graphics.getDeltaTime());

		if(restartGame) {
            getScreen().dispose();
			setScreen(new GameScreen(this));
			multiplexer.addProcessor(((GameScreen)getScreen()).stage);
//			Gdx.input.setInputProcessor(multiplexer);
		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resize (int width, int height) {
		if (uiScreen != null) uiScreen.resize(width, height);
	}

	public void submitScore() {
		//// TODO: 1/15/16 submit player score to game center here
	}

	public void preloadAssets() {
		atlasManager.get("gfx/@atlas/pack.atlas", TextureAtlas.class);
//		atlasManager.get(String.format("gfx/%s/bot.atlas", Strings.FIELD_BACKGROUND), TextureAtlas.class);
//		atlasManager.get(String.format("gfx/%s/bot.atlas", Strings.HALL_BACKGROUND), TextureAtlas.class);
		atlasManager.get(String.format("gfx/%s/bot.atlas", Strings.CRYSTAL_BACKGROUND), TextureAtlas.class);

		SkeletonJson json = atlasManager.getSkeletonJson(ShootingBoss.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.BOSS_SCALE_RATIO);
		SkeletonData skeletonData = atlasManager.getSkeletonData(ShootingBoss.class, String.format("gfx/%s/skeleton.json", Strings.SHOOTING_BOSS), json);
		atlasManager.getAnimationStateData(ShootingBoss.class, skeletonData);

		json = atlasManager.getSkeletonJson(MotherBoss.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.BOSS_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(MotherBoss.class, String.format("gfx/%s/skeleton.json", Strings.MOTHER_BOSS), json);
		atlasManager.getAnimationStateData(MotherBoss.class, skeletonData);

		json = atlasManager.getSkeletonJson(MotherEnemy.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.HEALTHY_ENEMY_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(MotherEnemy.class, String.format("gfx/%s/skeleton.json", Strings.HEALTHY_ENEMY), json);
		atlasManager.getAnimationStateData(MotherEnemy.class, skeletonData);

		json = atlasManager.getSkeletonJson(WalkingEnemy.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.WALKING_ENEMY_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(WalkingEnemy.class, String.format("gfx/%s/skeleton.json", Strings.WALKING_ENEMY), json);
		atlasManager.getAnimationStateData(WalkingEnemy.class, skeletonData);

		json = atlasManager.getSkeletonJson(BouncingEnemy.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.ATTACKING_ENEMY_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(AttackingEnemy.class, String.format("gfx/%s/skeleton.json", Strings.ATTACKING_ENEMY), json);
		atlasManager.getAnimationStateData(AttackingEnemy.class, skeletonData);

		json = atlasManager.getSkeletonJson(BouncingEnemy.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.BOUNCING_ENEMY_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(BouncingEnemy.class, String.format("gfx/%s/skeleton.json", Strings.BOUNCING_ENEMY), json);
		atlasManager.getAnimationStateData(BouncingEnemy.class, skeletonData);

		json = atlasManager.getSkeletonJson(ShootingEnemy.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.SHOOTING_ENEMY_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(ShootingEnemy.class, String.format("gfx/%s/skeleton.json", Strings.SHOOTING_ENEMY), json);
		atlasManager.getAnimationStateData(ShootingEnemy.class, skeletonData);

		json = atlasManager.getSkeletonJson(Unicorn.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.UNICORN_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(Unicorn.class, String.format("gfx/%s/skeleton.json", Strings.UNICORN), json);
		atlasManager.getAnimationStateData(Unicorn.class, skeletonData);

		json = atlasManager.getSkeletonJson(Spell.class, "gfx/@atlas/pack.atlas");
		json.setScale(Constants.SPELL_SCALE_RATIO);
		skeletonData = atlasManager.getSkeletonData(Spell.class, String.format("gfx/%s/skeleton.json", Strings.MOTHER_BOSS), json);
		atlasManager.getAnimationStateData(Spell.class, skeletonData);

		soundManager.playMusic("Color_Pony_Game_Music_Slow_", Sound.class, false);
		soundManager.playMusic(SoundManager.ERROR, Sound.class, false);
		soundManager.playMusic(SoundManager.CHANGE_COLOR, Sound.class, false);
		soundManager.playMusic(SoundManager.KNOCK, Sound.class, false);
		soundManager.playMusic(SoundManager.UNICORN_FIRE, Sound.class, false);
		soundManager.playMusic(SoundManager.FIRE, Sound.class, false);
		soundManager.playMusic(SoundManager.BOSS_FALL, Sound.class, false);
		soundManager.playMusic(SoundManager.BOSS_HIT, Sound.class, false);
		soundManager.playMusic(SoundManager.BOSS_VOICE, Sound.class, false);
		soundManager.playMusic(SoundManager.BOSS_TAIL, Sound.class, false);
		soundManager.playMusic(SoundManager.BOSS_EXPLODE, Sound.class, false);
		soundManager.playMusic(SoundManager.EXPLODE, Sound.class, false);
		soundManager.playMusic(SoundManager.TEETH_CHATTER, Sound.class, false);
		soundManager.playMusic(SoundManager.JUMP, Sound.class, false);
	}
}
