package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.TutorialAction;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.ui.CompleteDialog;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Utils;

import java.util.Observer;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public abstract class MainCharacter extends Creature implements Observer {

    public static final float MAX_HIT_POINTS = 3;
    public static final float COMBO_VALUE = 0.13f;
    private PowerBar powerBar;
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

    private static Array<Enemy> positionChangeListeners = new Array<>();
    private TutorialManager.EventListener listener;

    private Bone posFill;

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
            if(listener != null) {
                listener.eventFire();
                removePositionChangeListener();
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

//        powerBar = new PowerBar(gameStage);
//        powerBar.setPosition(getX() - powerBar.getWidth() / 2, getY());
//        gameStage.addActor(powerBar);
//        powerBar.setZIndex(getZIndex() + 1);

        positionY = 0;
        tile = gameStage.grid.grid[0][0];
        this.setPosition(gameStage.colorsPlatform.positions.get(positionY).x, gameStage.colorsPlatform.positions.get(positionY).y);
        positionChanged();
        maxHitPoints = hitPoints = MAX_HIT_POINTS;
//        enableRainbowMode();
        setUserObject(ActorDataType.UNICORN);
        fireBone = skeletonActor.getSkeleton().findBone("center-spawn");

        posFill = gameStage.colorsPlatform.skeletonActor.getSkeleton().findBone("health-bar");
        Debug.log("posfill x  = " + posFill.getX());
    }

    public void setProgress() {
        Debug.log("progress = " + (hitPoints / maxHitPoints * getWidth()));
        posFill.setX(2.23f + hitPoints / maxHitPoints * 1.36f);
        if(hitPoints <= 0) {
            gameStage.colorsPlatform.skeletonActor.getSkeleton().findSlot("healthbar").getColor().a = 0;
        }
    }

    @Override
    public void hit(float damage) {
        super.hit(damage);
        if(gameStage.colorsPlatform.skeletonActor.getAnimationState().getTracks().get(0) == null ||
                !gameStage.colorsPlatform.skeletonActor.getAnimationState().getTracks().get(0).getAnimation().getName().equals("hit")) {
            gameStage.colorsPlatform.skeletonActor.getAnimationState().setAnimation(1, "hit", false);
        }
        if(hitPoints < maxHitPoints / 3) {
            if(!gameStage.colorsPlatform.skeletonActor.getSkeleton().getSkin().equals("2")) {
                gameStage.colorsPlatform.skeletonActor.getSkeleton().setSkin("2");
            }
        } else if(hitPoints < maxHitPoints / 1.5f) {
            if(!gameStage.colorsPlatform.skeletonActor.getSkeleton().getSkin().equals("1")) {
                gameStage.colorsPlatform.skeletonActor.getSkeleton().setSkin("1");
            }
        }
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if(powerBar != null) {
            powerBar.setPosition(getX() - powerBar.getWidth() / 2, getY() - powerBar.getHeight());
        }
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "idle", true);
        skeletonActor.getAnimationState().setAnimation(1, "comboup-idle", true);
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

    public void addPositionChangeListener(final TutorialManager.EventListener listener) {
        this.listener = listener;
    }

    public void removePositionChangeListener() {
        this.listener = null;
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
        float oldY = getY();
        setY(gameStage.colorsPlatform.positions.get(positionY).y);
        if(oldY != getY()) {
            gameStage.game.soundManager.playMusic(SoundManager.CHANGE_COLOR, Sound.class, true, false);
        }
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
        if(!gameStage.stopGame) {
            gameStage.stopGame = true;
            skeletonActor.getAnimationState().setAnimation(0, "die", false);
            skeletonActor.getAnimationState().setAnimation(1, "die", false);
            skeletonActor.getAnimationState().clearListeners();
            gameStage.colorsPlatform.skeletonActor.getAnimationState().setAnimation(0, "die", false);
            setProgress();
            gameStage.colorsPlatform.skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateListener() {
                @Override
                public void event(int trackIndex, Event event) {

                }

                @Override
                public void complete(int trackIndex, int loopCount) {
                    CompleteDialog dialog = new CompleteDialog(gameStage.game.uiScreen.stage);
                    gameStage.game.uiScreen.stage.addActor(dialog);
                    gameStage.colorsPlatform.skeletonActor.getAnimationState().clearListeners();
                }

                @Override
                public void start(int trackIndex) {

                }

                @Override
                public void end(int trackIndex) {

                }
            });
//            addAction(Actions.delay(0.3f, Actions.run(new Runnable() {
//                @Override
//                public void run() {
                    stage.game.multiplexer.removeProcessor(stage);
                    Gdx.input.setInputProcessor(stage.game.multiplexer);
//                }
//            })));
        }
    }

    public void regenerate() {
        if(!gameStage.stopGame) {
            if (hitPoints < maxHitPoints) {
                hitPoints += 0.1f;
            }
        }
    }

    public abstract void useAbility();
    public abstract Bullet getNextBullet();
    public abstract void reset();

    public void moveTo(Tile tile) {
        if(this.tile != null) {
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
        if(combo < 19) {
            this.combo += COMBO_VALUE;
            combo = (int)(this.combo / COMBO_VALUE);

//            if (combo >= 20) {
//                bulletsToShootCount = 3;
////            } else if (combo >= 50) {
////                bulletsToShootCount = 3;
////        } else if(combo > 10) {
////            bulletsToShootCount = 2;
//            } else {
//                bulletsToShootCount = 1;
//            }
            gameStage.game.uiScreen.stage.powerBar.setProgress(combo / 5, false);
        }
    }

    private void activateDoubleBullet() {
        bulletsToShootCount = 2;
    }

    boolean tutorialHintShown = false;
    public void resetCombo() {
        if(!tutorialHintShown) {
            gameStage.game.tutorialManager.fireAction(TutorialAction.WRONG_COLOR, null);
        }
        gameStage.game.soundManager.playMusic(SoundManager.ERROR, Sound.class, true, false, 1.5f);
        Gdx.input.vibrate(100);
        int combo = (int)(this.combo / COMBO_VALUE);
        int progress = -1;
        if(combo >= 5) {
            this.combo -= COMBO_VALUE * 5;
            progress = (int)(this.combo / COMBO_VALUE) / 5;
        } else {
            this.combo = 0;
        }
//        if(combo <= 5) {
//            this.combo = 0;
//        } else if(combo <= 10) {
//            this.combo = COMBO_VALUE * 5;
//        } else if (combo <= 15) {
//            this.combo = COMBO_VALUE * 10;
//        } else {
//            this.combo = COMBO_VALUE * 15;
//        }
//        if(this.combo < COMBO_VALUE) {
//            this.combo = COMBO_VALUE;
//        }
        combo = (int)(this.combo / COMBO_VALUE);
        if(combo / 5 >= 0) {
            gameStage.game.uiScreen.stage.powerBar.setProgress(progress, true);
        }
//        if(combo >= 20) {
//            bulletsToShootCount = 3;
////        } else if(combo >= 40) {
////            bulletsToShootCount = 3;
////        } else if(combo > 10) {
////            bulletsToShootCount = 2;
//        } else {
//            bulletsToShootCount = 1;
//        }
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
        if(!gameStage.stopGame) {
            if (hitPoints < maxHitPoints) {
                hitPoints += 0.01f * delta;
                setProgress();
                if(hitPoints > maxHitPoints / 1.5f) {
                    if(!gameStage.colorsPlatform.skeletonActor.getSkeleton().getSkin().equals("0")) {
                        gameStage.colorsPlatform.skeletonActor.getSkeleton().setSkin("0");
                    }
                } else if(hitPoints > maxHitPoints / 3) {
                    if(!gameStage.colorsPlatform.skeletonActor.getSkeleton().getSkin().equals("1")) {
                        gameStage.colorsPlatform.skeletonActor.getSkeleton().setSkin("1");
                    }
                }
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
