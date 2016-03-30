package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 2/28/16.
 */
public class HealthSpell extends Spell {
    public HealthSpell(GameStage stage) {
        super(stage);
        spellType = SpellType.HEALTH;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.HEALTH_SPELL;
    }
}
