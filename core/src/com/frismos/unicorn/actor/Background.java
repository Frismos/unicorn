package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgaravanyan on 10/19/15.
 */

public abstract class Background extends SpineActor {

    private Vector2 zero;

    private TopLayer topLayer;

    public Background(GameStage stage) {
        super(stage);
        String filePath = String.format("gfx/%s/top.atlas", path);
        if(Gdx.files.internal(filePath).exists()) {
            topLayer = new TopLayer(gameStage, path, scaleRatio);
            topLayer.skeletonActor.getAnimationState().setAnimation(0, "animation", true);
            gameStage.addActor(topLayer);
        }
    }

    @Override
    protected void startDefaultAnimation() {
        Debug.log("default animation");
        skeletonActor.getAnimationState().setAnimation(0, "animation", true);
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BACKGROUND_SCALE_RATIO;
    }

    public Vector2 getZero() {
        if(zero == null) {
            zero = new Vector2(skeletonActor.getSkeleton().findBone("zero").getX(), skeletonActor.getSkeleton().findBone("zero").getY());
        }
        return zero;
    }

    @Override
    public boolean remove() {
        if(topLayer != null) {
            topLayer.remove();
        }
        return super.remove();
    }

    @Override
    public boolean remove(boolean dispose) {
        if(topLayer != null) {
            topLayer.remove();
        }
        return super.remove(dispose);
    }
}
