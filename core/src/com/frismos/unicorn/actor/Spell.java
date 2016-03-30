package com.frismos.unicorn.actor;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;

/**
 * Created by edgar on 12/14/2015.
 */
public abstract class Spell extends GameActor {

    public SpellType spellType;
    private boolean isClaimed;

    public Spell(GameStage stage) {
        super(stage, ColorType.getRandomColor());
        setUserObject(ActorDataType.SPELL);
        gameStage.game.timerManager.run(10f, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                if(!isClaimed) {
                    skeletonActor.getAnimationState().setAnimation(0, "expire", false);
                    skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateListener() {
                        @Override
                        public void event(int trackIndex, Event event) {

                        }

                        @Override
                        public void complete(int trackIndex, int loopCount) {
                            remove();
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
        });
    }

    public void playClaimAnimation() {
        isClaimed = true;
        gameStage.collisionDetector.removeListenerActor(this);
        skeletonActor.getAnimationState().setAnimation(0, "claim", false);
        skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateListener() {
            @Override
            public void event(int trackIndex, Event event) {

            }

            @Override
            public void complete(int trackIndex, int loopCount) {
                remove();
            }

            @Override
            public void start(int trackIndex) {

            }

            @Override
            public void end(int trackIndex) {

            }
        });
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "idle", true);
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.SPELL_SCALE_RATIO;
    }
}
