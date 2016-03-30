package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 12/9/2015.
 */
public abstract class Creature extends GameActor {

    protected float moveSpeed = 10.0f;

    public float hitPoints;
//    protected Array<Label> hitLabels = new Array<Label>();

    protected ProgressBar progressBar;
    private int hitLabelIndex = 0;

    protected float maxHitPoints;

    public Creature(GameStage stage, ColorType colorType) {
        super(stage, colorType);
    }

    protected void showProgressBar() {
//        if(hitPoints > 1) {
//            if (progressBar != null) {
//                progressBar.remove();
//            }
//            progressBar = new ProgressBar(gameStage, maxHitPoints, hitPoints);
//            gameStage.addActor(progressBar);
//            progressBar.setX(getX());
//            progressBar.setY(getY());
//        }
    }

    public void hit (float damage) {
        hit(damage, null);
    }

    Bullet old;
    public void hit(float damage, Bullet bullet) {
        old = bullet;
        if(bullet == null || !bullet.isHit) {
            if (gameStage.game.tutorialManager.isTutorialMode) {
                if (this instanceof MainCharacter) {
                    return;
                }
                if (gameStage.game.tutorialManager.currentStep == TutorialStep.FINISH) {
                    gameStage.game.tutorialManager.removeArrow();
                    gameStage.game.tutorialManager.isTutorialMode = false;
                    gameStage.game.tutorialManager.pauseGame = false;
                }
            }
//            gameStage.score++;
//            gameStage.comboLabel.setText(String.format("score: %s", String.valueOf(gameStage.score)));// TODO: 3/4/16
            hitPoints -= damage;
            if (hitPoints <= 0) {
                hitPoints = 0;
                die();
            }
            if(this instanceof Enemy && !bullet.isSubBullet) {
                gameStage.unicorn.setCombo(MainCharacter.COMBO_VALUE);
            }
        }
        if(bullet != null) {
            bullet.isHit = true;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(progressBar != null) {
            progressBar.setProgress(hitPoints);
            progressBar.setPosition(getX() + getWidth() * getScaleX() / 2 - progressBar.getWidth() / 2, getY() + getHeight() * getScaleY() - progressBar.getHeight());
            progressBar.setZIndex(getZIndex() + 1);
        }
    }

    @Override
    public boolean remove(boolean dispose) {
        if(progressBar != null) {
            progressBar.remove(dispose);
        }
        return super.remove(dispose);
    }

    @Override
    public void actorAddedToStage() {
        showProgressBar();
        super.actorAddedToStage();
    }

    public abstract void die();

    public void hideProgressBar() {
        if(progressBar != null) {
            progressBar.remove();
            progressBar = null;
        }
    }
}
