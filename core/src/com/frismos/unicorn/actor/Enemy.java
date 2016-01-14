package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UserDataType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Utils;

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

    public boolean isSonOfABoss = false; // :D

    private AnimationState.AnimationStateListener dieListener = new AnimationState.AnimationStateListener() {

        @Override
        public void event(int trackIndex, Event event) {
            if (Enemy.this.tile != null) {
                if(Enemy.this.tile.j != 0){
                    tile.color(colorType);
                }
                tile = null;
            }
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            int prob = 0;
            if(Enemy.this instanceof Boss) {
                prob = 100;
            } else if(Enemy.this instanceof AttackingEnemy) {
                prob = 25;
            } else if(Enemy.this instanceof BouncingEnemy) {
                prob = 7;
            } else if(Enemy.this instanceof ShootingEnemy) {
                prob = 3;
            }
            if(MathUtils.random(100) < prob) {
                gameStage.putSpell(getX(), getY());
            }
            animationState.removeListener(this);
            remove();
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public Enemy(GameStage stage, UserData userData, ColorType colorType) {
        super(stage, userData, colorType);

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
            positionY = MathUtils.random(GameStage.ROW_LENGTH - 1);
        }
        if(gameStage.colorIndices.size > 0) {
            if(colorType == ColorType.YELLOW) {
                positionY = 0;
            } else if(colorType == ColorType.RED) {
                positionY = 1;
            } else if(colorType == ColorType.GREEN) {
                positionY = 2;
            } else if(colorType == ColorType.BLUE) {
                positionY = 3;
            }
        }
//        if(MathUtils.random(100) <= GameStage.ENEMY_SPAWN_MIDDLE_CHANCE) {
//            positionX = MathUtils.random(3, GameStage.COLUMN_LENGTH - 1);
//        }
        this.setX(gameStage.background.getZero().x + positionX * gameStage.grid.tileWidth + getWidth() / 2);
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + getHeight() / 2);
        spawnPoint = new Vector2(0, getY() + getHeight() * getScaleY() / 2);
        spawnPoint = gameStage.stageToScreenCoordinates(spawnPoint);
        isAttacking = true;
        moveSpeed = GameStage._ENEMY_MOVE_SPEED;
    }

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    @Override
    public UserData getUserData() {
        return this.userData;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void die(AnimationState.AnimationStateListener dieListener) {
        if(isAttacking) {
            gameStage.collisionDetector.collisionListeners.removeValue(this, false);
            dispose();
            gameStage.unicorn.removePositionChangeListener(this);
            if(gameStage.boss == null) {
                gameStage.deadEnemyCounter++;
            }
            if (Enemy.this.tile != null) {
                tile.enemies.removeValue(Enemy.this, false);
            }
            isAttacking = false;
            if(userData.getUserDataType() != UserDataType.BOSS) {
                animationState.setAnimation(0, "die", false);
            }
            if(dieListener == null) {
                dieListener = this.dieListener;
            }
            animationState.removeListener(dieListener);
            animationState.addListener(dieListener);
        }
    }

    public void die() {
        die(null);
    }

    public abstract void attack();

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
            moveBy(-directionX * moveSpeed * delta, -directionY * moveSpeed * delta);  //  23

            if(this.getX() < 7) {
                die(new AnimationState.AnimationStateListener() {
                    @Override
                    public void event(int trackIndex, Event event) {

                    }

                    @Override
                    public void complete(int trackIndex, int loopCount) {
                        gameStage.restartGame();
                    }

                    @Override
                    public void start(int trackIndex) {

                    }

                    @Override
                    public void end(int trackIndex) {

                    }
                });
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

    @Override
    public boolean remove() {
        dispose();
        return super.remove();
    }
}
