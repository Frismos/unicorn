package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Timer;
import com.frismos.unicorn.util.Utils;

import java.util.Observer;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public abstract class MainCharacter extends Creature implements Observer {

    public static final float MAX_HIT_POINTS = 3;
    public static final float COMBO_VALUE = 0.12f;
    public float AUTO_ATTACK_SPEED = 0.2f;
    public float SINGLE_ATTACK_SPEED = 0.1f;
    public float CANNON_ATTACK_SPEED = 0.9f;

    public Tile tile;
    private int colorTypeIndex = 0;
    private float touchDownX;
    protected float touchX = Float.MIN_VALUE;
    protected float touchY = Float.MIN_VALUE;
    protected float bulletAngle;

    public int positionY;
    public boolean isFiring;

    public UnicornType unicornType;
    public int nextBulletIndex = 0;
    public Array<Bullet> gameBullets = new Array<>();

    public float combo = COMBO_VALUE;
    public ColorType currentComboColor;
    public final static float COMBO_TIME = 4f;

    private static Array<Enemy> positionChangeListeners = new Array<>();

    private Timer comboTimer;

    protected AnimationState.AnimationStateListener fireAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {

        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().removeListener(this);
            skeletonActor.getAnimationState().setAnimation(0, "idle", true);
            isFiring = false;
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    private Vector2 firePoint;
    protected Array<Integer> directions;
    private Runnable changeEnemyDirection = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < positionChangeListeners.size; i++) {
                positionChangeListeners.get(i).changeDirection();
            }
        }
    };

    public float attackSpeed;
    private Bone fireBone;
    public int bulletsToShootCount = 1;

    public MainCharacter(GameStage stage, UnicornType unicornType) {
        super(stage, ColorType.values()[0]);

        setUnicornType(unicornType);
        setColorType(this.colorType);
        if(unicornType == UnicornType.RHINO) {
            attackSpeed = CANNON_ATTACK_SPEED;
        } else if(unicornType == UnicornType.STAR) {
            attackSpeed = SINGLE_ATTACK_SPEED;
        } else if(unicornType == UnicornType.UNICORN) {
            attackSpeed = AUTO_ATTACK_SPEED;
        }

        positionY = 0;
        tile = gameStage.grid.grid[0][0];
        this.setPosition(gameStage.colorsPlatform.positions.get(positionY).x, gameStage.colorsPlatform.positions.get(positionY).y);
        positionChanged();
        maxHitPoints = hitPoints = MAX_HIT_POINTS;
        showProgressBar();
//        enableRainbowMode();
        setUserObject(ActorDataType.UNICORN);
        fireBone = skeletonActor.getSkeleton().findBone("center-spawn");

        comboTimer = gameStage.game.timerManager.loop(COMBO_TIME, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
//                if(combo > 0) {
//                    combo -= COMBO_VALUE;
//                }
//                int combo = (int)(MainCharacter.this.combo / COMBO_VALUE);
//                gameStage.comboLabel.setText(String.format("combo x%d", combo));
            }
        });
    }

    @Override
    protected void startDefaultAnimation() {
//        skeletonActor.getAnimationState().setAnimation(0, "idle", true); TODO uncomment this when animation is ready
    }

    public void setUnicornType(UnicornType unicornType) {
        this.unicornType = unicornType;
    }

    public void addPositionChangeListener(final Enemy enemy) {
        if(!positionChangeListeners.contains(enemy, false)) {
            positionChangeListeners.add(enemy);
            enemy.changeDirection();
            enemy.moveSpeed *= 3;
        }
    }

    public void removePositionChangeListener(Enemy enemy) {
        positionChangeListeners.removeValue(enemy, false);
    }

    public void playFireAnimation(float x, float y) {
        skeletonActor.getAnimationState().setAnimation(0, "attack", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(fireAnimationListener);
        skeletonActor.getSkeleton().updateWorldTransform();
    }

    public void moveUp(float velocity) {
        if(tile != null) {
            if(gameStage.game.tutorialManager.isTutorialMode) {
                if(gameStage.game.tutorialManager.isTutorialEnemyOnStage) {
                    if (gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST) {
                        if (colorType == gameStage.game.tutorialManager.firstStepColor) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.SECOND) {
                        if (colorType == gameStage.game.tutorialManager.enemies.get(0).colorType) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.THIRD) {
                        if (colorType == gameStage.game.tutorialManager.thirdStepColor) {
                            return;
                        }
                    }
                }
                gameStage.game.tutorialManager.removeArrow();
            }
            tile.character = null;
//            Debug.log("position y = " + positionY);
            if (gameStage.grid.isTileInsideGrid(tile.i + 1, tile.j)) {
                tile = gameStage.grid.grid[tile.i + 1][tile.j];
                positionY++;
            }


            setColorType(tile.colorType);

            //tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f),
            updatePosition();
        }
    }

    public void moveDown(float velocity) {
        if(tile != null) {
            if(gameStage.game.tutorialManager.isTutorialMode) {
                if(gameStage.game.tutorialManager.isTutorialEnemyOnStage) {
                    if (gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST) {
                        if (colorType == gameStage.game.tutorialManager.firstStepColor) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.SECOND) {
                        if (colorType == gameStage.game.tutorialManager.enemies.get(0).colorType) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.THIRD) {
                        if (colorType == gameStage.game.tutorialManager.thirdStepColor) {
                            return;
                        }
                    }
                }
                gameStage.game.tutorialManager.removeArrow();
            }
            tile.character = null;
//            Debug.log("position y = " + positionY);
            if(gameStage.grid.isTileInsideGrid(tile.i - 1, tile.j)) {
                tile = gameStage.grid.grid[tile.i - 1][tile.j];
                positionY--;
            }

            setColorType(tile.colorType);

//            this.addAction(Actions.sequence(Actions.moveTo(tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f),
//                    Actions.run(changeEnemyDirection)));
            updatePosition();
        }
    }

    public void moveLeft(float velocity) {
        if (tile != null) {
            tile.character = null;
            int j = tile.j - 1;
            if(velocity < -GameStage.VELOCITY_HORIZONTAL_FORCE) {
                j--;
            }
//        animationState.setAnimation(0, "move", false);
            if (gameStage.grid.isTileInsideGrid(tile.i, j)) {
                tile = gameStage.grid.grid[tile.i][j];
            } else if(gameStage.grid.isTileInsideGrid(tile.i, j + 1)) {
                tile = gameStage.grid.grid[tile.i][j + 1];
            }

            setColorType(tile.colorType);

            this.addAction(Actions.moveTo(tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f));
        }
    }

    public void moveRight(float velocity) {
        if(tile != null) {
            tile.character = null;
//        animationState.setAnimation(0, "move", false);
            int j = tile.j + 1;
            if(velocity > GameStage.VELOCITY_HORIZONTAL_FORCE) {
                j++;
            }
            if (gameStage.grid.isTileInsideGrid(tile.i, j)) {
                tile = gameStage.grid.grid[tile.i][j];
            } else if(gameStage.grid.isTileInsideGrid(tile.i, j - 1)) {
                tile = gameStage.grid.grid[tile.i][j - 1];
            }

            setColorType(tile.colorType);

            this.addAction(Actions.moveTo(tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f));
        }
    }

    public void updatePosition() {
        setY(gameStage.colorsPlatform.positions.get(positionY).y);
        changeEnemyDirection.run();
        gameStage.attackCommand.cancelTask();
        gameStage.colorsPlatform.setColorType(colorType);
    }

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    public Vector2 getFirePoint() {
        if(firePoint == null) {
            firePoint = new Vector2();
        }
        firePoint.x = fireBone.getWorldX();
        firePoint.y = fireBone.getWorldY();
        return firePoint;
    }

    public void die() {
        gameStage.stopGame = true;
        addAction(Actions.delay(0.3f, Actions.run(new Runnable() {
            @Override
            public void run() {
                gameStage.restartGame = true;
            }
        })));
    }

    public void regenerate() {
        if(hitPoints < maxHitPoints) {
            hitPoints++;
        }
    }

    public abstract void useAbility();
    public abstract Bullet getNextBullet();
    public abstract void reset();

    public void moveTo(Tile tile) {
        if(this.tile != null) {
            if(gameStage.game.tutorialManager.isTutorialMode) {
                if(gameStage.game.tutorialManager.isTutorialEnemyOnStage) {
                    if (gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST) {
                        if (colorType == gameStage.game.tutorialManager.firstStepColor) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.SECOND) {
                        if (colorType == gameStage.game.tutorialManager.enemies.get(0).colorType) {
                            return;
                        }
                    } else if (gameStage.game.tutorialManager.currentStep == TutorialStep.THIRD) {
                        if (colorType == gameStage.game.tutorialManager.thirdStepColor) {
                            return;
                        }
                    }
                }
                gameStage.game.tutorialManager.removeArrow();
            }
            this.tile.character = null;
            this.tile = tile;

            for (int i = 0; i < ColorType.values().length; i++) {
                if(this.tile.colorType == ColorType.values()[i]) {
                    positionY = i;
                    break;
                }
            }
            setColorType(this.tile.colorType);
            updatePosition();
        }
    }

    public void setCombo(float value) {
        int combo = (int)(this.combo / COMBO_VALUE);
        if(combo < 50) {
            this.combo += COMBO_VALUE;
            combo = (int)(this.combo / COMBO_VALUE);
            gameStage.comboLabel.setText(String.format("combo x%d", combo));
            if (combo >= 50) {
                bulletsToShootCount = 3;
//            } else if (combo >= 50) {
//                bulletsToShootCount = 3;
//        } else if(combo > 10) {
//            bulletsToShootCount = 2;
            } else {
                bulletsToShootCount = 1;
            }
        }
        comboTimer.reset();
    }

    private void activateDoubleBullet() {
        bulletsToShootCount = 2;
    }

    public void resetCombo() {
        Gdx.input.vibrate(100);
        int combo = (int)(this.combo / COMBO_VALUE);
        if(combo < 10) {
            this.combo -= COMBO_VALUE;
        } else if(combo < 15){
            this.combo -= COMBO_VALUE * 5;
        } else if (combo < 20) {
            this.combo /= 2;
        } else {
            this.combo /= 3;
        }
        if(this.combo < COMBO_VALUE) {
            this.combo = COMBO_VALUE;
        }
        combo = (int)(this.combo / COMBO_VALUE);
        gameStage.comboLabel.setText(String.format("combo x%d", combo));
        if(combo >= 50) {
            bulletsToShootCount = 3;
//        } else if(combo >= 40) {
//            bulletsToShootCount = 3;
//        } else if(combo > 10) {
//            bulletsToShootCount = 2;
        } else {
            bulletsToShootCount = 1;
        }
    }

    protected void showProgressBar() {
        if(hitPoints > 1) {
            if (progressBar != null) {
                progressBar.remove();
            }
            progressBar = new ProgressBar(gameStage, maxHitPoints, hitPoints);
            gameStage.addActor(progressBar);
            progressBar.setX(getX());
            progressBar.setY(getY());
        }
    }

    public int getCombo() {
        return (int)(this.combo / COMBO_VALUE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(hitPoints < maxHitPoints) {
            hitPoints += 0.05f * delta;
            if(progressBar != null) {
                progressBar.setProgress(hitPoints);
            }
        }
//        if (gameStage.grid.isPointInsideGrid(getX() + getWidth() / 2, getY() + getHeight() / 2 / 2)) {
//            if(tile != null) {
//                tile.character = null;
//            }
//            tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY() + getHeight() / 2 / 2);
//            tile.character = this;
//        }
    }
}
