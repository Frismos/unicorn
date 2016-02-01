package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Utils;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public abstract class MainCharacter extends Creature {
    public float AUTO_ATTACK_SPEED = 0.05f;
    public float SINGLE_ATTACK_SPEED = 0.7f;
    public float CANNON_ATTACK_SPEED = 0.9f;

    public Tile tile;
    private int colorTypeIndex = 0;
    private float touchDownX;
    protected float touchX = Float.MIN_VALUE;
    protected float touchY = Float.MIN_VALUE;
    protected float bulletAngle;

    private float positionY;
    public boolean isFiring;

    public UnicornType unicornType;
    public int nextBulletIndex = 0;
    public Array<Bullet> gameBullets = new Array<>();

    private static Array<Enemy> positionChangeListeners = new Array<>();

    protected AnimationState.AnimationStateListener fireAnimationListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {
            if(touchX != Float.MIN_VALUE) {
                gameStage.fireBullet(touchX, touchY);
                touchX = Float.MIN_VALUE;
            } else if(bulletAngle != Float.MIN_VALUE){
                gameStage.fireBullet(bulletAngle);
            }
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

    private float maxHitPoints;
    public float attackSpeed;

    public MainCharacter(GameStage stage, UnicornType unicornType) {
        super(stage, ColorType.YELLOW);

        setUnicornType(unicornType);
        setColorType(this.colorType);

        this.setY(gameStage.background.getZero().y);
        positionChanged();
        maxHitPoints = hitPoints = 3;
//        enableRainbowMode();
        setUserObject(ActorDataType.UNICORN);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "idle", true);
    }

    public void setUnicornType(UnicornType unicornType) {
        this.unicornType = unicornType;
        if(unicornType == UnicornType.RHINO) {
            attackSpeed = CANNON_ATTACK_SPEED;
        } else if(unicornType == UnicornType.STAR) {
            attackSpeed = SINGLE_ATTACK_SPEED;
        } else if(unicornType == UnicornType.UNICORN) {
            attackSpeed = AUTO_ATTACK_SPEED;
        }
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
//        if(isFiring) {
        gameStage.fireBullet(x, y);
        touchX = Float.MIN_VALUE;
        bulletAngle = Float.MIN_VALUE;
//        }
//        if(!isFiring) {
//            isFiring = true;
//            this.touchX = x;
//            this.touchY = y;
        this.directions = directions;
        skeletonActor.getAnimationState().setAnimation(0, "fire", false);
        skeletonActor.getAnimationState().clearListeners();
        skeletonActor.getAnimationState().addListener(fireAnimationListener);
//        }
    }

//    public void playFireAnimation(float angle) {
//        touchX = Float.MIN_VALUE;
//        bulletAngle = Float.MIN_VALUE;
////        bulletAngle = angle;
////        if(isFiring) {
//        gameStage.fireBullet(angle);
////            touchX = Float.MIN_VALUE;
//
////        }
////        if(!isFiring) {
////            isFiring = true;
//        skeletonActor.getAnimationState().setAnimation(0, "fire", false);
//        skeletonActor.getAnimationState().clearListeners();
//        skeletonActor.getAnimationState().addListener(fireAnimationListener);
////        }
//    }

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
            }
            tile.character = null;
            if (gameStage.grid.isTileInsideGrid(tile.i + 1, tile.j)) {
                tile = gameStage.grid.grid[tile.i + 1][tile.j];
                positionY++;
            }

            setColorType(tile.colorType);

            this.addAction(Actions.sequence(Actions.moveTo(tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f),
                    Actions.run(changeEnemyDirection)));
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
            }
            tile.character = null;
            if(gameStage.grid.isTileInsideGrid(tile.i - 1, tile.j)) {
                tile = gameStage.grid.grid[tile.i - 1][tile.j];
                positionY--;
            }

            setColorType(tile.colorType);

            this.addAction(Actions.sequence(Actions.moveTo(tile.getX() + gameStage.grid.tileWidth / 2 - getWidth() / 2, tile.getY() + gameStage.grid.tileHeight / 2 - getHeight() / 2, 0.02f),
                    Actions.run(changeEnemyDirection)));
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

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    public Vector2 getFirePoint() {
        if(firePoint == null) {
            firePoint = new Vector2(skeletonActor.getSkeleton().findBone("center-spawn").getWorldX(), skeletonActor.getSkeleton().findBone("center-spawn").getWorldY());
        }
        return firePoint;
    }

    public void die() {
        gameStage.stopGame = true;
        addAction(Actions.delay(0.3f, Actions.run(new Runnable() {
            @Override
            public void run() {
                gameStage.restartGame();
            }
        })));
    }

    public void regenerate() {
        if(hitPoints < maxHitPoints) {
            hitPoints++;
        }
    }

    @Override
    public boolean remove(boolean dispose) {
        return super.remove(dispose);
    }

    public abstract void useAbility();

    @Override
    public void act(float delta) {
        super.act(delta);
        if (gameStage.grid.isPointInsideGrid(getX() + getWidth() / 2, getY() + getHeight() / 2 / 2)) {
            if(tile != null) {
                tile.character = null;
            }
            tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY() + getHeight() / 2 / 2);
            tile.character = this;
        }
    }

    public abstract Bullet getNextBullet();
}
