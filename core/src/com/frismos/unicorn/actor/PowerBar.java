package com.frismos.unicorn.actor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Slot;
import com.frismos.TweenAccessor.BoneAccessor;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.SimpleStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Sine;

/**
 * Created by eavanyan on 4/15/16.
 */
public class PowerBar extends SpineActor {

    private Array<Slot> slots = new Array<>();
    private int lastIndex = 0;
    private float slotY;

    public PowerBar(SimpleStage stage) {
        super(stage);

        skeletonActor.getSkeleton().updateWorldTransform();
        for (int i = 0; i < 4; i++) {
            slots.add(skeletonActor.getSkeleton().findSlot(String.format("%d", i)));
            slots.get(i).getColor().a = 0;
        }
        slotY = slots.get(0).getBone().getY();
    }

    public void setProgress(int index, boolean resetPowerBar) {
        boolean doTweening = slots.get(index).getColor().a == 0;

        if(resetPowerBar) {
            removeSlots(slots.size - 1, index - 1);
        } else if(doTweening) {
            ((GameScreen)stage.game.getScreen()).stage.unicorn.skeletonActor.getAnimationState().setAnimation(1, "comboup", false);
            ((GameScreen)stage.game.getScreen()).stage.unicorn.skeletonActor.getAnimationState().addAnimation(1, "comboup-idle", true,0);

            stage.game.soundManager.playMusic(SoundManager.COIN, Sound.class, true);
            slots.get(index).getColor().a = 1;
            slots.get(index).getBone().setY(slotY);
            float time = 0.25f;
            slots.get(index).getBone().setScale(2);
            Tween.to(slots.get(index).getBone(), BoneAccessor.SCALE, time).target(1.0f).start(stage.game.tweenManager);

            Tween tween = Tween.to(slots.get(index).getBone(), BoneAccessor.ROTATION, time);
            tween = tween.targetRelative(720);
            tween.ease(Sine.OUT);
            tween.start(stage.game.tweenManager);
        }
    }

    private void removeSlots(final int ii, final int index) {
        if(ii == index) {
            return;
        }
        if(slots.get(ii).getColor().a == 0) {
            removeSlots(ii - 1, index);
            return;
        }

        Tween.to(slots.get(ii).getBone(), BoneAccessor.ALPHA, 0.1f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                slots.get(ii).getBone().getData().getColor().a = 1;
            }
        }).start(stage.game.tweenManager);
        Tween.to(slots.get(ii).getBone(), BoneAccessor.POSITION, 0.05f).targetRelative(0, -23f).start(stage.game.tweenManager).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                slots.get(ii).getColor().a = 0;
                slots.get(ii).getBone().setY(slotY);
                removeSlots(ii - 1, index);
            }
        });
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.POWER_BAR;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 1.0f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "animation", true);
    }
}
