package com.frismos.unicorn.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.frismos.TweenAccessor.CameraAccessor;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.AttackingEnemy;
import com.frismos.unicorn.actor.Background;
import com.frismos.unicorn.actor.Boss;
import com.frismos.unicorn.actor.BouncingEnemy;
import com.frismos.unicorn.actor.Bullet;
import com.frismos.unicorn.actor.ChewingEnemy;
import com.frismos.unicorn.actor.ColorsPlatform;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.actor.MotherEnemy;
import com.frismos.unicorn.actor.FieldBackground;
import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.actor.HallBackground;
import com.frismos.unicorn.actor.HealthSpell;
import com.frismos.unicorn.actor.MainCharacter;
import com.frismos.unicorn.actor.MiniUnicorn;
import com.frismos.unicorn.actor.MotherBoss;
import com.frismos.unicorn.actor.RainbowSpell;
import com.frismos.unicorn.actor.RunningEnemy;
import com.frismos.unicorn.actor.ShootingBoss;
import com.frismos.unicorn.actor.ShootingEnemy;
import com.frismos.unicorn.actor.Spell;
import com.frismos.unicorn.actor.Star;
import com.frismos.unicorn.actor.TopLayer;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.actor.UnicornSpell;
import com.frismos.unicorn.actor.WalkingEnemy;
import com.frismos.unicorn.enums.BossType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.Direction;
import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.enums.TutorialStep;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.grid.Grid;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.manager.TutorialManager;
import com.frismos.unicorn.patterns.AttackCommand;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.util.BodyUtils;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Timer;
import com.frismos.unicorn.util.Utils;

import java.util.Comparator;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class GameStage extends SimpleStage {

    public ColorsPlatform colorsPlatform;

    public Image changeUnicornType;
    public Grid grid;
    public static Texture blackPixel;

    public Star unicorn;

    public ObjectMap<UnicornType, MainCharacter> unicorns = new ObjectMap<>();

    private ObjectMap<String, Background> backgrounds = new ObjectMap<>();
    public Background background;
    public int deadEnemyCounter = 0;

    public Boss boss;
    public int level = 1;

    public AttackCommand attackCommand;

    // set this v lues  ***** **** **** **** * *****************************************************
    public static float ENEMY_SEND_TIME_STEP = 3.0f;
    public static float ENEMY_SEND_ACCELERATION_TIME_STEP = 10f;
    public static float ENEMY_SEND_ACCELERATION_STEP = 0.1f;
    public static float MIN_ENEMY_SEND_TIME_STEP = 1.0f;

    public static float BOSS_MOVE_SPEED = 1.0f;
    public static float ENEMY_MOVE_SPEED = 3.0f;
    public static float ENEMY_MOVE_ACCELERATION_TIME_STEP = 15f;
    public static float ENEMY_MOVE_ACCELERATION_STEP = 0.05f;
    public static float MAX_ENEMY_MOVE_SPEED = 5.0f;

    public static final float VELOCITY_HORIZONTAL_FORCE = 2300.0f;
    public static final float VELOCITY_VERTICAL_FORCE = 1900.0f;

    public static final int ENEMY_MATCH_CHANCE = 50;
    public static final float ENEMY_SPAWN_MIDDLE_CHANCE = 30;

    public static final float BULLET_MOVE_SPEED = 100;
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

    public static final int ROW_LENGTH = 3;
    public static final int COLUMN_LENGTH = 8;

    private float enemyMoveTimer = 0.0f;
    private float enemySendCounter = 5.0f;
    private float enemySendTimer = 0.0f;

    private EnemyDie enemyDie;
    private EnemyDie unicornDie;
    public boolean restartGame = false;
    public boolean stopGame = false;

    private Array<Integer> directions = new Array<>();
    public Array<Integer> colorIndices = new Array<>();

    public int score = 0;
    public float timeMillis;

    private float timeTimer = 0;
    private static final int TIME_TIME_STEP = 1;

    private int colorRowsLength;

    private Vector2 screenZero;
    public boolean joystickTouched = false;

    private Image joystick;
    private Image joystickArea;
    private float joystickX;
    private float joystickY;
    private float joystickRadius;
    private float normalEnemySendTimeStep = ENEMY_SEND_TIME_STEP;

    public Spell spellCandy;

    public CollisionDetector collisionDetector = new CollisionDetector();
    private Runnable spellCandyRunnable = new Runnable() {
        @Override
        public void run() {
            collisionDetector.addListener(spellCandy);
        }
    };
    private boolean sendWave = false;
    private int currentWaveLevel = 1;
    private int fireIndex;

    private float layeringTime = 0.1f;
    private float layeringTimer = 0.0f;
    private Comparator<? super Actor> actorsComparator = new Comparator<Actor>() {
        @Override
        public int compare(Actor o1, Actor o2) {
            if (o1.getY() < o2.getY()) {
                return 1;
            } else if (o1.getY() > o2.getY()) {
                return -1;
            } else if (o1.getX() < o2.getX()) {
                return 1;
            } else if (o1.getX() > o2.getX()) {
                return -1;
            } else if (o1.getZIndex() > o2.getZIndex()) {
                return 1;
            } else if (o1.getZIndex() < o2.getZIndex()) {
                return -1;
            }

            return 0;
        }
    };
    public Array<Actor> actors = new Array<>();
    public boolean isWorldShaking = false;
    private Array<Runnable> runnableArray = new Array<>();
    private long time;
    private int touchDownCount;

    public GameStage(final UnicornGame game) {
        super(new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT), new PolygonSpriteBatch());
        this.game = game;

        blackPixel = new Texture("blackPixel.png");
        Image pixel = new Image(blackPixel);
        pixel.setSize(1, 1);

        background = new FieldBackground(this);
//        backgrounds.put(Strings.FIELD_BACKGROUND, background);
//        backgrounds.put(Strings.HALL_BACKGROUND, new HallBackground(this));
        grid = new Grid(this);
        addActor(grid);

        colorsPlatform = new ColorsPlatform(this);

        unicorn = new Star(this, UnicornType.STAR);
        unicorns.put(unicorn.unicornType, unicorn);
        attackCommand = new AttackCommand(unicorn);

//        Rhino rhino = new Rhino(this, UnicornType.RHINO);
//        rhino.setX(grid.grid[0][0].getX() + grid.tileWidth / 2 - rhino.getWidth() / 2);
//        rhino.setY(grid.grid[0][0].getY() + grid.tileHeight / 2 - rhino.getHeight() / 2);
//        unicorns.put(rhino.unicornType, rhino);
//
//        Unicorn star = new Unicorn(this, UnicornType.UNICORN);
//        star.setX(grid.grid[0][0].getX() + grid.tileWidth / 2 - star.getWidth() / 2);
//        star.setY(grid.grid[0][0].getY() + grid.tileHeight / 2 - star.getHeight() / 2);
//        unicorns.put(star.unicornType, star);

//        InputMultiplexer multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(this);
//        Gdx.input.setInputProcessor(multiplexer);

        addActor(background);
        addActor(colorsPlatform);
        colorsPlatform.setColorType(unicorn.colorType);
        screenZero = new Vector2();
        screenZero.x = Gdx.graphics.getWidth() / Constants.VIEWPORT_WIDTH * background.getZero().x;
        screenZero.y = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / Constants.VIEWPORT_HEIGHT * background.getZero().y;

        collisionDetector.addListener(unicorn);
        addActor(unicorn);
//        int length = GameStage.ROW_LENGTH > ColorType.values().length ? ColorType.values().length : GameStage.ROW_LENGTH;
//        for(int i = 0; i < length; i++) {
//            colorIndices.add(i);
//        }

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

        changeUnicornType = new Image(game.atlasManager.get("gfx/joystick.png", Texture.class));
        changeUnicornType.setSize(3, 3);
        changeUnicornType.setX(4.6875f);//// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
        changeUnicornType.setY(2.109375f);// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
        changeUnicornType.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                GameStage.this.game.gameCenterController.showLeaderboardView("");
//                character.callUnicorns();
                changeUnicorn();
                joystickTouched = false;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
//        addActor(changeUnicornType);
        float gutterWidth = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * Math.abs(getViewport().getRightGutterWidth());
        getCamera().translate(gutterWidth / 2, 0, 0);

        initConstants();

        Thread detector = new Thread(collisionDetector);
        detector.start();

        if(game.tutorialManager.isTutorialMode) {
            colorIndices.add(0);
            changeUnicornType.remove();
        }

//        getCamera().viewportWidth -= 0.2f;
//        getCamera().viewportHeight -= 0.2f;

        game.aiManager.setGameStage(this);
        game.aiManager.sendWaves(0);
//        sendBoss(BossType.SHOOTING);
        game.soundManager.playMusic();
    }

    public void restartGame() {
        game.soundManager.reset();
        game.soundManager.sounds.get(game.soundManager.currentSoundId).stop(game.soundManager.currentSoundId);
        game.restartGame = true;
        initConstants();
        game.timerManager.reset();
        game.tutorialManager = new TutorialManager(game);
        this.clear();
        game.aiManager.reset();
        game.uiScreen.stage.addLabels();
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

    public void shakeWorld(int count) {
        if(count == 0) {
            isWorldShaking = false;
            return;
        }
        isWorldShaking = true;
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
        unicorn.reset();
        unicorn.remove(false);
        Tile tile = unicorn.tile;
//        ColorType colorType = unicorn.colorType;
//        if(unicorn.unicornType == UnicornType.STAR) {
//            if(game.tutorialManager.isTutorialMode) {
//                unicorn = unicorns.get(UnicornType.RHINO);
//            } else {
//                unicorn = unicorns.get(UnicornType.UNICORN);
//            }
//        } else if(unicorn.unicornType == UnicornType.RHINO) {
//            unicorn = unicorns.get(UnicornType.STAR);
//        } else if(unicorn.unicornType == UnicornType.UNICORN) {
//            unicorn = unicorns.get(UnicornType.RHINO);
//        }
        unicorn.setUnicornType(unicorn.unicornType);
        unicorn.tile = tile;
        unicorn.updatePosition();
//        unicorn.setColorType(colorType);
        collisionDetector.addListener(unicorn);
        addActor(unicorn);

        if(game.tutorialManager.isTutorialMode) {
            if(game.tutorialManager.currentStep == TutorialStep.THIRD) {
                if(unicorn.unicornType == UnicornType.RHINO) {
                    game.tutorialManager.removeArrow();
                }
            } else if(unicorn.unicornType == UnicornType.STAR || game.tutorialManager.currentStep == TutorialStep.FOURTH) {
                game.tutorialManager.removeArrow();
                game.tutorialManager.currentStep = TutorialStep.FINISH;
                game.dataManager.setTutorialPassed(true);
            }
        }
    }

    public Boss sendBoss() {
        return sendBoss(false);
    }

    public Boss sendBoss(BossType bossType) {
        return sendBoss(bossType, false);
    }

    public Boss sendBoss(boolean isTutorial) {
        return sendBoss(null, isTutorial);
    }

    public Boss sendBoss(BossType bossType, boolean isTutorial) {
        ColorType colorType = ColorType.getRandomColor();
        Boss boss;
        boolean isShooting = bossType == BossType.SHOOTING ? true : bossType == BossType.MOTHER ? false : MathUtils.randomBoolean();
        if(isShooting) {
            boss = new ShootingBoss(this, colorType, isTutorial);
        } else {
            boss = new MotherBoss(this, colorType, isTutorial);
        }
        game.tutorialManager.isTutorialEnemyOnStage = isTutorial;
        collisionDetector.addListener(boss);
        addActor(boss);
        this.boss = boss;
        return boss;
    }

    public Enemy sendWave(WaveType waveType) {
        ColorType colorType = ColorType.getRandomColor();
        Enemy enemy = null;
//        switch (waveType) {
//            case WALKING:
//                enemy = MathUtils.randomBoolean() ? new WalkingEnemy(this, colorType) : new MotherEnemy(this, colorType);
//                break;
//            case RUNNING:
//                enemy = new RunningEnemy(this, colorType);
//                break;
//            case CHEWING:
                enemy = new ChewingEnemy(this, colorType);//// TODO: 4/10/16  
//                break;
//            case BOUNCING:
//                enemy = new BouncingEnemy(this, colorType);
//                break;
//            case ATTACKING:
//                enemy = new AttackingEnemy(this, colorType);
//                break;
//            case SHOOTING:
//                enemy = new ShootingEnemy(this, colorType);
//        }
        addActor(enemy);
        collisionDetector.addListener(enemy);
        return enemy;
    }

    public Enemy sendEnemy() {
        return sendEnemy(false);
    }

    private Enemy sendEnemy(boolean isTutorial) {
        ColorType colorType;
        Enemy enemy;
        ATTACKING_ENEMY_SPAWN_CHANCE = level == 1 || sendWave ? 0 : 10;
        if(colorIndices.size > 0) {
            int colorIndex = colorIndices.get(MathUtils.random(colorIndices.size - 1));
            colorType = ColorType.values()[colorIndex];
            if(game.tutorialManager.currentStep == TutorialStep.THIRD &&
                    game.tutorialManager.thirdStepColor != colorType) {
                isTutorial = false;
            }
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
        collisionDetector.addListener(enemy);
        if(game.tutorialManager.isTutorialMode) {
            game.tutorialManager.enemies.add(enemy);
        }

        return enemy;
    }

    public void putSpell(float x, float y) {
        if(spellCandy == null) {
            SpellType spellType = SpellType.getRandomValue(unicorn.hitPoints < Unicorn.MAX_HIT_POINTS);
            switch(spellType) {
                case HEALTH:
                    spellCandy = new HealthSpell(this);
                    break;
                case CALL_UNICORNS:
                    spellCandy = new UnicornSpell(this);
                    break;
                case RAINBOW_MODE:
                    spellCandy = new RainbowSpell(this);
                    break;

            }
            spellCandy.setScale(0);
            spellCandy.setX(x);
            spellCandy.setY(y);
            addActor(spellCandy);
            spellCandy.addAction(Actions.sequence(Actions.scaleTo(1f, 1f, 1.5f), Actions.run(spellCandyRunnable)));
        }
    }

    public void nextLevel() {
        level++;

//        String backgroundKey = level % 2 == 0 ? Strings.HALL_BACKGROUND : Strings.FIELD_BACKGROUND;
//        final Background background = backgrounds.get(backgroundKey);
//        this.addActor(background);
//        background.setZIndex(this.background.getZIndex());
//        background.setX(Constants.VIEWPORT_WIDTH);
//        background.addAction(Actions.moveTo(0, 0, 1.5f));
//        this.background.addAction(Actions.sequence(Actions.moveTo(-Constants.VIEWPORT_WIDTH, 0, 1.5f), Actions.run(new Runnable() {
//            @Override
//            public void run() {
//                GameStage.this.background.remove();
//                GameStage.this.background = background;
//            }
//        })));
        game.aiManager.sendWaves(0);
    }

    public void runOnGameThread(Runnable runnable) {
        runnableArray.add(runnable);
    }

    public interface EnemyDie {
        void die();
    }

//    public void unicornFire(float angle) {
//        if(!stopGame) {
//            unicorn.playFireAnimation(angle);
//        }
//    }

    public void unicornFire(float x, float y) {
        if(!stopGame) {
            unicorn.playFireAnimation(x, y);
        }
    }

    public void fireBullet(float angle) {
        if(unicorn.colorType != null) {
            Bullet bullet = getBullet();
            bullet.setAngle(angle);
            ColorType colorType = unicorn.colorType;

            bullet.setColorType(colorType);
            bullet.setX(unicorn.getFirePoint().x + unicorn.getX());
            bullet.setY(unicorn.getFirePoint().y + unicorn.getY());
            bullet.fire();

            collisionDetector.addListener(bullet);
            addActor(bullet);
        }
    }

    public Bullet getBullet() {
        Bullet bullet = unicorn.getNextBullet();
        return bullet;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            restartGame = true;
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
    private boolean isSwiped = false;
    private float fingerScreenX, fingerScreenY;
    private float deltaY;
    private boolean touchUp = true;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(screenX <= Gdx.graphics.getWidth() / 6.0f) {
            this.touchY = screenY;
            this.deltaY = 0;
            float fingerY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * screenY;
            if(fingerY < background.getZero().y + grid.tileHeight) {
                unicorn.moveTo(grid.grid[0][0]);
            } else if(fingerY < background.getZero().y + 2 * grid.tileHeight) {
                unicorn.moveTo(grid.grid[1][0]);
            } else {
                unicorn.moveTo(grid.grid[2][0]);
            }

        } else {
            float stageX = screenX * Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() - joystick.getWidth() * joystick.getScaleX() / 2;
            float stageY = (Gdx.graphics.getHeight() - screenY) * Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() - joystick.getHeight() * joystick.getScaleY() / 2;
//            if(character.unicornType == UnicornType.UNICORN) {
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
//            if(character.unicornType == UnicornType.UNICORN) {
            if(stageX < changeUnicornType.getX() ||
                    stageX > changeUnicornType.getX() + changeUnicornType.getWidth() ||
                    stageY < changeUnicornType.getY() ||
                    stageY > changeUnicornType.getY() + changeUnicornType.getHeight()) {
                if(!game.tutorialManager.isTutorialMode) {
                    attackCommand.setTouchCoordinates(screenX, screenY);
                    attackCommand.execute();
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
                        } else if(game.tutorialManager.currentStep == TutorialStep.FOURTH) {
                            joystickTouched = true;
                            fireIndex = 0;
                        } else if(game.tutorialManager.currentStep == TutorialStep.FINISH) {
                            if(unicorn.unicornType == UnicornType.STAR) {
                                joystickTouched = true;
                                fireIndex = 0;
                            }
                        }
                    }
                }
            }
//            }

            fingerScreenX = screenX;
            fingerScreenY = screenY;
            touchUp = false;
            touchDownCount++;
            time = System.currentTimeMillis();
        }
        isSwiped = false;

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(screenX > Gdx.graphics.getWidth() / 6.0f) {
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
        } else {
            this.touchY = screenY;
            this.deltaY = 0;
            float fingerY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * screenY;
            if(fingerY < background.getZero().y + grid.tileHeight) {
                unicorn.moveTo(grid.grid[0][0]);
            } else if(fingerY < background.getZero().y + 2 * grid.tileHeight) {
                unicorn.moveTo(grid.grid[1][0]);
            } else {
                unicorn.moveTo(grid.grid[2][0]);
            }
////            if(!isSwiped) {
//            deltaY = screenY - this.touchY;
//            float dragDeltaY = !isSwiped ? 50 : 100;
//                if (deltaY > dragDeltaY) {
//                    unicorn.moveDown(0);
//                    this.touchY = screenY;
//                    isSwiped = true;
//                } else if (-deltaY > dragDeltaY) {
//                    unicorn.moveUp(0);
//                    isSwiped = true;
//                    this.touchY = screenY;
//                }
////            }
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(screenX > Gdx.graphics.getWidth() / 6.0f) {
//            if(unicorn.unicornType == UnicornType.UNICORN) {
//            joystickTouched = false;
            touchUp = true;
            touchDownCount--;
//            } else if(fingerX < changeUnicornType.getX() ||
//                    fingerX > changeUnicornType.getX() + changeUnicornType.getWidth() ||
//                    fingerY < changeUnicornType.getY() ||
//                    fingerY > changeUnicornType.getY() + changeUnicornType.getHeight()) {
//                joystickTouched = true;
//            }

//            joystick.addAction(Actions.moveTo(joystickX, joystickY, 0.05f));
        } else {
//            if(deltaY <= 10) {
//                float fingerX = Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * screenX;
//                float fingerY = Constants.VIEWPORT_HEIGHT - Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight() * screenY;
//
//                if(fingerY > background.getZero().y) {
//                    if(fingerY < background.getZero().y + grid.tileHeight) {
//                        unicorn.moveTo(grid.grid[0][0]);
//                    } else if(fingerY < background.getZero().y + 2 * grid.tileHeight) {
//                        unicorn.moveTo(grid.grid[1][0]);
//                    } else if(fingerY < background.getZero().y + 3 * grid.tileHeight) {
//                        unicorn.moveTo(grid.grid[2][0]);
//                    }
//                }
//
//            }
            this.touchY = Float.MIN_VALUE;
            this.deltaY = 0;
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    public class CollisionDetector implements Runnable {

        public Array<GameActor> collisionListeners = new Array<>();

        public void addListener(GameActor actor) {
            if(!collisionListeners.contains(actor, false)) {
                collisionListeners.add(actor);
            }
        }

        public void removeListenerActor(GameActor actor) {
            collisionListeners.removeValue(actor, false);
        }

        public void hitEnemy(final Bullet bullet, Enemy enemy) {
            if ((enemy.colorType == bullet.colorType || bullet.colorType == ColorType.RAINBOW) && enemy.isAttacking() && !bullet.isHit) {
                enemy.hit(bullet.damage + unicorn.combo, bullet);
            }
            if(!enemy.isAttacking()) {
                bullet.isHit = true;
            }
            if(!bullet.mark) {
                bullet.mark = true;
                game.timerManager.run(0.015f, new TimerRunnable() {
                    @Override
                    public void run(Timer timer) {
                        bullet.destroy();
                    }
                });
            }
        }

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
                            hitEnemy(bullet, enemy);
                            break;
                        }
                        if (BodyUtils.bodyIsEnemy(b) && BodyUtils.bodyIsBullet(a)) {
                            final Enemy enemy = (Enemy) b;
                            final Bullet bullet = (Bullet) a;
                            hitEnemy(bullet, enemy);
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(a) && BodyUtils.bodyIsBullet(b)) {
                            final Enemy enemy = (Enemy) a;
                            final Bullet bullet = (Bullet) b;
                            hitEnemy(bullet, enemy);
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(b) && BodyUtils.bodyIsBullet(a)) {
                            final Enemy enemy = (Enemy) b;
                            final Bullet bullet = (Bullet) a;
                            hitEnemy(bullet, enemy);
                            break;
                        }

                        //enemy hit character
//                        if (BodyUtils.bodyIsUnicorn(a) && BodyUtils.bodyIsEnemy(b)) {
//                            unicorn.hit(1);
//                            final Enemy enemy = (Enemy) b;
//                            enemy.die();
//                            break;
//                        }
//                        if (BodyUtils.bodyIsUnicorn(b) && BodyUtils.bodyIsEnemy(a)) {
//                            unicorn.hit(1);
//                            final Enemy enemy = (Enemy) a;
//                            enemy.die();
//                            break;
//                        }

                        //mini character hit enemy
                        if (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsMiniUnicorn(b)) {
                            final Enemy enemy = (Enemy) a;
                            final MiniUnicorn miniUnicorn = (MiniUnicorn) b;
                            if (enemy.colorType == miniUnicorn.colorType && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.die();
                                miniUnicorn.remove(false);
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsEnemy(b) && BodyUtils.bodyIsMiniUnicorn(a)) {
                            final Enemy enemy = (Enemy) b;
                            final MiniUnicorn miniUnicorn = (MiniUnicorn) a;
                            if (enemy.colorType == miniUnicorn.colorType && enemy.isAttacking()) {// && (enemy.tile.colorType == null || enemy.tile.colorType == enemy.colorType)) {
                                enemy.die();
                                miniUnicorn.remove(false);
                            }
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(a) && BodyUtils.bodyIsMiniUnicorn(b)) {
                            Enemy boss = (Enemy)a;
                            MiniUnicorn miniUnicorn = (MiniUnicorn)b;
                            boss.hit(miniUnicorn.damage);
                            miniUnicorn.remove(false);
                            break;
                        }
                        if (BodyUtils.bodyIsBoss(b) && BodyUtils.bodyIsMiniUnicorn(a)) {
                            Enemy boss = (Enemy)b;
                            MiniUnicorn miniUnicorn = (MiniUnicorn)a;
                            boss.hit(miniUnicorn.damage);
                            miniUnicorn.remove(false);
                            break;
                        }

                        //bullet hit character
                        if (BodyUtils.bodyIsUnicorn(a) && BodyUtils.bodyIsEnemyBullet(b)) {
                            final MainCharacter unicorn = (MainCharacter) a;
                            final Bullet bullet = (Bullet) b;
                            unicorn.hit(bullet.damage, bullet);
                            bullet.destroy();
                            break;
                        }
                        if (BodyUtils.bodyIsUnicorn(b) && BodyUtils.bodyIsEnemyBullet(a)) {
                            final MainCharacter unicorn = (MainCharacter) b;
                            final Bullet bullet = (Bullet) a;
                            unicorn.hit(bullet.damage, bullet);
                            bullet.destroy();
                            break;
                        }


                        //TODO THIS IS TEMPORARY WAY
                        //bullet hit spell
                        if((BodyUtils.bodyIsBullet(a) || BodyUtils.bodyIsCannonBullet(a)) && BodyUtils.bodyIsSpell(b)) {
                            if(spellCandy.spellType == SpellType.HEALTH) {
                                unicorn.regenerate();
                            } else if(spellCandy.spellType == SpellType.RAINBOW_MODE) {
                                unicorn.setRainbowMode();
                            } else if (spellCandy.spellType == SpellType.CALL_UNICORNS){
                                unicorn.callUnicorns();
                            }
                            ((Spell)b).playClaimAnimation();
                            spellCandy = null;
                            ((Bullet)a).destroy();
                            break;
                        }
                        if((BodyUtils.bodyIsBullet(b) || BodyUtils.bodyIsCannonBullet(b)) && BodyUtils.bodyIsSpell(a)) {
                            if(spellCandy.spellType == SpellType.HEALTH) {
                                unicorn.regenerate();
                            } else if(spellCandy.spellType == SpellType.RAINBOW_MODE){
                                unicorn.setRainbowMode();
                            } else if (spellCandy.spellType == SpellType.CALL_UNICORNS){
                                unicorn.callUnicorns();
                            }
                            ((Spell)a).playClaimAnimation();
                            spellCandy = null;
                            ((Bullet)b).destroy();
                            break;
                        }
                    }
                }
            }
        }
    }

    public void layerStage(Actor actor) {
        try {
            if (actor != null) {
                if(actor instanceof MainCharacter ||
                        actor instanceof Enemy ||
                        actor instanceof MiniUnicorn) {
                    actors.add(actor);
                }
                actors.sort(actorsComparator);
                for (int i = 0; i < actors.size; i++) {
                    actors.get(i).setZIndex(1000 + i + colorsPlatform.getZIndex() + game.aiManager.enemies.size);
                }
            }
        } catch (IllegalArgumentException ex) {
            Gdx.app.error("", ex.getMessage(), ex);
        }
    }

    private TopLayer topLayer;

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if(actor instanceof TopLayer) {
            topLayer = (TopLayer) actor;
        }
        if(actor instanceof SpineActor) {
            ((SpineActor)actor).actorAddedToStage();
        }
        layerStage(actor);
        if(topLayer != null) {
            topLayer.toFront();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!stopGame) {
            timeMillis += delta;
            timeTimer += delta;
            if(timeTimer >= TIME_TIME_STEP) {
                int seconds = (int)((GameScreen)game.getScreen()).stage.timeMillis;
                int minutes = seconds / 60;
                seconds %= 60;
                game.uiScreen.stage.timeLabel.setText(String.format("%s:%s", minutes, seconds >= 10 ? seconds : String.format("0%s", seconds)));
            }
            enemySendCounter += delta;
            enemySendTimer += delta;
            enemyMoveTimer += delta;
//            layeringTimer += delta;
//            if (layeringTimer >= layeringTime) {
//                layerStage();
//                layeringTimer = 0.0f;
//            }
            if (!game.tutorialManager.isTutorialMode) {
//                if (sendWave) {
//                    enemyAccelerationTimer += delta;
//                    if (enemyAccelerationTimer >= _ENEMY_SEND_ACCELERATION_TIME) {
//                        enemyAccelerationTimer = 0;
//                        sendWave = false;
//                        _ENEMY_SEND_ACCELERATION_TIME += 0.3f;
//                        _ENEMY_SEND_TIME_STEP = normalEnemySendTimeStep;
//                    }
//                }
//
//                if (enemySendCounter >= _ENEMY_SEND_TIME_STEP) {
//                    if (!sendWave && MathUtils.random(100) < 50 && level == currentWaveLevel) {
//                        sendWave = true;
//                        normalEnemySendTimeStep = _ENEMY_SEND_TIME_STEP;
//                        _ENEMY_SEND_TIME_STEP = 0.2f;
//                        currentWaveLevel++;
//                    }
//                    if (deadEnemyCounter >= DEAD_ENEMIES_TILL_BOSS * 0.6f && _ENEMY_SEND_TIME_STEP != 1 && !sendWave) {
//                        normalEnemySendTimeStep = _ENEMY_SEND_TIME_STEP;
//                        _ENEMY_SEND_TIME_STEP = 1f;
//                    }
//                    int bossSendProb = deadEnemyCounter >= DEAD_ENEMIES_TILL_BOSS ? MathUtils.random(100) : 100;
//                    if (bossSendProb <= 70) {
//                        boss = sendBoss();
//                        _ENEMY_SEND_TIME_STEP = normalEnemySendTimeStep;
//                        if (DEAD_ENEMIES_TILL_BOSS < MAX_DEAD_ENEMY_COUNT) {
//                            DEAD_ENEMIES_TILL_BOSS += 10;
//                        }
//                        deadEnemyCounter = 0;
//                    } else if (boss == null) {
//                        sendEnemy();
//                    }
//                    enemySendCounter = 0.0f;
//                }
//                if (enemySendTimer >= _ENEMY_SEND_ACCELERATION_TIME_STEP && _ENEMY_SEND_TIME_STEP > _MIN_ENEMY_SEND_TIME_STEP) {
//                    _ENEMY_SEND_TIME_STEP -= _ENEMY_SEND_ACCELERATION_STEP;
//                    enemySendTimer = 0.0f;
//                }
//                if (enemyMoveTimer >= _ENEMY_MOVE_ACCELERATION_TIME_STEP && _ENEMY_MOVE_SPEED < _MAX_ENEMY_MOVE_SPEED) {
//                    _ENEMY_MOVE_SPEED += _ENEMY_MOVE_ACCELERATION_STEP;
//                    enemyMoveTimer = 0.0f;
//                }



                if (unicornDie != null) {
                    unicornDie.die();
                    unicornDie = null;
                }

                if (enemyDie != null) {
                    enemyDie.die();
                    enemyDie = null;
                }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
/*@@@@@@@@*/} else {//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ TUTORIAL ACTIONS  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
                Enemy enemy = null;
                if (game.tutorialManager.currentStep == TutorialStep.FIRST) {
                    if (!game.tutorialManager.isFirstEnemySend) {
                        enemy = sendEnemy(true);
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
                            enemy = sendEnemy(true);
                        }
                    } else if(!game.tutorialManager.pauseGame && !game.tutorialManager.isTutorialEnemyOnStage &&
                            game.tutorialManager.secondStepEnemies < TutorialManager.SECOND_STEP_ENEMIES_COUNT) {
                        if (enemySendCounter >= _ENEMY_SEND_TIME_STEP) {
                            enemy = sendEnemy();
                            enemySendCounter = 0;
                            game.tutorialManager.secondStepEnemies++;
                        }
                    }
                } else if(game.tutorialManager.currentStep == TutorialStep.THIRD) {
                    if(!game.tutorialManager.isThirdEnemiesInit) {
                        addActor(changeUnicornType);
                        for (int i = 0; i < TutorialManager.THIRD_STEP_ENEMIES_COUNT - 1; i++) {
                            colorIndices.add(1);
                        }
                        colorIndices.add(3);
                        game.tutorialManager.isThirdEnemiesInit = true;
                    }
                    if (!game.tutorialManager.isThirdEnemySend && enemySendCounter >= TutorialManager.THIRD_STEP_ENEMIES_SEND_TIME) {
                        enemy = sendEnemy(true);
                        enemySendCounter = 0;
                        if(++game.tutorialManager.thirdStepEnemies == TutorialManager.THIRD_STEP_ENEMIES_COUNT) {
                            game.tutorialManager.isThirdEnemySend = true;
                        }
                    }
                } else if(game.tutorialManager.currentStep == TutorialStep.FOURTH) {
                    if(!game.tutorialManager.pauseGame && !game.tutorialManager.isTutorialEnemyOnStage &&
                            game.tutorialManager.fourthStepEnemies < TutorialManager.FOURTH_STEP_ENEMIES_COUNT) {
                        if (enemySendCounter >= _ENEMY_SEND_TIME_STEP) {
                            enemy = sendEnemy();
                            enemySendCounter = 0;
                            game.tutorialManager.fourthStepEnemies++;
                        }
                    }
                }

                if(game.tutorialManager.enemies.size > 0) {
                    if(unicorn.colorType != game.tutorialManager.enemies.get(0).colorType) {
                        Direction direction;
                        if(game.tutorialManager.enemies.get(0).colorType.ordinal() > unicorn.colorType.ordinal()) {
                            direction = Direction.UP;
                        } else {
                            direction = Direction.DOWN;
                        }
                        game.tutorialManager.showSlideArrow(this, direction);
                    }
                }
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
/*&&&&&&&&*/}//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& END OF TUTORIAL ACTIONS  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
            attackCommand.update();
            collisionDetector.run();
        }
        for (Runnable runnable : runnableArray) {
            runnable.run();
        }
        runnableArray.clear();
        if(restartGame) {
            restartGame();
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}
