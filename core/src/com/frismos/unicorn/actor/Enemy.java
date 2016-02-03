package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.*;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Utils;
import sun.font.TrueTypeFont;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public abstract class Enemy extends Creature {

    public Tile tile;

    public boolean isAttackingOnUnicorn = false;
    public boolean isAttacking = true;
    protected Vector2 spawnPoint;

    protected float directionX = 1, directionY = 0;

    protected int positionY;

    public boolean isTutorialEnemy;

    public boolean isSonOfABoss = false; // :D

    private AnimationState.AnimationStateListener dieListener = new AnimationState.AnimationStateListener() {

        @Override
        public void event(int trackIndex, Event event) {
            if(isTutorialEnemy) {
                gameStage.game.tutorialManager.pauseGame = false;
            }
            if (Enemy.this.tile != null) {
                if(Enemy.this.tile.j != 0){
//                    tile.color(colorType);
                }
                tile = null;
            }
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            if(!gameStage.game.tutorialManager.isTutorialMode) {
                int prob = 0;
                if (Enemy.this instanceof Boss) {
                    prob = 100;
                } else if (Enemy.this instanceof AttackingEnemy) {
                    prob = 25;
                } else if (Enemy.this instanceof BouncingEnemy) {
                    prob = 7;
                } else if (Enemy.this instanceof ShootingEnemy) {
                    prob = 3;
                }
                if (MathUtils.random(100) < prob) {
                    gameStage.putSpell(getX(), getY());
                }
            }
            skeletonActor.getAnimationState().removeListener(this);
            remove(true);
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public Enemy(GameStage stage, ColorType colorType) {
        this(stage, colorType, false);
    }

    public Enemy(GameStage stage, ColorType colorType, boolean isTutorialEnemy) {
        super(stage, colorType);

        setColorType(colorType);

        int positionX = GameStage.COLUMN_LENGTH;
        positionY = -1;
        int percent = MathUtils.random(100);
        if(percent <= GameStage.ENEMY_MATCH_CHANCE) {
            if (colorType == ColorType.RED) {
                if (gameStage.grid.nextRedRow != -1) {
                    positionY = gameStage.grid.nextRedRow;
                    gameStage.grid.nextRedRow = -1;
                }
            } else if (colorType == ColorType.GREEN) {
                if (gameStage.grid.nextGreenRow != -1) {
                    positionY = gameStage.grid.nextGreenRow;
                    gameStage.grid.nextGreenRow = -1;
                }
            } else if (colorType == ColorType.BLUE) {
                if (gameStage.grid.nextBlueRow != -1) {
                    positionY = gameStage.grid.nextBlueRow;
                    gameStage.grid.nextBlueRow = -1;
                }
            } else if (colorType == ColorType.YELLOW) {
                if (gameStage.grid.nextYellowRow != -1) {
                    positionY = gameStage.grid.nextYellowRow;
                    gameStage.grid.nextYellowRow = -1;
                }
            }
        }

        if(positionY == -1) {
            int positionYOffset = isTutorialEnemy ? 2 : 1;
            positionY = MathUtils.random(GameStage.ROW_LENGTH - positionYOffset);
        }
        if(gameStage.colorIndices.size > 0) {
            if(colorType == ColorType.YELLOW) {
                positionY = 3;
//            } else if(colorType == ColorType.RED) {
//                positionY = 1;
            } else if(colorType == ColorType.GREEN) {
                positionY = 0;
//            } else if(colorType == ColorType.BLUE) {
//                positionY = 1;
            }
        }
//        if(MathUtils.random(100) <= GameStage.ENEMY_SPAWN_MIDDLE_CHANCE) {
//            positionX = MathUtils.random(3, GameStage.COLUMN_LENGTH - 1);
//        }
        this.setX(gameStage.background.getZero().x + positionX * gameStage.grid.tileWidth + getWidth() / 2);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + getHeight() / 4);
        spawnPoint = new Vector2(0, getY() + getHeight() * getScaleY() / 2);
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        isAttacking = true;
        this.isTutorialEnemy = isTutorialEnemy;
        if(!isTutorialEnemy) {
            moveSpeed = GameStage._ENEMY_MOVE_SPEED;
        }
        setUserObject(ActorDataType.ENEMY);
    }

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void die(AnimationState.AnimationStateListener dieListener) {
        if(isAttacking) {
            if(gameStage.game.tutorialManager.isTutorialMode) {
                gameStage.game.tutorialManager.enemies.removeValue(this, false);
                if(this.isTutorialEnemy) {
                    gameStage.game.tutorialManager.isTutorialEnemyOnStage = false;
                }
                if(TutorialStep.values().length - 1 == gameStage.game.tutorialManager.currentStep.ordinal()) {
                    gameStage.game.tutorialManager.isTutorialMode = false;
                } else {
                    if(gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST) {            //cul
                        gameStage.game.tutorialManager.currentStep = TutorialStep.SECOND;
                    } else if(TutorialStep.SECOND == gameStage.game.tutorialManager.currentStep) {    //kov
                        if(gameStage.game.tutorialManager.secondStepEnemies >= TutorialManager.SECOND_STEP_ENEMIES_COUNT) {
                            if(gameStage.game.tutorialManager.enemies.size == 0) {
                                gameStage.game.tutorialManager.currentStep = TutorialStep.THIRD;
                            }
                        }
                    } else if(TutorialStep.THIRD == gameStage.game.tutorialManager.currentStep) {     //ayc
                        if(isTutorialEnemy && colorType == gameStage.game.tutorialManager.thirdStepColor) {
                            gameStage.game.tutorialManager.currentStep = TutorialStep.FOURTH;
                            gameStage.game.tutorialManager.pauseGame = false;
                            gameStage.changeUnicornType.remove();
                        }
                    } else if(TutorialStep.FOURTH == gameStage.game.tutorialManager.currentStep) {    //xoz
                        if (gameStage.game.tutorialManager.fourthStepEnemies >= TutorialManager.FOURTH_STEP_ENEMIES_COUNT) {
                            if (gameStage.game.tutorialManager.enemies.size == 0) {
                                gameStage.boss = gameStage.sendBoss(true);
                                gameStage.addActor(gameStage.changeUnicornType);
                            }
                        }
                    }
                }
                gameStage.game.tutorialManager.removeArrow();
            } else if(isTutorialEnemy) {
                gameStage.game.tutorialManager.removeArrow();
            }

            gameStage.collisionDetector.collisionListeners.removeValue(this, false);
            gameStage.unicorn.removePositionChangeListener(this);
            if(gameStage.boss == null && !gameStage.game.tutorialManager.isTutorialMode) {
                gameStage.deadEnemyCounter++;
            }
            if (Enemy.this.tile != null) {
                tile.enemies.removeValue(Enemy.this, false);
            }
            isAttacking = false;
            skeletonActor.getAnimationState().setAnimation(0, "die", false);
            if(dieListener == null) {
                dieListener = this.dieListener;
            }
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().addListener(dieListener);
        }
    }

    public void die() {
        die(null);
    }

    public abstract void attack();

    @Override
    public boolean remove() {
        return super.remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAttacking) {
            if(gameStage.grid.isPointInsideGrid(getX(), getY())) {
                if(tile != null) {
                    tile.enemies.removeValue(this, false);
                }
                tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY());
                tile.enemies.add(this);
            }
            if(!gameStage.game.tutorialManager.isTutorialMode || !gameStage.game.tutorialManager.pauseGame) {
                moveBy(-directionX * moveSpeed * delta, -directionY * moveSpeed * delta);  //  23
            }

            if(this.getX() < 7) {
                die(new AnimationState.AnimationStateListener() {
                    @Override
                    public void event(int trackIndex, Event event) {

                    }

                    @Override
                    public void complete(int trackIndex, int loopCount) {
                        gameStage.restartGame = true;
                    }

                    @Override
                    public void start(int trackIndex) {

                    }

                    @Override
                    public void end(int trackIndex) {

                    }
                });
            }

            if(isTutorialEnemy && getX() < gameStage.background.getZero().x + gameStage.background.getWidth() - gameStage.grid.tileWidth) {
                gameStage.game.tutorialManager.pauseGame = true;
                moveSpeed = GameStage._ENEMY_MOVE_SPEED;
                if(gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST ||
                        gameStage.game.tutorialManager.currentStep == TutorialStep.SECOND) {
                    if(gameStage.unicorn.colorType != colorType) {
                        Direction direction;
                        if(colorType.ordinal() > gameStage.unicorn.colorType.ordinal()) {
                            direction = Direction.UP;
                        } else {
                            direction = Direction.DOWN;
                        }
                        gameStage.game.tutorialManager.showSlideArrow(gameStage, direction);
                    } else {
                        gameStage.game.tutorialManager.showArrowOnActor(this);
                    }
                } else if(gameStage.game.tutorialManager.currentStep == TutorialStep.THIRD) {
                    if(gameStage.unicorn.colorType != gameStage.game.tutorialManager.thirdStepColor) {
                        Direction direction;
                        if (gameStage.game.tutorialManager.thirdStepColor.ordinal() > gameStage.unicorn.colorType.ordinal()) {
                            direction = Direction.UP;
                        } else {
                            direction = Direction.DOWN;
                        }
                        gameStage.game.tutorialManager.showSlideArrow(gameStage, direction);
                    } else if(gameStage.unicorn.unicornType != UnicornType.RHINO) {
                        gameStage.game.tutorialManager.showArrowOnActor(gameStage.changeUnicornType);
                    } else {
                        gameStage.game.tutorialManager.showArrowOnActor(gameStage.grid.grid[1][6]);
                    }
                } else if(gameStage.game.tutorialManager.currentStep == TutorialStep.FOURTH ||
                        gameStage.game.tutorialManager.currentStep == TutorialStep.FINISH) {
                    if(this instanceof Boss) {
                        if(gameStage.unicorn.unicornType != UnicornType.STAR) {
                            gameStage.game.tutorialManager.showArrowOnActor(gameStage.changeUnicornType);
                        } else {
                            gameStage.game.tutorialManager.showArrowOnActor(this);
                        }
                    }
                }
            }
        }
    }

    public void changeDirection() {
        float angle = (float) Math.toDegrees(Math.atan2(getX() - gameStage.unicorn.getX(),
                getY() - gameStage.unicorn.getY()));
        angle = 90 - angle;
        directionX = MathUtils.cosDeg(angle);
        directionY = MathUtils.sinDeg(angle);
    }
}
