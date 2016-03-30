package com.frismos.unicorn.spine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.frismos.unicorn.actor.Background;
import com.frismos.unicorn.actor.MainCharacter;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 12/11/2015.
 */
public abstract class SpineActor extends Actor {

    public GameStage gameStage;

    protected String path = "";
    protected float scaleRatio;

    public SkeletonActor skeletonActor;

    public Polygon bounds;

    public SpineActor(GameStage stage) {
        gameStage = stage;

        setResourcesPath();
        setScaleRatio();
//        Class type = getType();
        String filePath = String.format("gfx/%s.atlas", path.contains("@") ? "@atlas/pack" : path.contains("bg") ? String.format("%s/bot", path) : String.format("%s/skeleton", path));
        SkeletonJson skeletonJson = gameStage.game.atlasManager.getSkeletonJson(this.getClass(), filePath);
        skeletonJson.setScale(scaleRatio);
        filePath = path.contains("bg") ? String.format("gfx/%s/bot.json", path) : String.format("gfx/%s/skeleton.json", path);
        SkeletonData skeletonData = gameStage.game.atlasManager.getSkeletonData(this.getClass(), filePath, skeletonJson);
        AnimationStateData animationStateData = gameStage.game.atlasManager.getAnimationStateData(this.getClass(), skeletonData);

        skeletonActor = gameStage.game.atlasManager.getSkeletonActor(skeletonData, animationStateData);
        calculateSize();
//        animationState.setTimeScale(MathUtils.random(.4f,.8f));
    }

    public void calculateSize() {
        SkeletonBounds skeletonBounds = new SkeletonBounds();
        skeletonBounds.update(skeletonActor.getSkeleton(), true);
        if(skeletonBounds.getBoundingBoxes().size > 0) {

            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            for (int i = 0; i < skeletonBounds.getBoundingBoxes().get(0).getVertices().length; i += 2) {
                if (maxX < skeletonBounds.getBoundingBoxes().get(0).getVertices()[i]) {
                    maxX = skeletonBounds.getBoundingBoxes().get(0).getVertices()[i];
                }
                if (maxY < skeletonBounds.getBoundingBoxes().get(0).getVertices()[i + 1]) {
                    maxY = skeletonBounds.getBoundingBoxes().get(0).getVertices()[i + 1];
                }
                if (minX > skeletonBounds.getBoundingBoxes().get(0).getVertices()[i]) {
                    minX = skeletonBounds.getBoundingBoxes().get(0).getVertices()[i];
                }
                if (minY > skeletonBounds.getBoundingBoxes().get(0).getVertices()[i + 1]) {
                    minY = skeletonBounds.getBoundingBoxes().get(0).getVertices()[i + 1];
                }

            }
            setSize(maxX - minX, maxY - minY);

            this.bounds = new Polygon(skeletonBounds.getBoundingBoxes().get(0).getVertices());
        } else {
            this.bounds = new Polygon(new float[] {
                    getX(), getY(),
                    getX(), getY() + getHeight(),
                    getX() + getWidth(), getY() + getHeight(),
                    getX() + getWidth(), getY()
            });
        }

    }

//    protected abstract Class getType();
    protected abstract void setResourcesPath();
    protected abstract void setScaleRatio();
    protected abstract void startDefaultAnimation();


    @Override
    public void draw(Batch batch, float parentAlpha) {
        skeletonActor.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        skeletonActor.act(delta);
        super.act(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        skeletonActor.setPosition(getX(), getY());
    }

    public float getScaleRatio() {
        return scaleRatio;
    }

    public boolean remove(boolean dispose) {
        if (dispose) {
            dispose();
        } else {
            resetPosition();
        }
        return super.remove();
    }

    public void dispose() {
        gameStage.game.atlasManager.freeSkeletonActor(skeletonActor);
        skeletonActor.act(Gdx.graphics.getDeltaTime());
    }

    protected void resetPosition() {

    }

    public void actorAddedToStage() {
        startDefaultAnimation();
    }
}
