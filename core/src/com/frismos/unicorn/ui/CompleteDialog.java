package com.frismos.unicorn.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.UIStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 4/2/16.
 */
public class CompleteDialog extends SpineActor {

    public CompleteDialog(final UIStage stage) {
        super(stage);
        setPosition(stage.getWidth() / 2 - getWidth() / 2, stage.getHeight() / 2 - getHeight() / 2);
        debug();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                CompleteDialog.this.skeletonActor.getAnimationState().setAnimation(0, "hide", false);
                CompleteDialog.this.skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
                    @Override
                    public void complete(int trackIndex, int loopCount) {
                        CompleteDialog.this.remove();
                        ((GameScreen)stage.game.getScreen()).stage.restartGame();
                        CompleteDialog.this.skeletonActor.getAnimationState().removeListener(this);

                        super.complete(trackIndex, loopCount);
                    }
                });
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.COMPLETE_DIALOG;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.5f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "show", false);
    }
}
