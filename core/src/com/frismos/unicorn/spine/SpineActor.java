package com.frismos.unicorn.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonBounds;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/19/15.
 */
public abstract class SpineActor extends BaseSpineActor {

    public SpineActor(GameStage stage) {
        super(stage);

        SkeletonBounds skeletonBounds = new SkeletonBounds();
        skeletonBounds.update(skeleton, true);
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

        AnimationStateData animationStateData = new AnimationStateData(skeleton.getData());
        animationState = new AnimationState(animationStateData);
//        animationState.setTimeScale(MathUtils.random(.4f,.8f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.RED);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        animationState.apply(skeleton);

        super.act(delta);
        animationState.update(delta);
    }

    public interface AnimationEventListener {
        void fire();
    }
}
