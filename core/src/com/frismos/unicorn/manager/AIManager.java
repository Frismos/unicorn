package com.frismos.unicorn.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.enums.BossType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.SendingPattern;
import com.frismos.unicorn.util.Timer;

/**
 * Created by eavanyan on 2/9/16.
 */
public class AIManager {

    private Array<Float> timeArray = new Array<>();
    public Array<Enemy> enemies = new Array<>();
    private Array<Timer> timers = new Array<>();
    private Array<Float> sendTimeArray = new Array<>();
    private Array<Float> timeStepArray = new Array<>();
    private Timer timer;

    private Timer checkNextWaveTimer;
    private GameStage gameStage;
    private static final float SECOND_WAVE_SEND_TIME = 20;
    private static final float THIRD_WAVE_SEND_TIME = 6;
    private static final float FOURTH_WAVE_SEND_TIME = 6;
    private static final float FIFTH_WAVE_SEND_TIME = 6;
    private static final float SIXTH_WAVE_SEND_TIME = 1;

    private static final float WALKING_ENEMY_SEND_TIME_STEP = 1.4f;
    private static final float RUNNING_ENEMY_SEND_TIME_STEP = 0.9f;
    private static final float BOUNCING_ENEMY_SEND_TIME_STEP = 1.0f;
    private static final float ATTACKING_ENEMY_SEND_TIME_STEP = 1.1f;
    private static final float SHOOTING_ENEMY_SEND_TIME_STEP = 1.2f;

    public static float MOTHER_ENEMY_HP = 2.5f;
    public static float WALKING_ENEMY_HP = 2.1f;
    public static float RUNNING_ENEMY_HP = 1.1f;
    public static float BOUNCING_ENEMY_HP = 1.5f;
    public static float ATTACKING_ENEMY_HP = 3.5f;
    public static float SHOOTING_ENEMY_HP = 2.6f;

    private int enemyTypeIndex;
    public int currentIndex;

    private int level = 0;
    private Array<SendingPattern> patterns = new Array<>();
    private boolean isPattern = false;
    private int patternIndex;
    private int sendingPatternIndex = -1;
    private int sentEnemiesCount = 0;

    private int globalWaveIndex = 0;

    public AIManager() {
        init(level);
    }

    public void init(int globalLevel) {
        float level = globalLevel;
        patterns.clear();
        patterns.addAll(
                new SendingPattern(3, ColorType.RED),
                new SendingPattern(3, ColorType.GREEN),
                new SendingPattern(3, ColorType.BLUE),
                new SendingPattern(3, ColorType.RED)//,
                //new SendingPattern(3)
        );
        patternIndex = patterns.size;
        isPattern = false;
        sentEnemiesCount = 0;

        WALKING_ENEMY_HP += level * 0.5f;
        RUNNING_ENEMY_HP += level * 0.5f;
        BOUNCING_ENEMY_HP += level * 0.5f;
        ATTACKING_ENEMY_HP += level * 0.5f;
        SHOOTING_ENEMY_HP += level * 0.5f;

        sendTimeArray.clear();
        timeStepArray.clear();
        timers.clear();
        timeArray.clear();
        timeArray.addAll(THIRD_WAVE_SEND_TIME,
                RUNNING_ENEMY_SEND_TIME_STEP - 0.7f * level,
                FOURTH_WAVE_SEND_TIME,
                BOUNCING_ENEMY_SEND_TIME_STEP - 0.5f * level,
                FIFTH_WAVE_SEND_TIME,
                ATTACKING_ENEMY_SEND_TIME_STEP - 0.5f * level,
                SIXTH_WAVE_SEND_TIME,
                SHOOTING_ENEMY_SEND_TIME_STEP - 0.5f * level);
        sendTimeArray.add(SECOND_WAVE_SEND_TIME);
        timeStepArray.add(WALKING_ENEMY_SEND_TIME_STEP - 0.5f * level);
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void setGameStage(GameStage stage) {
        gameStage = stage;
    }

    public void sendWaves(final int index) {
        currentIndex = index;
//        enemies.add(gameStage.sendWave(WaveType.values()[index]));
        timer = gameStage.game.timerManager.loop(timeStepArray.get(index), new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                sendEnemy(index);
            }
        });
        timers.add(timer);
        addWaveType(index);
    }

    public void sendEnemy(final int index) {
        int size = gameStage.unicorn.getCombo() > 15 ? 7 : 5;
        if(!isPattern && enemies.size >= 6) {
            gameStage.game.timerManager.run(0.1f, new TimerRunnable() {
                @Override
                public void run(Timer timer) {
                    sendEnemy(index);
                }
            });
        } else {
            timer.reset();
            if (timer.getTimeStep() > 0.5f) {
                timer.setTimeStep(timer.getTimeStep() - 0.015f);
                if (timers.contains(timer, false)) {
                    timeStepArray.set(timers.indexOf(timer, false), timer.getTimeStep());
                }
            }
            if (!isPattern) {
                if (sentEnemiesCount >= 2) {
                    sendingPatternIndex = MathUtils.random(patterns.size - 1);
                    if (MathUtils.random(100) < 50) {
                        timer.setTimeStep(0.0f);
                        isPattern = true;
                        patternIndex = 0;
                        if (!patterns.get(sendingPatternIndex).isVertical) {
                            patterns.get(sendingPatternIndex).rowIndex = MathUtils.random(GameStage.ROW_LENGTH - 1);
                            patterns.get(sendingPatternIndex).colorTypes.shuffle();
                        }
                        do {
                            int type = enemyTypeIndex;
                            if (enemyTypeIndex == 2) {
                                type--;
                            }
                            patterns.get(sendingPatternIndex).enemyType = WaveType.values()[MathUtils.random(type, index)];
                        } while (patterns.get(sendingPatternIndex).enemyType == WaveType.BOUNCING);
                    }
                }
            }
            sentEnemiesCount++;
            int sendIndex = MathUtils.random(enemyTypeIndex, globalWaveIndex % 2 == 1 ? index : enemyTypeIndex);
            if (sendingPatternIndex != -1) {
                if (patternIndex < patterns.get(sendingPatternIndex).patternEnemiesCount) {
                    enemies.add(gameStage.sendWave(patterns.get(sendingPatternIndex).enemyType));
                    if (!patterns.get(sendingPatternIndex).isVertical) {
                        enemies.get(enemies.size - 1).positionY = patterns.get(sendingPatternIndex).rowIndex;
                    } else {
                        enemies.get(enemies.size - 1).positionY = patterns.get(sendingPatternIndex).rowIndices.get(patternIndex);
                    }
                    enemies.get(enemies.size - 1).setColorType(patterns.get(sendingPatternIndex).colorTypes.get(patterns.get(sendingPatternIndex).colorTypes.size > 1 ? patternIndex : 0));

                    int prob = MathUtils.random(2);
                    float offsetY = prob == 2 ? enemies.get(enemies.size - 1).getHeight() / 2 : prob == 1 ? enemies.get(enemies.size - 1).getHeight() / 4 : enemies.get(enemies.size - 1).getHeight() / 1.5f;
                    enemies.get(enemies.size - 1).setY(gameStage.background.getZero().y + enemies.get(enemies.size - 1).positionY * gameStage.grid.tileHeight + offsetY);

                    if (++patternIndex >= patterns.get(sendingPatternIndex).patternEnemiesCount) {
                        if (timers.contains(timer, false)) {
                            timer.setTimeStep(timeStepArray.get(timers.indexOf(timer, false)));
                        }
                        sentEnemiesCount = 0;
                        isPattern = false;
                        sendingPatternIndex = -1;
                    }
                } else {
                    enemies.add(gameStage.sendWave(WaveType.values()[sendIndex]));
                }
            } else {
                enemies.add(gameStage.sendWave(WaveType.values()[sendIndex]));
            }
            enemies.get(enemies.size - 1).checkToWait();
        }
    }

    private void addWaveType(final int index) {
        if(index == 0) {
            gameStage.game.timerManager.run(sendTimeArray.get(index), new TimerRunnable() {
                @Override
                public void run(Timer timer) {
                    gameStage.runOnGameThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < timers.size; i++) {
                                gameStage.game.timerManager.removeTimer(timers.get(i));
                            }
                            timers.clear();
                            final int enemiesSize = MathUtils.random(1, 3);
                            checkNextWaveTimer = gameStage.game.timerManager.loop(0.3f, new TimerRunnable() {
                                @Override
                                public void run(Timer timer) {
                                    if (enemies.size <= enemiesSize) {
                                        gameStage.runOnGameThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                gameStage.game.timerManager.removeTimer(checkNextWaveTimer);
//                                                Boss boss = gameStage.sendBoss(BossType.MOTHER);
//                                                boss.setSpawnEnemyType(WaveType.values()[timeStepArray.size - 1]);
                                                nextLevel();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            });
            return;
        }
        gameStage.game.timerManager.run(sendTimeArray.get(index), new TimerRunnable() {
            @Override
            public void run(Timer timer) {
                isPattern = false;
                sentEnemiesCount = 0;
                int sendIndex = index;
                if(globalWaveIndex % 2 == 0) {
                    enemyTypeIndex--;
                    sendIndex--;
                }
                globalWaveIndex++;
                addWaveType(sendIndex);
            }
        });
    }

    public void unFocus(Actor actor) {
        if(actor instanceof Enemy) {
            enemies.removeValue((Enemy)actor, false);
        }
    }

    public void nextLevel() {
        if(timeArray.size == 0) {
            gameStage.sendBoss(BossType.SHOOTING);
            init(++level);
        } else {
            if (sendTimeArray.size > 1) {
                sendTimeArray.set(sendTimeArray.size - 1, 2f);
                sendTimeArray.set(0, sendTimeArray.get(0) - 3.5f);
            }
            sendTimeArray.add(timeArray.removeIndex(0));
            timeStepArray.add(timeArray.removeIndex(0));

            enemyTypeIndex = timeStepArray.size - 1;
            sendWaves(enemyTypeIndex);
        }
    }

    public void reset() {
        sendTimeArray.clear();
        timeStepArray.clear();
        timers.clear();
        enemies.clear();
        timeArray.clear();
        gameStage.game.timerManager.reset();
        level = 0;
        enemyTypeIndex = 0;
        currentIndex = 0;
        patterns.clear();
        init(level);
    }
}
