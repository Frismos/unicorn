package com.frismos.unicorn.spine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;

/**
 * Created by edgar on 12/11/2015.
 */
public abstract class SpineActor extends Actor {

    public GameStage gameStage;

    protected String path;
    protected float scaleRatio;

    public SkeletonActor skeletonActor;

    public SpineActor(GameStage stage) {
        gameStage = stage;

        setResourcesPath();
        setScaleRatio();
//        Class type = getType();
        String filePath = gameStage.game.strings.addString("gfx/").addString(path).addString("/skeleton.atlas").toString();
//        textureAtlas = gameStage.game.atlasManager.get(filePath, TextureAtlas.class);
        SkeletonJson skeletonJson = gameStage.game.atlasManager.getSkeletonJson(this.getClass(), filePath);
        skeletonJson.setScale(scaleRatio / Constants.SCALE_RATIO);
        filePath = filePath.replaceFirst("atlas", "json");
        SkeletonData skeletonData = gameStage.game.atlasManager.getSkeletonData(this.getClass(), filePath, skeletonJson);
//        skeleton = new Skeleton(skeletonData);
//        skeleton.setToSetupPose();

        AnimationStateData animationStateData = new AnimationStateData(skeletonData);

        skeletonActor = gameStage.game.atlasManager.getSkeletonActor(skeletonData, animationStateData);

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
        }
//        animationState.setTimeScale(MathUtils.random(.4f,.8f));
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
