package com.frismos.unicorn.actor;

import com.esotericsoftware.spine.Bone;
import com.frismos.unicorn.spine.BaseSpineActor;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgar on 12/9/2015.
 */
public class ProgressBar extends BaseSpineActor {

    private float progress;
    private float maxProgress;

    private Bone posBg;
    private Bone posFill;

    public ProgressBar(GameStage stage, float size, float maxProgress) {
        super(stage);
        this.maxProgress = maxProgress;
        posBg = skeleton.findBone("pos-bg");
        posBg.setX(size);
        posFill = skeleton.findBone("pos-fill");
        posFill.setX(size);
        setWidth(size);
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
        scaleRatio = 0.2f;
    }
}
