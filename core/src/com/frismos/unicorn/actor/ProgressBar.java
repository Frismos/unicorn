package com.frismos.unicorn.actor;

import com.esotericsoftware.spine.Bone;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;

/**
 * Created by edgar on 12/9/2015.
 */
public class ProgressBar extends SpineActor {

    private float progress;
    private float maxProgress;
    private float size;

    private Bone posBg;
    private Bone posFill;

    public ProgressBar(GameStage stage, float size, float maxProgress) {
        super(stage);
        this.maxProgress = maxProgress;
        this.size = size;
        posBg = skeletonActor.getSkeleton().findBone("pos-bg");
        posFill = skeletonActor.getSkeleton().findBone("pos-fill");
        posBg.setX(size);
        posFill.setX(size);
        setWidth(size);
    }

    @Override
    protected void startDefaultAnimation() {

    }

    @Override
    public void actorAddedToStage() {
        posBg.setX(size);
        posFill.setX(size);
        setWidth(size);
        setProgress(this.progress);
        super.actorAddedToStage();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        posFill.setX(progress / maxProgress * getWidth());
    }

    public float getProgress() {
        return progress;
    }

    @Override
    protected void setResourcesPath() {
        path = "progress";
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.PROGRESS_BAR_SCALE_RATIO;
    }
}
