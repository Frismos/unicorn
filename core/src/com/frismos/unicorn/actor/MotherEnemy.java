package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 3/18/16.
 */
public class MotherEnemy extends WalkingEnemy {

    public MotherEnemy(GameStage stage,  ColorType colorType,  boolean isTutorial) {
        this(stage, colorType);
        this.isTutorialEnemy = isTutorial;
    }

    public MotherEnemy(GameStage stage, ColorType colorType) {
        super(stage, colorType);
        hitPoints = AIManager.MOTHER_ENEMY_HP;
        skeletonActor.getAnimationState().getData().setMix("attack", "walk", 0.1f);
        skeletonActor.getAnimationState().getData().setMix("walk", "attack", 0.1f);
    }

    public void playDieSound() {
        gameStage.game.soundManager.playMusic(SoundManager.EXPLODE, Sound.class, true);
    }

    @Override
    public void eatWall() {
        gameStage.game.soundManager.playMusic(SoundManager.MOTHER_ENEMY_HIT, Sound.class, true);
    }

    @Override
    public void wallAttackingAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "attack", true);
        super.wallAttackingAnimation();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.HEALTHY_ENEMY;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.MOTHER_ENEMY_SCALE_RATIO;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "walk", true);
    }
}
