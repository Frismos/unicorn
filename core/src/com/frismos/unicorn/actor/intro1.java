package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Music;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.SimpleStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 6/4/16.
 */
public class Intro1 extends SpineActor {

    public Intro1(final SimpleStage stage) {
        super(stage);
//        stage.game.timerManager.run(10f, new TimerRunnable() {
//            @Override
//            public void run(Timer timer) {
                stage.addActor(Intro1.this);
                skeletonActor.getSkeleton().getRootBone().setScaleX(stage.getWidth() / getWidth());
                skeletonActor.getSkeleton().getRootBone().setScaleY(stage.getHeight() / getHeight());
                startDefaultAnimation();
                stage.game.soundManager.playMusic("music", Music.class, true, false);
//            }
//        });
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.INTRO1;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 1.0f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "animation", false);
        skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateListener() {
            @Override
            public void event(int trackIndex, Event event) {

            }

            @Override
            public void complete(int trackIndex, int loopCount) {
//                stage.game.soundManager.stop();
                new Intro2(stage);
                remove(true);
            }

            @Override
            public void start(int trackIndex) {

            }

            @Override
            public void end(int trackIndex) {

            }
        });
    }
}
