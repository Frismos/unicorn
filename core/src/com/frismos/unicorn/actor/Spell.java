package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/14/2015.
 */
public class Spell extends GameActor {

    public SpellType spellType;

    public Spell(GameStage stage, UserData userData) {
        super(stage, userData, ColorType.getRandomColor());
        setSize(10 / 4.0f, 15 / 4.0f);
        spellType = SpellType.getRandomValue(gameStage.unicorn.hitPoints == 1);
    }

    @Override
    public UserData getUserData() {
        return this.userData;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.BOSS;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 0.1f;
    }
}
