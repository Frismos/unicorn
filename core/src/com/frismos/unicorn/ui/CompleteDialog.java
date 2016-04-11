package com.frismos.unicorn.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.frismos.unicorn.manager.SoundManager;
import com.frismos.unicorn.screen.GameScreen;
import com.frismos.unicorn.screen.SplashScreen;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.UIStage;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

import java.util.Iterator;

/**
 * Created by eavanyan on 4/2/16.
 */
public class CompleteDialog extends SpineActor {

    public ObjectMap<String, Actor> boundingBoxes = new ObjectMap<>();
    private UIStage stage;
    public SkeletonBounds skeletonBounds;
    public String touchedBounds = "";

    public CompleteDialog(final UIStage stage) {
        super(stage);
        this.stage = stage;
        stage.comboLabel.remove();
        setPosition(stage.getWidth() / 2 - getWidth() / 2, stage.getHeight() / 2 - getHeight() / 2);

        startDefaultAnimation();
//        stage.game.soundManager.playMusic("005575160-magic-turning-spells-casting (mp3cut.net)", Sound.class, true);
        stage.game.multiplexer.removeProcessor(((GameScreen)stage.game.getScreen()).stage);
        Gdx.input.setInputProcessor(stage.game.multiplexer);
    }

    public void calculateButtonSizes() {
        if (skeletonBounds.getBoundingBoxes().size > 0) {

            for (int index = 0; index < skeletonBounds.getBoundingBoxes().size; index++) {
                if (!skeletonBounds.getBoundingBoxes().get(index).getName().equals("bounds")) {
                    Bone bone = skeletonActor.getSkeleton().findSlot(skeletonBounds.getBoundingBoxes().get(index).getName()).getBone();
                    float rotation = bone.getRotation();
                    Polygon bounds = new Polygon(skeletonBounds.getBoundingBoxes().get(index).getVertices());
                    bounds.rotate(-rotation);

                    Actor img = null;
                    if(skeletonBounds.getBoundingBoxes().get(index).getName().equals("green-bounds")) {
                        img = new Actor();
                        img.addListener(new ClickListener() {
                            @Override
                            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                stage.game.soundManager.playMusic(SoundManager.BUTTON, Sound.class, true, false, 1.0f);
                                skeletonActor.getSkeleton().findSlot("green-normal").getColor().a = 0;
                                return super.touchDown(event, x, y, pointer, button);
                            }

                            @Override
                            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                CompleteDialog.this.skeletonActor.getAnimationState().setAnimation(0, "hide", false);
                                CompleteDialog.this.skeletonActor.getAnimationState().clearListeners();
                                CompleteDialog.this.skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
                                    @Override
                                    public void complete(int trackIndex, int loopCount) {
                                        CompleteDialog.this.remove(false);
                                        ((GameScreen) stage.game.getScreen()).stage.restartGame();
                                        CompleteDialog.this.skeletonActor.getAnimationState().removeListener(this);

                                        super.complete(trackIndex, loopCount);
                                    }
                                });
                                super.touchUp(event, x, y, pointer, button);
                            }
                        });
                    } else if(skeletonBounds.getBoundingBoxes().get(index).getName().equals("red-bounds")) {
                        img = new Actor();
                        img.addListener(new ClickListener() {
                            @Override
                            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                stage.game.soundManager.playMusic(SoundManager.BUTTON, Sound.class, true, false, 1.0f);
                                skeletonActor.getSkeleton().findSlot("red-normal").getColor().a = 0;
                                return super.touchDown(event, x, y, pointer, button);
                            }

                            @Override
                            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                CompleteDialog.this.skeletonActor.getAnimationState().setAnimation(0, "hide", false);
                                CompleteDialog.this.skeletonActor.getAnimationState().clearListeners();
                                CompleteDialog.this.skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
                                    @Override
                                    public void complete(int trackIndex, int loopCount) {
                                        CompleteDialog.this.remove(false);
                                        stage.game.getScreen().dispose();;
                                        stage.game.setScreen(new SplashScreen(stage.game));
                                        CompleteDialog.this.skeletonActor.getAnimationState().removeListener(this);

                                        super.complete(trackIndex, loopCount);
                                    }
                                });
                                super.touchUp(event, x, y, pointer, button);
                            }
                        });
                    } else if(skeletonBounds.getBoundingBoxes().get(index).getName().equals("content-bounds")) {
                        img = stage.scoreLabel;
                        img.remove();
                        ((Label)img).setAlignment(Align.center);
                    } else if(skeletonBounds.getBoundingBoxes().get(index).getName().equals("defended-bounds")) {
                        img = new Group();

                        stage.timeLabel.remove();
                        Rectangle boundingRectangle = bounds.getBoundingRectangle();
                        stage.timeLabel.setSize(boundingRectangle.width, boundingRectangle.height);
                        int seconds = (int)((GameScreen)stage.game.getScreen()).stage.timeMillis;
                        int minutes = seconds / 60;
                        seconds %= 60;
                        stage.timeLabel.setText(String.format("%sm %ss", minutes, seconds >= 10 ? seconds : String.format("0%s", seconds)));
                        stage.timeLabel.setPosition(0, 5);
                        stage.timeLabel.setAlignment(Align.top);
                        ((Group)img).addActor(stage.timeLabel);
                        img.rotateBy(-7);
                    }
                    if(img != null) {
                        Rectangle boundingRectangle = bounds.getBoundingRectangle();
                        img.setSize(boundingRectangle.width, boundingRectangle.height);
                        img.setPosition(boundingRectangle.x + getX(), boundingRectangle.y + getY());
                        stage.addActor(img);
                        img.toFront();

                        boundingBoxes.put(skeletonBounds.getBoundingBoxes().get(index).getName(), img);
                    }
                }
            }
        }
    }

    public void calculatePositions() {
        if (skeletonBounds != null && skeletonBounds.getBoundingBoxes().size > 0) {
            for (int index = 0; index < skeletonBounds.getBoundingBoxes().size; index++) {
                if (!skeletonBounds.getBoundingBoxes().get(index).getName().equals("bounds")) {
                    Bone bone = skeletonActor.getSkeleton().findSlot(skeletonBounds.getBoundingBoxes().get(index).getName()).getBone();
                    Polygon bounds = new Polygon(skeletonBounds.getBoundingBoxes().get(index).getVertices());
                    Actor img = boundingBoxes.get(skeletonBounds.getBoundingBoxes().get(index).getName());
                    img.setPosition(bounds.getBoundingRectangle().x + getX(), bounds.getBoundingRectangle().y + getY());
                    img.toBack();
                }
            }
        }
    }

    @Override
    public void act(float delta) {
//        calculatePositions();
        super.act(delta);
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.COMPLETE_DIALOG;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.5f;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "show", false);
//        calculateButtonSizes();
        skeletonActor.getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(int trackIndex, int loopCount) {
                CompleteDialog.this.skeletonActor.getAnimationState().removeListener(this);
                skeletonBounds = new SkeletonBounds();
                skeletonBounds.update(skeletonActor.getSkeleton(), true);
                calculateButtonSizes();
//                skeletonBounds.aabbContainsPoint()

                super.complete(trackIndex, loopCount);
            }
        });
    }

    @Override
    public boolean remove(boolean dispose) {
        ObjectMap.Keys<String> keys = boundingBoxes.keys();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            boundingBoxes.get(key).remove();
        }
        return super.remove(dispose);
    }
}
