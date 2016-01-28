package com.frismos.unicorn.stage;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.spine.utils.SkeletonActorPool;
import com.frismos.TweenAccessor.CameraAccessor;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.*;
import com.frismos.unicorn.enums.*;
import com.frismos.unicorn.grid.Grid;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.util.*;

import java.util.Comparator;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class GameStage extends Stage {

    public UnicornGame game;

    private Image changeUnicornType;
    public Grid grid;
    public static Texture blackPixel;

    public Unicorn unicorn;

    private ObjectMap<UnicornType, Unicorn> unicorns = new ObjectMap<UnicornType, Unicorn>();

    public Background background;
    public int deadEnemyCounter = 0;

    public Boss boss;
    public int level = 1;

    // set this v lues  ***** **** **** **** * *****************************************************
    public static float ENEMY_SEND_TIME_STEP = 4.0f;
    public static float ENEMY_SEND_ACCELERATION_TIME_STEP = 10f;
    public static float ENEMY_SEND_ACCELERATION_STEP = 0.1f;
    public static float MIN_ENEMY_SEND_TIME_STEP = 1.0f;

    public static float BOSS_MOVE_SPEED = 1.0f;
    public static float ENEMY_MOVE_SPEED = 2.0f;
    public static float ENEMY_MOVE_ACCELERATION_TIME_STEP = 15f;
    public static float ENEMY_MOVE_ACCELERATION_STEP = 0.05f;
    public static float MAX_ENEMY_MOVE_SPEED = 5.0f;

    public static final float VELOCITY_HORIZONTAL_FORCE = 2300.0f;
    public static final float VELOCITY_VERTICAL_FORCE = 1900.0f;

    public static final int ENEMY_MATCH_CHANCE = 50;
    public static final float ENEMY_SPAWN_MIDDLE_CHANCE = 30;

    public static final float BULLET_MOVE_SPEED = 60;
    public static final float ENEMY_BULLET_MOVE_SPEED = 30;

    private int DEAD_ENEMIES_TILL_BOSS = 10;
    private static final int MAX_DEAD_ENEMY_COUNT = 50;

    private static int ATTACKING_ENEMY_SPAWN_CHANCE = 10;
    private static int SHOOTING_ENEMY_SPAWN_CHANCE = 15;
    private static int BOUNCING_ENEMY_SPAWN_CHANCE = 25;

    public static float ENEMY_SEND_ACCELERATION_TIME = 1.2f;
    //**** ****** ***** *** ****************** * ***************************************************

    private float enemyAccelerationTimer = 0;
    private static float _ENEMY_SEND_TIME_STEP = 5f;
    private static float _ENEMY_SEND_ACCELERATION_TIME_STEP = 20f;
    private static float _ENEMY_SEND_ACCELERATION_STEP = 0.1f;
    private static float _MIN_ENEMY_SEND_TIME_STEP = 1.0f;

    public static float _ENEMY_MOVE_SPEED = 20.0f;
    public static float _BOSS_MOVE_SPEED = 20.0f;
    private static float _ENEMY_MOVE_ACCELERATION_TIME_STEP = 5.0f;
    private static float _ENEMY_MOVE_ACCELERATION_STEP = 1f;
    private static float _MAX_ENEMY_MOVE_SPEED = 40.0f;

    public static float _ENEMY_SEND_ACCELERATION_TIME = 1.2f;

    public static final int ROW_LENGTH = 4;
    public static final int COLUMN_LENGTH = 7;

    private float enemyMoveTimer = 0.0f;
    private float enemySendCounter = 5.0f;
    private float enemySendTimer = 0.0f;
    private float fireTimer;

    private EnemyDie enemyDie;
    private EnemyDie unicornDie;
    public boolean restartGame = false;
    public boolean stopGame = false;

    private Array<Integer> directions = new Array<>();
    public Array<Integer> colorIndices = new Array<>();

    public Array<Bullet> gameBullets = new Array<>();
    public int nextBulletIndex = 0;

    public int score = 0;
    public Label scoreLabel;

    private int colorRowsLength;

    private Vector2 screenZero;

    private Image joystick;
    private Image joystickArea;
    private boolean joystickTouched = false;
    private float joystickX;
    private float joystickY;
    private float joystickRadius;
    private float normalEnemySendTimeStep = ENEMY_SEND_TIME_STEP;

    public Spell spellCandy;

    public CollisionDetector collisionDetector = new CollisionDetector();
    private Runnable spellCandyRunnable = new Runnable() {
        @Override
        public void run() {
            collisionDetector.collisionListeners.add(spellCandy);
        }
    };
    private boolean sendWave = false;
    private int currentWaveLevel = 1;
    private int fireIndex;

    private float layeringTime = 0.1f;
    private float layeringTimer = 0.0f;

    private Pool<Enemy> pools = new Pool<Enemy>() {
        @Override
        protected Enemy newObject() {
            return new WalkingEnemy(GameStage.this, ColorType.GREEN);
        }
    };
    private Comparator<? super Actor> actorsComporator = new Comparator<Actor>() {
        @Override
        public int compare(Actor o1, Actor o2) {
            if (o1 instanceof Background || o2 instanceof Background ||
                    o1 instanceof Bullet || o2 instanceof Bullet ||
                    o1 instanceof Grid || o2 instanceof Grid ||
                    o1 instanceof ProgressBar || o2 instanceof ProgressBar) {
                return 0;
            }
            if (o1.getY() < o2.getY()) {
                //                    Debug.Log("y smaller");
                return 1;
            } else if (o1.getY() > o2.getY()) {
                return -1;
            } else if (o1.getX() < o2.getX()) {
                //                    Debug.Log("x smaller");
                return 1;
            } else if (o1.getX() > o2.getX()) {
                //                    Debug.Log("x greater");
                return -1;
            } else if (o1.getZIndex() < o2.getZIndex()) {
                Debug.Log("z index smaller");
                return 1;
            } else if (o1.getZIndex() > o2.getZIndex()) {
                Debug.Log("z index greater");
                return -1;
            }

            Debug.Log("LAYER STAGE 0");
            return 0;
        }
    };

    public GameStage(final UnicornGame game) {
        super(new StretchViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT), new PolygonSpriteBatch());
        this.game = game;

        blackPixel = new Texture("blackPixel.png");

        background = new Background(this);
        grid = new Grid(this);
        addActor(grid);

        unicorn = new Unicorn(this, UnicornType.UNICORN);
        unicorn.setX(background.getZero().x + grid.tileWidth / 2 - unicorn.getWidth() / 2);
        unicorn.setY(background.getZero().y + grid.tileHeight / 2 - unicorn.getHeight() / 2);
        unicorn.showProgressBar();
        unicorns.put(unicorn.unicornType, unicorn);

        Rhino rhino = new Rhino(this, UnicornType.RHINO);
        rhino.setX(background.getZero().x + grid.tileWidth / 2 - rhino.getWidth() / 2);
        rhino.setY(background.getZero().y + grid.tileHeight / 2 - rhino.getHeight() / 2);
        rhino.showProgressBar();
        unicorns.put(UnicornType.RHINO, rhino);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);

        addActor(background);
        screenZero = new Vector2();
        screenZero.x = Gdx.graphics.getWidth() / Constants.VIEWPORT_WIDTH * background.getZero().x;
        screenZero.y = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / Constants.VIEWPORT_HEIGHT * background.getZero().y;

        collisionDetector.collisionListeners.add(unicorn);
        addActor(unicorn);
        unicorn.setZIndex(Integer.MAX_VALUE);
//        int length = GameStage.ROW_LENGTH > ColorType.values().length ? ColorType.values().length : GameStage.ROW_LENGTH;
//        for(int i = 0; i < length; i++) {
//            colorIndices.add(i);
//        }

        BitmapFont font = game.fontsManager.getFont(20);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("score: 0", style);
        scoreLabel.setFontScale(0.1f);
        scoreLabel.setPosition(2f, Constants.VIEWPORT_HEIGHT - scoreLabel.getHeight() / 1.85f);
        addActor(scoreLabel);

        joystick = new Image(game.atlasManager.get("gfx/joystick.png", Texture.class));
        joystick.setScale(0.0075f);
        joystickX = getWidth() - joystick.getWidth() * joystick.getScaleX() * 4;
        joystickY = background.getZero().y + 2;
        joystick.setX(joystickX);
        joystick.setY(joystickY);
        joystick.getColor().a = 0.85f;

        joystickRadius = joystick.getWidth() * joystick.getScaleX() * 1.5f;
        joystickArea = new Image(game.atlasManager.get("gfx/joystick.png", Texture.class));
        joystickArea.setSize(joystickRadius * 2, joystickRadius * 2);
        joystickArea.setX(joystickX - joystickRadius + joystick.getWidth() * joystick.getScaleX() / 2);
        joystickArea.setY(joystickY - joystickRadius + joystick.getHeight() * joystick.getScaleY() / 2);
        joystickArea.getColor().a = 0.6f;
//        addActor(joystickArea);
//        addActor(joystick);

        changeUnicornType = new Image(game.atlasManager.get("gfx/unicorn.png", Texture.class));
        changeUnicornType.setSize(3, 3);
        changeUnicornType.setX(background.getZero().x);//Constants.VIEWPORT_WIDTH - changeUnicornType.getWidth() * 20 * changeUnicornType.getScaleX());
        changeUnicornType.setY(1);
        changeUnicornType.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                GameStage.this.game.gameCenterController.showLeaderboardView("");
                unicorn.callUnicorns();
                changeUnicorn();
                joystickTouched = false;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        addActor(changeUnicornType);

        initConstants();

        for (int i = 0; i < 20; i++) {
            gameBullets.add(new Bullet(this, ActorDataType.BULLET));
        }

        Thread detector = new Thread(collisionDetector);
        detector.start();

        if(game.tutorialManager.isTutorialMode) {
            colorIndices.add(0);
        }
    }

    public void shakeWorld(int count) {
        if(count == 0) {
            return;
        }
        final float x = MathUtils.randomBoolean() ? -0.1f : 0.1f;
        final float y = MathUtils.randomBoolean() ? -0.1f : 0.1f;

        final int fCount = --count;
        Tween.to(getCamera(), CameraAccessor.POSITION_XY, 0.05f).targetRelative(x, y).start(game.tweenManager).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                Tween.to(getCamera(), CameraAccessor.POSITION_XY, 0.05f).targetRelative(-x, -y).start(game.tweenManager).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        shakeWorld(fCount);
                    }
                });
            }
        });
    }

    private void changeUnicorn() {
        if(game.tutorialManager.isTutorialMode && game.tutorialManager.currentStep == TutorialStep.FOURTH) {
            game.tutorialManager.currentStep = TutorialStep.FINISH;
        }
        unicorn.remove();
        float y = unicorn.getY();
        ColorType colorType = unicorn.colorType;
        if(unicorn.unicornType == UnicornType.UNICORN) {
            unicorn = unicorns.get(UnicornType.RHINO);
        } else if(unicorn.unicornType == UnicornType.RHINO) {
            unicorn = unicorns.get(UnicornType.UNICORN);
        }
        unicorn.setUnicornType(unicorn.unicornType);
        unicorn.setY(y);
        unicorn.showProgressBar();
        unicorn.setColorType(colorType);
        collisionDetector.collisionListeners.add(unicorn);
        addActor(unicorn);
    }

    private void initConstants() {
        _ENEMY_SEND_TIME_STEP = ENEMY_SEND_TIME_STEP;
        _ENEMY_SEND_ACCELERATION_TIME_STEP = ENEMY_SEND_ACCELERATION_TIME_STEP;
        _ENEMY_SEND_ACCELERATION_STEP = ENEMY_SEND_ACCELERATION_STEP;
        _MIN_ENEMY_SEND_TIME_STEP = MIN_ENEMY_SEND_TIME_STEP;
        _ENEMY_MOVE_SPEED = ENEMY_MOVE_SPEED;
        _ENEMY_MOVE_ACCELERATION_TIME_STEP = ENEMY_MOVE_ACCELERATION_TIME_STEP;
        _ENEMY_MOVE_ACCELERATION_STEP = ENEMY_MOVE_ACCELERATION_STEP;
        _MAX_ENEMY_MOVE_SPEED = MAX_ENEMY_MOVE_SPEED;
        _BOSS_MOVE_SPEED = BOSS_MOVE_SPEED;
        _ENEMY_SEND_ACCELERATION_TIME = ENEMY_SEND_ACCELERATION_TIME;
    }

    private Boss sendBoss() {
        return sendBoss(false);
    }

    private Boss sendBoss(boolean isTutorial) {
        ColorType colorType = ColorType.RAINBOW;
        Enemy boss;
        if(MathUtils.randomBoolean()) {
            boss = new ShootingBoss(this, colorType, isTutorial);
        } else {
            boss = new MotherBoss(this, colorType, isTutorial);
        }
        game.tutorialManager.isTutorialEnemyOnStage = isTutorial;
        collisionDetector.collisionListeners.add(boss);
        addActor(boss);
        boss.setZIndex(unicorn.getZIndex());
        boss.showProgressBar();
        return (Boss)boss;
    }

    private void sendEnemy() {
        sendEnemy(false);
    }

    private void sendEnemy(boolean isTutorial) {
        ColorType colorType;
        Enemy enemy;
        ATTACKING_ENEMY_SPAWN_CHANCE = level == 1 || sendWave ? 0 : 10;
        if(colorIndices.size > 0) {
            int colorIndex = colorIndices.get(MathUtils.random(colorIndices.size - 1));
            colorType = ColorType.values()[colorIndex];
            enemy = new WalkingEnemy(this, colorType, isTutorial);
            colorIndices.removeValue(colorIndex, true);
        } else {
            colorType = ColorType.getRandomColor();
            int enemyProb = MathUtils.random(100);
            if(enemyProb <= SHOOTING_ENEMY_SPAWN_CHANCE) {
                enemy = new ShootingEnemy(this, colorType);
            } else if(enemyProb <= SHOOTING_ENEMY_SPAWN_CHANCE + ATTACKING_ENEMY_SPAWN_CHANCE) {
                enemy = new AttackingEnemy(this, colorType);
            } else if(enemyProb <= SHOOTING_ENEMY_SPAWN_CHANCE + ATTACKING_ENEMY_SPAWN_CHANCE + BOUNCING_ENEMY_SPAWN_CHANCE) {
                enemy = new BouncingEnemy(this, colorType);
            } else {
                enemy = new WalkingEnemy(this, colorType);
            }
        }
        enemy.isTutorialEnemy = isTutorial;
        game.tutorialManager.isTutorialEnemyOnStage = isTutorial;
        addActor(enemy);
        collisionDetector.collisionListeners.add(enemy);
        enemy.setZIndex(background.getZIndex() + 1);
        if(game.tutorialManager.isTutorialMode) {
            game.tutorialManager.enemies.add(enemy);
        }
    }

    public void restartGame() {
        restartGame = true;
        initConstants();
    }

    public void putSpell(float x, float y) {
        if(spellCandy == null) {
            spellCandy = new Spell(this);
            spellCandy.setScale(0);
            spellCandy.setX(x);
            spellCandy.setY(y);
            addActor(spellCandy);
            spellCandy.addAction(Actions.sequence(Actions.scaleTo(1f, 1f, 1.5f), Actions.run(spellCandyRunnable)));
        }
    }

    public void nextLevel() {
        level++;

        if(unicorn.SINGLE_ATTACK_SPEED >= 0.1f + level / 100.0f) {
            unicorn.SINGLE_ATTACK_SPEED -= level / 100.0f;
        }

        if(unicorn.CANNON_ATTACK_SPEED >= 0.6f + level / 100.0f) {
            unicorn.CANNON_ATTACK_SPEED -= level / 100.0f;
        }
    }

    public interface EnemyDie {
        void die();
    }

    public void unicornFire(float angle) {
        if(!stopGame) {
            unicorn.playFireAnimation(angle);
        }
    }

    public void unicornFire(float x, float y) {
        if(!stopGame) {
            unicorn.playFireAnimation(x, y);
        }
    }

    public void fireBullet(float x, float y) {
        if(unicorn.colorType != null) {
            Bullet bullet;
            if(unicorn.unicornType == UnicornType.RHINO) {
                bullet = new BezierBullet(this, x, y);
                bullet.damage = 1;
            } else {
                bullet = new Bullet(this, x, y, ActorDataType.BULLET);
            }
            bullet.setColorType(unicorn.colorType);
            bullet.setX(unicorn.getFirePoint().x + unicorn.getX());
            bullet.setY(unicorn.getFirePoint().y + unicorn.getY());
            bullet.calculateAngle();
            bullet.fire();

            collisionDetector.collisionListeners.add(bullet);
            addActor(bullet);
        }
    }

    public void fireBullet(float angle) {
        if(unicorn.colorType != null) {
            Bullet bullet = getBullet();
            bullet.setAngle(angle);
            bullet.setColorType(unicorn.colorType);
            bullet.setX(unicorn.getFirePoint().x + unicorn.getX());
            bullet.setY(unicorn.getFirePoint().y + unicorn.getY());
            bullet.fire();

            collisionDetector.collisionListeners.add(bullet);
            addActor(bullet);
        }
    }

    public Bullet getBullet() {
        Bullet bullet = gameBullets.get(nextBulletIndex);
        nextBulletIndex++;
        if(nextBulletIndex == gameBullets.size) {
            nextBulletIndex = 0;
        }
        return bullet;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            restartGame();
            return false;
        }else if(keyCode == Input.Keys.W || keyCode == Input.Keys.UP) {
            unicorn.moveUp(0);
        } else if(keyCode == Input.Keys.S || keyCode == Input.Keys.DOWN) {
            unicorn.moveDown(0);
        } else if(keyCode == Input.Keys.E) {
            changeUnicorn();
        }
        return true;
    }

    private float touchY = Float.MIN_VALUE;
    private boolean isFingerDown = false;
    private float fingerScreenX, fingerScreenY;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(screenX < Gdx.graphics.getWidth() / 6.0f) {
            this.touchY = screenY;
        } else {
            float stageX = screenX * Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() - joystick.getWidth() * joystick.getScaleX() / 2;
            float stageY = (Gdx.graphics.getHeight() - screenY) * Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() - joystick.getHeight() * joystick.getScaleY() / 2;
//            if(unicorn.unicornType == UnicornType.AUTOMATIC_ATTACK) {
                if ((stageX - joystickX) * (stageX - joystickX) + (stageY - joystickY) * (stageY - joystickY) > joystickRadius * joystickRadius) {
                    float borderX = background.getZero().x + grid.tileWidth * (COLUMN_LENGTH - 1) + grid.tileWidth / 2;
                    float borderY = background.getZero().y + grid.tileHeight / 3;

                    if (stageX + joystick.getWidth() * joystick.getScaleX() / 2 < borderX) {
                        joystickX = stageX;
                    } else {
                        joystickX = borderX - joystick.getWidth() * joystick.getScaleX() / 2;
                    }
                    if (stageY + joystick.getHeight() * joystick.getScaleY() / 2 > borderY) {
                        joystickY = stageY;
                    } else {
                        joystickY = borderY - joystick.getHeight() * joystick.getScaleY() / 2;
                    }
                    joystick.addAction(Actions.moveTo(joystickX, joystickY, 0.1f));
                    joystickArea.addAction(Actions.moveTo(joystickX - joystickRadius + joystick.getWidth() * joystick.getScaleX() / 2, joystickY - joystickRadius + joystick.getHeight() * joystick.getScaleY() / 2, 0.1f));
                }
//            }
//            if(unicorn.unicornType == UnicornType.AUTOMATIC_ATTACK) {
            if(stageX < changeUnicornType.getX() ||
                    stageX > changeUnicornType.getX() + changeUnicornType.getWidth() ||
                    stageY < changeUnicornType.getY() ||
                    stageY > changeUnicornType.getY() + changeUnicornType.getHeight()) {
                if(!game.tutorialManager.isTutorialMode) {
                    joystickTouched = true;
                    fireIndex = 0;
                } else {
                    if(!game.tutorialManager.isTutorialEnemyOnStage || game.tutorialManager.pauseGame) {
                        if(game.tutorialManager.currentStep == TutorialStep.FIRST ||
                                game.tutorialManager.currentStep == TutorialStep.SECOND) {
                            joystickTouched = true;
                            fireIndex = 0;
                        } else if(game.tutorialManager.currentStep == TutorialStep.THIRD) {
                            if(unicorn.unicornType == UnicornType.RHINO && unicorn.colorType == game.tutorialManager.thirdStepColor) {
                                joystickTouched = true;
                                fireIndex = 0;
                            }
                        } else if(game.tutorialManager.currentStep == TutorialStep.FOURTH || game.tutorialManager.currentStep == TutorialStep.FINISH) {
                            if(unicorn.unicornType == UnicornType.UNICORN) {
                                joystickTouched = true;
                                fireIndex = 0;
                            }
                        }
                    }
                }
            }
//            }
            isFingerDown = true;
            fingerScreenX = screenX;
            fingerScreenY = screenY;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(screenX >= Gdx.graphics.getWidth() / 6.0f) {
            fingerScreenX = screenX;
            fingerScreenY = screenY;
            if (joystickTouched) {
                float x = screenX * Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() - joystick.getWidth() * joystick.getScaleX() / 2;
//                if(x < joystickX) {
//                    fingerScreenX = (joystickX + joystick.getWidth() * joystick.getScaleX() / 2) * Gdx.graphics.getWidth() / Constants.VIEWPORT_WIDTH;
//                }
                float y = (Gdx.graphics.getHeight() - screenY) * Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() - joystick.getHeight() * joystick.getScaleY() / 2;
                float r = joystickRadius;
                if ((x - joystickX) * (x - joystickX) + (y - joystickY) * (y - joystickY) <= r * r) {
                    joystick.setPosition(x, y);
                } else {
                    float dx = x - joystickX;
                    float dy = y - joystickY;
                    float dr = (float) Math.sqrt(dx * dx + dy * dy);
                    float D = 0;

                    float xPlus = D * dy + (dy < 0 ? -1 : 1) * dx * (float) Math.sqrt(r * r * dr * dr - D * D) / (dr * dr) + joystickX;
                    float xMinus = D * dy - (dy < 0 ? -1 : 1) * dx * (float) Math.sqrt(r * r * dr * dr - D * D) / (dr * dr) + joystickX;

                    float yPlus = -D * dx + (float) (Math.abs(dy) * Math.sqrt(r * r * dr * dr - D * D)) / (dr * dr) + joystickY;
                    float yMinus = -D * dx - (float) (Math.abs(dy) * Math.sqrt(r * r * dr * dr - D * D)) / (dr * dr) + joystickY;

                    float xc, yc;

                    if (y > joystickY) {
                        yc = Math.max(yPlus, yMinus);
                    } else {
                        yc = Math.min(yPlus, yMinus);
                    }

                    if (x >= joystickX) {
                        xc = Math.max(xPlus, xMinus);
                    } else {
                        xc = Math.min(xPlus, xMinus);
                    }

                    if(y > Math.max(yPlus, yMinus)) {
                        yc = Math.max(yPlus, yMinus);
                    } else if(y < Math.min(yPlus, yMinus)) {
                        yc = Math.min(yPlus, yMinus);
                    }

                    joystick.setPosition(xc, yc);

                }
            }
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(screenX < Gdx.graphics.getWidth() / 6.0f) {
            if (screenY - this.touchY > 23) {
                unicorn.moveDown(0);
            } else if (this.touchY - screenY > 23) {
                unicorn.moveUp(0);
            }
            this.touchY = Float.MIN_VALUE;
        } else {
            isFingerDown = false;
            float fingerX = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * screenX;
            float fingerY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * screenY;
            if(unicorn.unicornType == UnicornType.AUTOMATIC_ATTACK) {
                joystickTouched = false;
//            } else if(fingerX < changeUnicornType.getX() ||
//                    fingerX > changeUnicornType.getX() + changeUnicornType.getWidth() ||
//                    fingerY < changeUnicornType.getY() ||
//                    fingerY > changeUnicornType.getY() + changeUnicornType.getHeight()) {
//                joystickTouched = true;
            }

            joystick.addAction(Actions.moveTo(joystickX, joystickY, 0.05f));
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    public class CollisionDetector implements Runnable {

        public Array<GameActor> collisionListeners = new Array<GameActor>();

        @Override
        public void run() {
            for (int i = 0; i < collisionListeners.size - 1; i++) {
                for (int j = i + 1; j < collisionListeners.size; j++) {
                    GameActor a = collisionListeners.get(i);
                    GameActor b = collisionListeners.get(j);
                    if (!a.getClass().equals(b.getClass()) && Utils.intersects(a, b)) {
                        //bullet hit enemy
                        if (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsBullet(b)) {
                            final Enemy enemy = (Enemy) a;
                            final Bullet bullet = (Bullet) b;
                            if ((enemy.colorType == bullet.colorType || bullet.colorType == ColorType.RAINBOW) && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.hit(bullet.damage);
                                bullet.destroy();
                            } else if(enemy instanceof AttackingEnemy && enemy.isAttacking() && !enemy.isAttackingOnUnicorn){
//                                enemy.attack();
                                bullet.destroy();
                            } else {
                                bullet.destroy();
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsEnemy(b) && BodyUtils.bodyIsBullet(a)) {
                            final Enemy enemy = (Enemy) b;
                            final Bullet bullet = (Bullet) a;

                            if ((enemy.colorType == bullet.colorType || bullet.colorType == ColorType.RAINBOW) && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.hit(bullet.damage);
                                bullet.destroy();
                            } else {
                                bullet.destroy();
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(a) && BodyUtils.bodyIsBullet(b)) {
                            final Enemy enemy = (Enemy) a;
                            final Bullet bullet = (Bullet) b;
                            enemy.hit(bullet.damage);
                            bullet.destroy();
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(b) && BodyUtils.bodyIsBullet(a)) {
                            final Enemy enemy = (Enemy) b;
                            final Bullet bullet = (Bullet) a;
                            enemy.hit(bullet.damage);
                            bullet.destroy();
                            break;
                        }

                        //enemy hit unicorn
                        if (BodyUtils.bodyIsUnicorn(a) && BodyUtils.bodyIsEnemy(b)) {
                            unicorn.hit(1);
                            final Enemy enemy = (Enemy) b;
                            enemy.die();
                            break;
                        }
                        if (BodyUtils.bodyIsUnicorn(b) && BodyUtils.bodyIsEnemy(a)) {
                            unicorn.hit(1);
                            final Enemy enemy = (Enemy) a;
                            enemy.die();
                            break;
                        }

                        //mini unicorn hit enemy
                        if (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsMiniUnicorn(b)) {
                            final Enemy enemy = (Enemy) a;
                            final MiniUnicorn miniUnicorn = (MiniUnicorn) b;
                            if (enemy.colorType == miniUnicorn.colorType && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.die();
                                miniUnicorn.remove();
                            } else {
                                miniUnicorn.remove();
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsEnemy(b) && BodyUtils.bodyIsMiniUnicorn(a)) {
                            final Enemy enemy = (Enemy) b;
                            final MiniUnicorn miniUnicorn = (MiniUnicorn) a;
                            if (enemy.colorType == miniUnicorn.colorType && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.die();
                                miniUnicorn.remove();
                            } else {
                                miniUnicorn.remove();
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(a) && BodyUtils.bodyIsMiniUnicorn(b)) {
                            Enemy boss = (Enemy)a;
                            MiniUnicorn miniUnicorn = (MiniUnicorn)b;
                            boss.hit(miniUnicorn.damage);
                            miniUnicorn.remove();
                        }
                        if (BodyUtils.bodyIsBoss(b) && BodyUtils.bodyIsMiniUnicorn(a)) {
                            Enemy boss = (Enemy)b;
                            MiniUnicorn miniUnicorn = (MiniUnicorn)a;
                            boss.hit(miniUnicorn.damage);
                            miniUnicorn.remove();
                        }

                        //bullet hit unicorn
                        if (BodyUtils.bodyIsUnicorn(a) && BodyUtils.bodyIsEnemyBullet(b)) {
                            final Unicorn unicorn = (Unicorn) a;
                            final Bullet bullet = (Bullet) b;
                            unicorn.hit(bullet.damage);
                            bullet.destroy();
                            break;
                        }
                        if (BodyUtils.bodyIsUnicorn(b) && BodyUtils.bodyIsEnemyBullet(a)) {
                            final Unicorn unicorn = (Unicorn) b;
                            final Bullet bullet = (Bullet) a;
                            unicorn.hit(bullet.damage);
                            bullet.destroy();
                            break;
                        }

                        //bullet hit spell
                        if((BodyUtils.bodyIsBullet(a) || BodyUtils.bodyIsCannonBullet(a)) && BodyUtils.bodyIsSpell(b)) {
                            if(spellCandy.spellType == SpellType.CALL_UNICORNS) {
                                unicorn.callUnicorns();
                            } else if(spellCandy.spellType == SpellType.RAINBOW_MODE){
                                unicorn.enableRainbowMode();
                            } else if(spellCandy.spellType == SpellType.HEALTH) {
                                unicorn.regenerate();
                            }
                            b.remove();
                            collisionListeners.removeValue(b, false);
                            spellCandy = null;
                        }
                        if((BodyUtils.bodyIsBullet(b) || BodyUtils.bodyIsCannonBullet(b)) && BodyUtils.bodyIsSpell(a)) {
                            if(MathUtils.randomBoolean()) {
                                unicorn.callUnicorns();
                            } else {
                                unicorn.enableRainbowMode();
                            }
                            a.remove();
                            collisionListeners.removeValue(a, false);
                            spellCandy = null;
                        }
                    }
                }
            }
        }
    }

    public void layerStage() {
        Array<Actor> actors = this.getActors();
        try {
            long time = System.currentTimeMillis();
            actors.sort(actorsComporator);
        } catch (IllegalArgumentException ex) {
            Gdx.app.error("", ex.getMessage(), ex);
        }
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if(actor instanceof SpineActor) {
            ((SpineActor)actor).actorAddedToStage();
        }
        if(!(actor instanceof Bullet)) {
            layerStage();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!stopGame) {
            enemySendCounter += delta;
            enemySendTimer += delta;
            enemyMoveTimer += delta;
//            layeringTimer += delta;
//            if (layeringTimer >= layeringTime) {
//                layerStage();
//                layeringTimer = 0.0f;
//            }
            if (!game.tutorialManager.isTutorialMode) {

                if (sendWave) {
                    enemyAccelerationTimer += delta;
                    if (enemyAccelerationTimer >= _ENEMY_SEND_ACCELERATION_TIME) {
                        enemyAccelerationTimer = 0;
                        sendWave = false;
                        _ENEMY_SEND_ACCELERATION_TIME += 0.3f;
                        _ENEMY_SEND_TIME_STEP = normalEnemySendTimeStep;
                    }
                }

                if (enemySendCounter >= _ENEMY_SEND_TIME_STEP) {
                    if (!sendWave && MathUtils.random(100) < 50 && level == currentWaveLevel) {
                        sendWave = true;
                        normalEnemySendTimeStep = _ENEMY_SEND_TIME_STEP;
                        _ENEMY_SEND_TIME_STEP = 0.2f;
                        currentWaveLevel++;
                    }
                    if (deadEnemyCounter >= DEAD_ENEMIES_TILL_BOSS * 0.6f && _ENEMY_SEND_TIME_STEP != 1 && !sendWave) {
                        normalEnemySendTimeStep = _ENEMY_SEND_TIME_STEP;
                        _ENEMY_SEND_TIME_STEP = 1f;
                    }
                    int bossSendProb = deadEnemyCounter >= DEAD_ENEMIES_TILL_BOSS ? MathUtils.random(100) : 100;
                    if (bossSendProb <= 70) {
                        if(level == 1 && game.tutorialManager.currentStep == TutorialStep.FOURTH) {
                            game.tutorialManager.isTutorialMode = true;
                            boss = sendBoss(true);
                        } else {
                            boss = sendBoss();
                        }
                        _ENEMY_SEND_TIME_STEP = normalEnemySendTimeStep;
                        if (DEAD_ENEMIES_TILL_BOSS < MAX_DEAD_ENEMY_COUNT) {
                            DEAD_ENEMIES_TILL_BOSS += 10;
                        }
                        deadEnemyCounter = 0;
                    } else if (boss == null) {
                        sendEnemy();
                    }
                    enemySendCounter = 0.0f;
                }
                if (enemySendTimer >= _ENEMY_SEND_ACCELERATION_TIME_STEP && _ENEMY_SEND_TIME_STEP > _MIN_ENEMY_SEND_TIME_STEP) {
                    _ENEMY_SEND_TIME_STEP -= _ENEMY_SEND_ACCELERATION_STEP;
                    enemySendTimer = 0.0f;
                }
                if (enemyMoveTimer >= _ENEMY_MOVE_ACCELERATION_TIME_STEP && _ENEMY_MOVE_SPEED < _MAX_ENEMY_MOVE_SPEED) {
                    _ENEMY_MOVE_SPEED += _ENEMY_MOVE_ACCELERATION_STEP;
                    enemyMoveTimer = 0.0f;
                }

                if (unicornDie != null) {
                    unicornDie.die();
                    unicornDie = null;
                }

                if (enemyDie != null) {
                    enemyDie.die();
                    enemyDie = null;
                }
            } else {
                if (game.tutorialManager.currentStep == TutorialStep.FIRST) {
                    if (!game.tutorialManager.isFirstEnemySend) {
                        sendEnemy(true);
                        game.tutorialManager.isFirstEnemySend = true;
                    }
                } else if (game.tutorialManager.currentStep == TutorialStep.SECOND) {
                    if (!game.tutorialManager.isSecondEnemySend) {
                        colorIndices.add(2);
                        colorIndices.add(3);
                        colorIndices.add(1);
                        game.tutorialManager.isSecondEnemySend = true;
                    }
                    if (colorIndices.size > 0) {
                        if(!game.tutorialManager.pauseGame && !game.tutorialManager.isTutorialEnemyOnStage) {
                            sendEnemy(true);
                        }
                    } else if(!game.tutorialManager.pauseGame && !game.tutorialManager.isTutorialEnemyOnStage &&
                            game.tutorialManager.secondStepEnemies < TutorialManager.SECOND_STEP_ENEMIES_COUNT) {
                        if (enemySendCounter >= _ENEMY_SEND_TIME_STEP) {
                            sendEnemy();
                            enemySendCounter = 0;
                            game.tutorialManager.secondStepEnemies++;
                        }
                    }
                } else if(game.tutorialManager.currentStep == TutorialStep.THIRD) {
                    if(!game.tutorialManager.isThirdEnemiesInit) {
                        for (int i = 0; i < TutorialManager.THIRD_STEP_ENEMIES_COUNT - 1; i++) {
                            colorIndices.add(1);
                        }
                        colorIndices.add(3);
                        game.tutorialManager.isThirdEnemiesInit = true;
                    }
                    if (!game.tutorialManager.isThirdEnemySend && enemySendCounter >= TutorialManager.THIRD_STEP_ENEMIES_SEND_TIME) {
                        sendEnemy(true);
                        enemySendCounter = 0;
                        if(++game.tutorialManager.thirdStepEnemies == TutorialManager.THIRD_STEP_ENEMIES_COUNT) {
                            game.tutorialManager.isThirdEnemySend = true;
                        }
                    }
                } else if(game.tutorialManager.currentStep == TutorialStep.FOURTH) {

                }
            }
            fireTimer += delta;
            if(joystickTouched) {
                if(fireTimer >= unicorn.attackSpeed) {
                    float fingerX = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * fingerScreenX;
                    float fingerY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * fingerScreenY;

                    float aimX = fingerScreenX;
                    float aimY = Constants.VIEWPORT_HEIGHT / joystickRadius * 2 * fingerY;
                    aimY = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / Constants.VIEWPORT_HEIGHT * fingerY;

                    float deltaX = fingerX - joystickX - joystick.getWidth() * joystick.getScaleX() / 2;
                    float deltaY = fingerY - joystickY - joystick.getHeight() * joystick.getScaleY() / 2;
                    float angle = 0;
                    float minDelta = 0.5f;
                    if(deltaX > minDelta || deltaY > minDelta || deltaX < -minDelta || deltaY < -minDelta) {
//                        angle = (float) Math.toDegrees(Math.atan2(fingerX - joystickX + joystickRadius,
//                                fingerY - joystickY - joystick.getHeight() * joystick.getScaleY() / 2));
//                        angle = 90 - angle;
//                        if(angle > 0) {
//                            angle = (int) angle % 10 < 5 ? (int) angle / 10 * 10 : (int) angle / 10 * 10 + 5;
//                        } else {
//                            angle = (int) angle % 10 > -5 ? (int) angle / 10 * 10 : (int) angle / 10 * 10 - 5;
//                        }
                    }
//                    if(deltaX > minDelta || deltaY > minDelta || deltaX < -minDelta || deltaY < -minDelta) {
//                        angle = (float) Math.toDegrees(Math.atan2(aimX - unicorn.getX() - unicorn.getFirePoint().x,
//                                aimY - unicorn.getY() - unicorn.getFirePoint().y));
//                            angle = 90 - angle;
//                        if(angle > 0) {
//                            angle = (int) angle % 10 < 5 ? (int) angle / 10 * 10 : (int) angle / 10 * 10 + 5;
//                        } else {
//                            angle = (int) angle % 10 > -5 ? (int) angle / 10 * 10 : (int) angle / 10 * 10 - 5;
//                        }
//                    }

//                    if(unicorn.unicornType == UnicornType.UNICORN) {
//                        unicornFire(angle);
//                    } else {
                    unicornFire(fingerScreenX, fingerScreenY);
//                    }
                    fireTimer = 0.0f;

//                    Unicorn unicornchik = null;
//                    unicornchik.unicornType =UnicornType.UNICORN;

                    if(unicorn.unicornType != UnicornType.AUTOMATIC_ATTACK) {
                        joystickTouched = false;
//                    } else if(unicorn.unicornType == UnicornType.UNICORN) {
//                        if(++fireIndex >= level / 2) {
//                            fireIndex = 0;
//                            joystickTouched = false;
//                        }
                    }
                }
            }
            collisionDetector.run();
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}
