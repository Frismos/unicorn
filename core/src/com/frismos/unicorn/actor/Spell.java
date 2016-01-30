package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class Spell extends GameActor {

    public SpellType spellType;

    public Spell(GameStage stage) {
        super(stage, ColorType.getRandomColor());
        spellType = SpellType.getRandomValue(gameStage.unicorn.hitPoints == 1);
        setUserObject(ActorDataType.SPELL);
    }

    @Override
    protected void startDefaultAnimation() {

    }

    @Override
    protected void setResourcesPath() {
        path = Strings.MOTHER_BOSS;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.SPELL_SCALE_RATIO;
    }

    @Override
    public void setScale(float scaleXY) {
//        scaleRatio = scaleXY * 0.1f;
        skeletonActor.getSkeleton().findBone("root").setScale(scaleXY);
        super.setScale(scaleXY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
//        scaleRatio = scaleX * 0.1f;
        skeletonActor.getSkeleton().findBone("root").setScale(scaleX);
        super.setScale(scaleX, scaleY);
    }

    @Override
    public void setScaleX(float scaleX) {
//        scaleRatio = scaleX * 0.1f;
        skeletonActor.getSkeleton().findBone("root").setScale(scaleX);
        super.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
//        scaleRatio = scaleY * 0.1f;
        skeletonActor.getSkeleton().findBone("root").setScale(scaleY);
        super.setScaleY(scaleY);
    }
}
