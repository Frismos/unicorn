package com.frismos.unicorn.spine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 12/11/2015.
 */
public abstract class BaseSpineActor extends Actor {

    public GameStage gameStage;

    private SkeletonRenderer skeletonRenderer;

    public Skeleton skeleton;
    protected AnimationState animationState;
    protected String path;
    protected float scaleRatio;

    public BaseSpineActor(GameStage stage) {
        gameStage = stage;
        skeletonRenderer = new SkeletonRenderer();

        setResourcesPath();
        setScaleRatio();
//        Class type = getType();
        String filePath = gameStage.game.strings.addString("gfx/").addString(path).addString("/skeleton.atlas").toString();
//        textureAtlas = gameStage.game.atlasManager.get(filePath, TextureAtlas.class);
        SkeletonJson skeletonJson = gameStage.game.atlasManager.getSkeletonJson(this.getClass(), filePath);
        skeletonJson.setScale(scaleRatio / Constants.SCALE_RATIO);
        filePath = filePath.replaceFirst("atlas", "json");
        SkeletonData skeletonData = gameStage.game.atlasManager.getSkeletonData(this.getClass(), filePath, skeletonJson);
        skeleton = new Skeleton(skeletonData);
        skeleton.setToSetupPose();
    }

//    protected abstract Class getType();
    protected abstract void setResourcesPath();
    protected abstract void setScaleRatio();

    @Override
    public void act(float delta) {
        skeleton.setX(getX());
        skeleton.setY(getY());
        skeleton.updateWorldTransform();

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        skeletonRenderer.draw((PolygonSpriteBatch)batch, skeleton);
        super.draw(batch, parentAlpha);
    }

//    class DataByPath
}
