package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.spine.AnimationState;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 4/8/16.
 */
public class ChewingEnemy extends WalkingEnemy {

    private boolean isHit = false;
    public ChewingEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);

        moveSpeed = INITIAL_MOVE_SPEED + stage.unicorn.getCombo() / 20.0f + (float)Math.sqrt(gameStage.gameTime / 4);
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
        long time = System.currentTimeMillis();
        if(isHit) {
            super.hit(damage, bullet);
        } else {
            isHit = true;
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
        skeletonActor.getAnimationState().setAnimation(1, "chewing", true);
    }
}
