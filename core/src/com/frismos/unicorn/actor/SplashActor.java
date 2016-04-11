package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.SimpleStage;
import com.frismos.unicorn.stage.UIStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 4/11/16.
 */
public class SplashActor extends SpineActor {
    private UIStage stage;
    public SplashActor(SimpleStage stage) {
        super(stage);
        this.stage = (UIStage)stage;
        stage.game.soundManager.playMusic("colorponymusic", Music.class, true);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hide();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.SPLASH_SCREEN;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 1920 / 3840.0f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "show", false);
    }

    public void hide() {
        stage.game.soundManager.stop();
        stage.game.setScreen(new GameScreen(stage.game));
        skeletonActor.getAnimationState().setAnimation(0, "hide", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(int trackIndex, int loopCount) {
                super.complete(trackIndex, loopCount);
                remove(true);
            }
        });
    }

    public void dispose() {
    }
}
