package com.frismos.unicorn.manager;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Bone;
import com.frismos.TweenAccessor.ActorAccessor;
import com.frismos.unicorn.UnicornGame;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.actor.MotherEnemy;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.Direction;
import com.frismos.unicorn.enums.TutorialAction;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.ui.TutorialLabel;


/**
 * Created by edgaravanyan on 1/18/16.
 */
public class TutorialManager implements Updatable {

    private TutorialLabel tutorialLabel;
    public boolean isTutorialMode;
    public TutorialAction currentStep;

    private UnicornGame game;
    private Image arrowImage;

    private Label gotItLabel;

    public ObjectMap<TutorialAction, Boolean> stepsPassed = new ObjectMap<>();
    public EventListener listener;
    public boolean fireListener = false;
    private Object bundle;

    public TutorialManager(UnicornGame game) {
        this.game = game;
        game.updatableArray.add(this);
        isTutorialMode = false;//!game.dataManager.isTutorialPassed();
        if(isTutorialMode) {
            currentStep = TutorialAction.NONE;
            stepsPassed.put(TutorialAction.GAME_STARTED, false);
            stepsPassed.put(TutorialAction.FIRST_MONSTER_APPEAR, false);
            stepsPassed.put(TutorialAction.PICK_COLOR, false);
            stepsPassed.put(TutorialAction.SHOOT_MONSTER, false);
            stepsPassed.put(TutorialAction.COMBO_HINT, false);
            stepsPassed.put(TutorialAction.WRONG_COLOR, true);

            tutorialLabel = new TutorialLabel(game);
            tutorialLabel.setPosition(0, game.uiScreen.stage.getHeight() / 4);

            arrowImage = new Image(new Texture(Gdx.files.internal("gfx/Arrow_down.png")));
            arrowImage.setScale(0.01f);

            fireListener = false;
            listener = null;
        }
    }

    public void showSlideArrow(GameStage gameStage, Direction direction) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(5);
            arrowImage.setY(10);
            float scaleY = direction == Direction.DOWN ? 0.01f : -0.01f;
            arrowImage.setScale(0.01f, scaleY);
            gameStage.addActor(arrowImage);
        }
    }

    private void showArrow(float x, float y) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(x + - arrowImage.getWidth() * arrowImage.getScaleX() / 2);
            arrowImage.setY(y);
            ((GameScreen)game.getScreen()).stage.addActor(arrowImage);
            arrowImage.toFront();
            Tween.to(arrowImage, ActorAccessor.POSITION_XY, 0.2f).targetRelative(0, arrowImage.getHeight() * arrowImage.getScaleY() / 2).repeatYoyo(Tween.INFINITY, 0.01f).ease(Sine.INOUT).start(game.tweenManager);
        }
    }

    public void showArrowOnActor(final Actor actor) {
        if(arrowImage.getParent() == null) {
            arrowImage.setX(actor.getX() + (actor.getWidth() * actor.getScaleX() - arrowImage.getWidth() * arrowImage.getScaleX()) / 2);
            arrowImage.setY(actor.getY() + actor.getHeight() + 2);
            actor.getParent().addActor(arrowImage);
            arrowImage.toFront();
            Tween.to(arrowImage, ActorAccessor.POSITION_XY, 0.2f).targetRelative(0, -actor.getHeight() / 2).repeatYoyo(Tween.INFINITY, 0.01f).ease(Sine.INOUT).start(game.tweenManager);
        }
    }

    public boolean removeArrow() {
        arrowImage.setScale(0.01f);
        game.tweenManager.killTarget(arrowImage);
        return arrowImage.remove();
    }

    public void showGotItLabel() {
        if(gotItLabel == null) {
            Label.LabelStyle style = new Label.LabelStyle(game.uiScreen.stage.font, Color.WHITE);
            gotItLabel = new Label("Got It!", style);
        }
        gotItLabel.setPosition(1500, 100);
        gotItLabel.setSize(200, 100);
        game.uiScreen.stage.addActor(gotItLabel);
    }

    public void fireAction(TutorialAction action,  Object object) {
        currentStep = action;
        fireListener = false;
        bundle = object;
    }

    public void sendFirstEnemy() {
        ColorType colorType = ColorType.getRandomColor();
        GameStage stage = ((GameScreen)game.getScreen()).stage;
        while (colorType == stage.unicorn.colorType) {
            colorType = ColorType.getRandomColor();
        }
        Enemy enemy = new MotherEnemy(stage, colorType, true);
        stage.collisionDetector.addListener(enemy);
        stage.addActor(enemy);
    }

    @Override
    public void update(float delta) {
        if(isTutorialMode) {
            if (currentStep != TutorialAction.NONE && !stepsPassed.get(currentStep)) {
                if (currentStep == TutorialAction.GAME_STARTED) {
                    executeGameStartAction();
                }
                if (currentStep == TutorialAction.FIRST_MONSTER_APPEAR) {
                    executeFirstMonsterAction();
                }
                if (currentStep == TutorialAction.PICK_COLOR) {
                    executePickColorAction();
                }
                if (currentStep == TutorialAction.SHOOT_MONSTER) {
                    executeShootMonsterAction();
                }
                if (currentStep == TutorialAction.COMBO_HINT) {
                    executeComboHitAction();
                }
                if (currentStep == TutorialAction.WRONG_COLOR) {
                    executeWrongColorAction();
                }
            }

            if (listener != null && fireListener) {
                listener.eventFire();
                listener = null;
            }
        }
    }

    private void executeWrongColorAction() {
        stepsPassed.put(TutorialAction.WRONG_COLOR, true);

        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();
        ((GameScreen)game.getScreen()).stage.attackCommand.cancelTask();

        game.multiplexer.removeProcessor(((GameScreen)game.getScreen()).stage);

        showTutorialText("Your bullet power is going down when you hit with wrong color.");
        showGotItLabel();

        final InputListener inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.tutorialManager.fireListener = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        };
        gotItLabel.addListener(inputListener);
        listener = new EventListener() {
            @Override
            public void eventFire() {
                tutorialLabel.remove();
                gotItLabel.remove();
                game.uiScreen.stage.removeListener(inputListener);
                game.aiManager.resumeGame();
                game.multiplexer.addProcessor(((GameScreen)game.getScreen()).stage);
            }
        };
    }

    private void executeComboHitAction() {
        stepsPassed.put(TutorialAction.COMBO_HINT, true);

        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();
        ((GameScreen)game.getScreen()).stage.attackCommand.cancelTask();

        final Actor enemy = (Actor)bundle;
        showArrowOnActor(enemy);

        showTutorialText("Each accurate hit gives your bullet power.");

        final GameStage stage = ((GameScreen)game.getScreen()).stage;
        stage.unicorn.addPositionChangeListener(new EventListener() {
            @Override
            public void eventFire() {
                if(stage.unicorn.colorType != ((Enemy)enemy).colorType) {
                    stage.unicorn.removePositionChangeListener();
                    ((Enemy) enemy).removeHitListener();
                    ((Enemy) enemy).removeDieListener();
                    removeArrow();
                    tutorialLabel.remove();
                    game.aiManager.resumeGame();
                    stepsPassed.put(TutorialAction.PICK_COLOR, false);
                    fireAction(TutorialAction.PICK_COLOR, bundle);
                }
            }
        });

        ((Enemy)enemy).setDieListener(new Enemy.EventListener() {

            @Override
            public void fireEvent() {
                fireListener = true;
            }
        });

        listener = new EventListener() {
            @Override
            public void eventFire() {
                stage.unicorn.removePositionChangeListener();
                ((Enemy)enemy).removeHitListener();
                ((Enemy)enemy).removeDieListener();
                removeArrow();
                tutorialLabel.remove();
                game.aiManager.resumeGame();
                stepsPassed.put(TutorialAction.WRONG_COLOR, false);
            }
        };
    }

    private void executeShootMonsterAction() {
        stepsPassed.put(TutorialAction.SHOOT_MONSTER, true);

        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();

        showTutorialText("SHOOT at the MONSTER!!!!");
        final Actor enemy = (Actor)bundle;

        final GameStage stage = ((GameScreen)game.getScreen()).stage;
        stage.unicorn.addPositionChangeListener(new EventListener() {
            @Override
            public void eventFire() {
                if(stage.unicorn.colorType != ((Enemy)enemy).colorType) {
                    stage.unicorn.removePositionChangeListener();
                    ((Enemy) enemy).removeHitListener();
                    removeArrow();
                    stepsPassed.put(TutorialAction.PICK_COLOR, false);
                    fireAction(TutorialAction.PICK_COLOR, bundle);
                }
            }
        });

        showArrowOnActor(enemy);

        final Enemy.EventListener eventListener = new Enemy.EventListener() {
            @Override
            public void fireEvent() {
                game.tutorialManager.fireListener = true;
            }
        };
        ((Enemy)enemy).setHitListener(eventListener);

        listener = new EventListener() {
            @Override
            public void eventFire() {
                stage.unicorn.removePositionChangeListener();
                ((Enemy)enemy).removeHitListener();
                tutorialLabel.remove();
                if(!((Enemy)enemy).isDead) {
                    stepsPassed.put(TutorialAction.COMBO_HINT, false);
                    fireAction(TutorialAction.COMBO_HINT, bundle);
                } else {
                    stepsPassed.put(TutorialAction.WRONG_COLOR, false);
                }
            }
        };
    }

    private void executePickColorAction() {
        stepsPassed.put(TutorialAction.PICK_COLOR, true);

        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();

        showTutorialText("Pick the right color and...");
        final GameStage stage = ((GameScreen)game.getScreen()).stage;
        stage.attackCommand.pause();
        final Actor enemy = (Actor)bundle;
        Bone tube = stage.colorsPlatform.getTube(((Enemy)enemy).colorType);
        showArrow(tube.getWorldX() + stage.colorsPlatform.getX(), tube.getWorldY() + stage.colorsPlatform.getY());

        final InputListener inputListener =  new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (x <= stage.colorsPlatform.getX() + stage.colorsPlatform.getWidth()) {
                    float fingerY = y;
                    if (((Enemy) enemy).colorType.ordinal() == 0) {
                        if (fingerY < stage.background.getZero().y + stage.grid.tileHeight) {
                            game.tutorialManager.fireListener = true;
                        }
                    } else if (((Enemy) enemy).colorType.ordinal() == 1) {
                        if(fingerY < stage.background.getZero().y + 2 * stage.grid.tileHeight && fingerY > stage.background.getZero().y + stage.grid.tileHeight) {
                            game.tutorialManager.fireListener = true;
                        }
                    } else {
                        if(fingerY > stage.background.getZero().y + 2 * stage.grid.tileHeight) {
                            game.tutorialManager.fireListener = true;
                        }
                    }
                }

                return super.touchDown(event, x, y, pointer, button);
            }
        };

        stage.addListener(inputListener);

        listener = new EventListener() {
            @Override
            public void eventFire() {
                stage.removeListener(inputListener);
                stage.attackCommand.resume();
                stepsPassed.put(TutorialAction.SHOOT_MONSTER, false);
                fireAction(TutorialAction.SHOOT_MONSTER, enemy);
                removeArrow();
            }
        };
    }

    private void executeFirstMonsterAction() {
        stepsPassed.put(TutorialAction.FIRST_MONSTER_APPEAR, true);

        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();

        showTutorialText("Don't let the monsters reach the rainbow.");
        final GameStage stage =((GameScreen)game.getScreen()).stage;
        stage.attackCommand.pause();
        showGotItLabel();

        game.multiplexer.removeProcessor(stage);

        final Actor enemy = (Actor)bundle;
        ((Enemy)enemy).pause();
        final InputListener inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.tutorialManager.listener != null) {
                    game.tutorialManager.fireListener = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        };
        game.uiScreen.stage.addListener(inputListener);

        listener = new EventListener() {
            @Override
            public void eventFire() {
                game.multiplexer.addProcessor(stage);
                gotItLabel.remove();
                game.uiScreen.stage.removeListener(inputListener);
                fireAction(TutorialAction.PICK_COLOR, enemy);
            }
        };
    }

    private void executeGameStartAction() {
        stepsPassed.put(TutorialAction.GAME_STARTED, true);
        fireListener = false;
        currentStep = TutorialAction.NONE;
        game.aiManager.pauseGame();

        showTutorialText("Your goal is to defend the rainbow.");

        ((GameScreen)game.getScreen()).stage.attackCommand.pause();
        showGotItLabel();

        final InputListener inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.tutorialManager.listener != null) {
                    game.tutorialManager.fireListener = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        };
        game.uiScreen.stage.addListener(inputListener);

        listener = new EventListener() {
            @Override
            public void eventFire() {
                game.uiScreen.stage.removeListener(inputListener);
                tutorialLabel.remove();
                game.aiManager.resumeGame();
                gotItLabel.remove();
                sendFirstEnemy();
            }
        };
    }

    private void showTutorialText(String text) {
        game.uiScreen.stage.addActor(tutorialLabel);
        tutorialLabel.setText(text);
    }

    public interface EventListener {
        public void eventFire();
    }
}
