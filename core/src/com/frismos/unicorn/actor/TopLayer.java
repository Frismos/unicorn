package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by eavanyan on 3/28/16.
 */
public class TopLayer extends Actor {

    public SkeletonActor skeletonActor;

    public TopLayer(GameStage gameStage, String filePath, float scale) {
        SkeletonJson skeletonJson = gameStage.game.atlasManager.getSkeletonJson(this.getClass(), String.format("gfx/%s/top.atlas", filePath));
        skeletonJson.setScale(scale);
        SkeletonData skeletonData = gameStage.game.atlasManager.getSkeletonData(this.getClass(), String.format("gfx/%s/top.json", filePath), skeletonJson);

        AnimationStateData animationStateData = gameStage.game.atlasManager.getAnimationStateData(this.getClass(), skeletonData);
        skeletonActor = gameStage.game.atlasManager.getSkeletonActor(skeletonData, animationStateData);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        skeletonActor.setPosition(getX(), getY());
    }

    @Override
    public void act(float delta) {
        skeletonActor.act(delta);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        skeletonActor.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }
}
