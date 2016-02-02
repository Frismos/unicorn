package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgar on 12/9/2015.
 */
public abstract class Creature extends GameActor {

    protected float moveSpeed = 10.0f;

    public float hitPoints;
//    protected Array<Label> hitLabels = new Array<Label>();

    private ProgressBar pb;
    private int hitLabelIndex = 0;

    public Creature(GameStage stage, ColorType colorType) {
        super(stage, colorType);
    }

    protected void showProgressBar() {
        if(hitPoints > 1) {
            if (pb == null) {
                pb = new ProgressBar(gameStage, 3, hitPoints);
            }
            gameStage.addActor(pb);
            pb.setX(getX());
            pb.setY(getY());
        }
    }

    public void hit(float damage) {
//        if(hitLabels.size > 0) {
//            final Label hitLabel = hitLabels.get(hitLabelIndex);
//            if (hitLabel.hasParent()) {
//                hitLabel.clearActions();
//            }
//            hitLabel.setText(damage + "");
//            hitLabel.setPosition(pb.getX(), pb.getY());
//            gameStage.addActor(hitLabel);
//            hitLabel.addAction(Actions.sequence(Actions.moveBy(-moveSpeed * 0.25f, 3, 0.25f), Actions.run(new Runnable() {
//                @Override
//                public void run() {
//                    hitLabel.setPosition(pb.getX(), pb.getY());
//                }
//            })));
//            hitLabelIndex++;
//            if (hitLabelIndex >= hitLabels.size) {
//                hitLabelIndex = 0;
//            }
//        }

        if(gameStage.game.tutorialManager.isTutorialMode && gameStage.game.tutorialManager.currentStep == TutorialStep.FINISH) {
            gameStage.game.tutorialManager.removeArrow();
            gameStage.game.tutorialManager.isTutorialMode = false;
            gameStage.game.tutorialManager.pauseGame = false;
        }
        gameStage.score++;
        gameStage.scoreLabel.setText(String.format("score: %s", String.valueOf(gameStage.score)));
        hitPoints -= damage;
        if(hitPoints <= 0) {
            die();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(pb != null) {
            pb.setProgress(hitPoints);
            pb.setPosition(getX() + getWidth() * getScaleX() / 2 - pb.getWidth() / 2, getY() + getHeight() * getScaleY() + 1);
            pb.setZIndex(getZIndex() + 1);
        }
    }

    @Override
    public boolean remove(boolean dispose) {
        if(pb != null) {
            pb.remove(dispose);
        }
        return super.remove(dispose);
    }

    @Override
    public void actorAddedToStage() {
        showProgressBar();
        super.actorAddedToStage();
    }

    public abstract void die();
}
