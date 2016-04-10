package com.frismos.unicorn.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.Direction;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.manager.AIManager;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Timer;
import com.frismos.unicorn.util.Utils;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public abstract class Enemy extends Creature {

    public static float INITIAL_MOVE_SPEED = 10.0f;

    private Bone liveBone;
    private float maxLiveScale;
    private float liveScale;

    public Tile tile;

    protected float TIME_STEP = 4f;
    public boolean isAttackingOnUnicorn = false;
    public boolean isAttacking = true;
    public boolean isEating = false;
    protected Vector2 spawnPoint;

    protected float directionX = 1, directionY = 0;

    public int positionY;

    public boolean isTutorialEnemy;

    public boolean isSonOfABoss = false; // :D

    public Enemy waitingEnemy;
    public Enemy enemyToWait;

    public Shadow shadow;

    private boolean hasWait;

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
                int prob = 1;
                /*if (Enemy.this instanceof Boss) {
                    prob = 100;
                } else*/ if (Enemy.this instanceof AttackingEnemy) {
                    prob = 40;
                } else if (Enemy.this instanceof BouncingEnemy) {
                    prob = 15;
                } else if (Enemy.this instanceof ShootingEnemy) {
                    prob = 25;
                }
                if (MathUtils.random(100) < prob) {
//                    gameStage.putSpell(getX(), getY());
                }
            }
            skeletonActor.getAnimationState().removeListener(this);
            remove(true);
            onDieComplete();
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };
    private Timer eatingTimer;
    private boolean isDead = false;
    protected boolean isWaiting = false;

    public Enemy(GameStage stage, ColorType colorType) {
        this(stage, colorType, false);
    }

    public Enemy(GameStage stage, ColorType colorType, boolean isTutorialEnemy) {
        super(stage, colorType);

        setColorType(colorType);

        int positionX = GameStage.COLUMN_LENGTH;
        positionY = -1;
//        int percent = MathUtils.random(100);
//        if(percent <= GameStage.ENEMY_MATCH_CHANCE) {
//            if (colorType == ColorType.RED) {
//                if (gameStage.grid.nextRedRow != -1) {
//                    positionY = gameStage.grid.nextRedRow;
//                    gameStage.grid.nextRedRow = -1;
//                }
//            } else if (colorType == ColorType.GREEN) {
//                if (gameStage.grid.nextGreenRow != -1) {
//                    positionY = gameStage.grid.nextGreenRow;
//                    gameStage.grid.nextGreenRow = -1;
//                }
//            } else if (colorType == ColorType.BLUE) {
//                if (gameStage.grid.nextBlueRow != -1) {
//                    positionY = gameStage.grid.nextBlueRow;
//                    gameStage.grid.nextBlueRow = -1;
//                }
//            } else if (colorType == ColorType.YELLOW) {
//                if (gameStage.grid.nextYellowRow != -1) {
//                    positionY = gameStage.grid.nextYellowRow;
//                    gameStage.grid.nextYellowRow = -1;
//                }
//            }
//        }

        if(positionY == -1) {
            int positionYOffset = isTutorialEnemy ? 2 : 1;
            positionY = MathUtils.random(GameStage.ROW_LENGTH - positionYOffset);
        }
        if(gameStage.colorIndices.size > 0) {
            if(colorType == ColorType.RED) {
                positionY = 2;
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
        if(this instanceof Boss) {
            this.setX(gameStage.background.getZero().x + (positionX - 2) * gameStage.grid.tileWidth + getWidth() / 2);
        }else
        this.setX(gameStage.background.getZero().x + (positionX - 1) * gameStage.grid.tileWidth + getWidth() / 2);

        int prob = MathUtils.random(2);
        float offsetY = prob == 2 ? getHeight() / 2 : prob == 1 ? getHeight() / 4 : getHeight() / 1.5f;
        this.setY(gameStage.background.getZero().y + positionY * gameStage.grid.tileHeight + offsetY);
        spawnPoint = new Vector2(0, getY() + getHeight() * getScaleY() / 2);
        this.isTutorialEnemy = isTutorialEnemy;
        if(isSonOfABoss) {
            moveSpeed = GameStage._ENEMY_MOVE_SPEED;
        }
        moveSpeed = 1;//INITIAL_MOVE_SPEED + stage.unicorn.getCombo() / 10.0f;//// TODO: 4/10/16  
        skeletonActor.getAnimationState().setTimeScale(moveSpeed * 0.3f);
        setUserObject(ActorDataType.ENEMY);
        shadow = new Shadow(gameStage, this);
        skeletonActor.getSkeleton().getRootBone().setScale(MathUtils.random(0.95f, 1.05f));

        for(int i = 0; i < skeletonActor.getSkeleton().getBones().size; i++) {
            if(skeletonActor.getSkeleton().getBones().get(i).getData().getName().contains("life-")) {
                liveBone = skeletonActor.getSkeleton().getBones().get(i);
                break;
            }
        }
        maxLiveScale = Integer.valueOf(liveBone.getData().getName().substring(5));
        liveScale = liveBone.getScaleX();
    }

    @Override
    public void hit(float damage, Bullet bullet) {
        super.hit(damage, bullet);
        if(hitPoints > 0) {
            Utils.colorActor(this, Color.WHITE);
            addAction(Actions.sequence(Actions.delay(0.02f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    Utils.colorActor(Enemy.this, getColor());
                }
            })));
        }
        float scale = liveScale + (maxHitPoints - hitPoints) / maxHitPoints * (maxLiveScale - liveScale);
        if(scale > maxLiveScale) {
            scale = maxLiveScale;
        }
        liveBone.setScale(scale);
    }

    protected void onDieComplete() {

    }

    public boolean checkToWait() {
        if(tile != null) {
            tile.enemies.removeValue(this, false);
        }
        tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY());
        tile.enemies.add(this);
        start();
        if(!gameStage.game.tutorialManager.isTutorialMode) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (!tile.enemies.get(i).equals(this)
                        && ((tile.enemies.get(i).getRight() > getX() && tile.enemies.get(i).getX() < getX())
                        || (getRight() > tile.enemies.get(i).getX() && tile.enemies.get(i).getX() > getX()))
                        && tile.enemies.get(i).getTop() > getY()
                        && !this.equals(tile.enemies.get(i).waitingEnemy)
                        && !tile.enemies.get(i).equals(waitingEnemy)
                        && !tile.enemies.get(i).isWaiting
                        && !tile.enemies.get(i).isDead) {
                    isWaiting = true;
                    tile.enemies.get(i).addWaitingEnemy(this);
                    return true;
                }
            }
        }
        return false;
    }

    public void setColorType(ColorType colorType) {
        Utils.setActorColorType(this, colorType);
    }

    public void addWaitingEnemy(Enemy waitingEnemy) {
        if(this.waitingEnemy == null) {
            this.waitingEnemy = waitingEnemy;
            waitingEnemy.enemyToWait = this;
            return;
        }
        this.waitingEnemy.addWaitingEnemy(waitingEnemy);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void playDieSound() {
        gameStage.game.soundManager.playMusic(SoundManager.EXPLODE, Sound.class, true);
    }

    public void die(AnimationState.AnimationStateListener dieListener) {
        if(!isDead) {
            playDieSound();
            gameStage.score++;
            gameStage.game.uiScreen.stage.scoreLabel.setText(java.lang.String.format("score: %d", gameStage.score));
            if(gameStage.game.aiManager.enemies.size == 0) {
                if(!(this instanceof Boss) && !(this instanceof BossSon) && gameStage.boss == null) {
                    gameStage.game.aiManager.sendEnemy(gameStage.game.aiManager.currentIndex);
                }
            }
            if(shadow != null) {
                shadow.remove();
            }
            hideProgressBar();

            if(waitingEnemy != null) {
                boolean wait = false;
                if(enemyToWait != null) {
                    enemyToWait.waitingEnemy = waitingEnemy;
                    waitingEnemy.enemyToWait = enemyToWait;
                    wait = true;
//                if(waitingEnemy.enemyToWait == null) {
//                }
                }
                if(!wait) {
                    waitingEnemy.start();
                }
                waitingEnemy = null;
            } else if(enemyToWait != null) {
                enemyToWait.waitingEnemy = null;
            }
            if(eatingTimer != null) {
                gameStage.game.timerManager.removeTimer(eatingTimer);
            }
            if(gameStage.game.tutorialManager.isTutorialMode) {
                gameStage.game.tutorialManager.enemies.removeValue(this, false);
                if(this.isTutorialEnemy) {
                    gameStage.game.tutorialManager.isTutorialEnemyOnStage = false;
                }
                if(TutorialStep.values().length - 1 == gameStage.game.tutorialManager.currentStep.ordinal()) {
                    gameStage.game.tutorialManager.isTutorialMode = false;
                } else {
                    if(gameStage.game.tutorialManager.currentStep == TutorialStep.FIRST) {          //cul
                        gameStage.game.tutorialManager.currentStep = TutorialStep.SECOND;
                    } else if(TutorialStep.SECOND == gameStage.game.tutorialManager.currentStep) {  //kov
                        if(gameStage.game.tutorialManager.secondStepEnemies >= TutorialManager.SECOND_STEP_ENEMIES_COUNT) {
                            if(gameStage.game.tutorialManager.enemies.size == 0) {
                                gameStage.game.tutorialManager.currentStep = TutorialStep.THIRD;
                                gameStage.joystickTouched = false;
                            }
                        }
                    } else if(TutorialStep.THIRD == gameStage.game.tutorialManager.currentStep) {   //ayc
                        if(isTutorialEnemy && colorType == gameStage.game.tutorialManager.thirdStepColor) {
                            gameStage.game.tutorialManager.currentStep = TutorialStep.FOURTH;
                            gameStage.game.tutorialManager.pauseGame = false;
                            gameStage.changeUnicornType.remove();
                        }
                    } else if(TutorialStep.FOURTH == gameStage.game.tutorialManager.currentStep) {  //xoz
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
            if(gameStage.unicorn.combo + Bullet.SINGLE_BULLET_DAMAGE < maxHitPoints + MainCharacter.COMBO_VALUE) {
                gameStage.game.timerManager.run(0.3f, new TimerRunnable() {
                    @Override
                    public void run(Timer timer) {
                        gameStage.collisionDetector.removeListenerActor(Enemy.this);
                    }
                });
            } else {
                gameStage.collisionDetector.removeListenerActor(Enemy.this);
            }

            gameStage.unicorn.removePositionChangeListener(this);
            if(gameStage.boss == null && !gameStage.game.tutorialManager.isTutorialMode) {
                gameStage.deadEnemyCounter++;
            }
            if (Enemy.this.tile != null) {
                tile.enemies.removeValue(Enemy.this, false);
            }
            isAttacking = false;
            isDead = true;
            skeletonActor.getAnimationState().setAnimation(0, "die", false);
            if(dieListener == null) {
                dieListener = this.dieListener;
            }
            skeletonActor.getAnimationState().clearListeners();
            skeletonActor.getAnimationState().addListener(dieListener);
        }
    }

    @Override
    public void die() {
        die(null);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if (shadow != null) {
            shadow.setPosition(getX(), getY() - shadow.getHeight() / 3);
        }
    }

    public abstract void attack();
    public void wallAttackingAnimation() {
        skeletonActor.getAnimationState().setTimeScale(1.0f);
    }
    public void startAttackingWall() {
        eatingTimer = gameStage.game.timerManager.loop(0.5f, new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                eatWall();
            }
        });
        wallAttackingAnimation();
    }
    public void eatWall() {
        gameStage.unicorn.hit(0.1f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAttacking && !isEating && !isWaiting) {
            if(gameStage.grid.isPointInsideGrid(getX(), getY())) {
                if(tile != null) {
                    tile.enemies.removeValue(this, false);
                }
                tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY());
                tile.enemies.add(this);
            }
//            if(!gameStage.game.tutorialManager.isTutorialMode || !gameStage.game.tutorialManager.pauseGame) {
                moveBy(-directionX * moveSpeed * delta, -directionY * moveSpeed * delta);  //  23
//            }
            if(waitingEnemy != null) {
                if (getRight() + getWidth() / 2 < waitingEnemy.getX()) {
                    waitingEnemy.start();
                    waitingEnemy = null;
                }
            }

            if(!hasWait && this.getX() <= gameStage.colorsPlatform.getRight() + gameStage.grid.tileWidth / 6) {
                hasWait = true;
                isWaiting = checkToWait();
                if(isWaiting) {
                    wallAttackingAnimation();
                }
            }
            if(this.getX() <= gameStage.colorsPlatform.getRight()) {
                isEating = true;
                gameStage.unicorn.hit(0.1f);
                startAttackingWall();
            }
            if(moveSpeed > GameStage._ENEMY_MOVE_SPEED
//                    && getX() < gameStage.background.getZero().x + gameStage.background.getWidth() - gameStage.grid.tileWidth
                    && getX() > gameStage.colorsPlatform.getRight() + gameStage.grid.tileWidth) {
                moveSpeed -= 0.02f;
                if(skeletonActor.getAnimationState().getCurrent(0).getAnimation().getName().contains("walk")) {
                    skeletonActor.getAnimationState().setTimeScale(moveSpeed * 0.3f);
                }
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

    public void start() {
        isAttacking = true;
        isWaiting = false;
        enemyToWait = null;
    }
}
