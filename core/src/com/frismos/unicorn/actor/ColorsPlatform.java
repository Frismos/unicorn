package com.frismos.unicorn.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Slot;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;
import com.frismos.unicorn.util.Utils;

/**
 * Created by eavanyan on 2/8/16.
 */
public class ColorsPlatform extends SpineActor {

    public Array<Vector2> positions;
    private ObjectMap<ColorType, Array<Slot>> tubes = new ObjectMap<>();
    private Array<Slot> currentTubes = new Array<>();

    public ColorsPlatform(GameStage stage) {
        super(stage);
        skeletonActor.getSkeleton().setSkin("1");
        skeletonActor.getSkeleton().getRootBone().setScale(gameStage.background.getHeight() / getHeight());
        setSize(getWidth() * skeletonActor.getSkeleton().getRootBone().getScaleX(), getHeight() * skeletonActor.getSkeleton().getRootBone().getScaleY());
        positions = new Array<>();
        setX(1.5f * Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth() * Math.abs(stage.getViewport().getLeftGutterWidth()));
        setY(stage.grid.grid[0][0].getY());
        skeletonActor.getSkeleton().updateWorldTransform();
        for (int i = 0; i < 3; i++) {
            Bone positionBone = skeletonActor.getSkeleton().findBone("pose" + i);
            positions.add(new Vector2(positionBone.getWorldX() + getX(), positionBone.getWorldY() + getY()));

            Array<Slot> tubeArray = new Array<>();
            tubeArray.add(skeletonActor.getSkeleton().findSlot(String.format("shine%d", i)));
//            tubeArray.add(skeletonActor.getSkeleton().findSlot(String.format("shine%dbot", i)));
            tubes.put(ColorType.values()[i], tubeArray);
            Utils.colorSlot(tubeArray.get(0), ColorType.values()[i]);
//            Utils.colorSlot(tubeArray.get(1), ColorType.values()[i]);
            tubes.get(ColorType.values()[i]).get(0).getColor().a = 0;
//            tubes.get(ColorType.values()[i]).get(1).getColor().a = 0;
        }

        Utils.colorPlatform(this);
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.COLORS_PLATFORM;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.COLORS_PLATFORM_SCALE_RATIO;
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "idle", true);
    }

    public void setColorType(ColorType colorType) {
        if(currentTubes.size > 0) {
            currentTubes.get(0).getColor().a = 0;
//            currentTubes.get(1).getColor().a = 0;
        }
        currentTubes = tubes.get(colorType);
        currentTubes.get(0).getColor().a = 1;
//        currentTubes.get(1).getColor().a = 1;
    }
}
