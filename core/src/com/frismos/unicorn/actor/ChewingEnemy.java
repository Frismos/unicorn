package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 4/8/16.
 */
public class ChewingEnemy extends WalkingEnemy {
    public ChewingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);

        moveSpeed = INITIAL_MOVE_SPEED + gameStage.game.aiManager.waveIndexForEscalation / 3;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.CHEWING_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.WALKING_ENEMY_SCALE_RATIO;
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        if(skeletonActor.getAnimationState().getTracks().size > 1 &&
                skeletonActor.getAnimationState().getCurrent(1) != null &&
                skeletonActor.getAnimationState().getCurrent(1).getAnimation().getName().equals("chewing") &&
                !skeletonActor.getAnimationState().getCurrent(1).isComplete()) {
            super.hit(damage, bullet);
        } else {
            swallowBullet(bullet);
        }
    }

    @Override
    public void playDieSound() {
        gameStage.game.soundManager.playMusic(SoundManager.EXPLODE, Sound.class, true);
    }

    @Override
    public void eatWall() {
        if(!attackAnim) {
            gameStage.game.soundManager.playMusic(SoundManager.WALKING_ENEMY_HIT, Sound.class, true);
        } else {
            gameStage.game.soundManager.playMusic(SoundManager.EYE_BEAT, Sound.class, true);
        }
    }

    public void swallowBullet(Bullet bullet) {
        gameStage.game.soundManager.playMusic(SoundManager.CHEWING_ENEMY, Sound.class, true);
        gameStage.game.soundManager.playMusic(SoundManager.CHEWING_ENEMY_CHEW, Sound.class, true);
        bullet.isHit = true;
        skeletonActor.getAnimationState().setAnimation(1, "chewing", false);
        skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(int trackIndex, int loopCount) {
                if(trackIndex==1) {
                    skeletonActor.getAnimationState().setAnimation(1, "free", false);
                    skeletonActor.getAnimationState().removeListener(this);
                }
                super.complete(trackIndex, loopCount);
            }
        });
    }
}
