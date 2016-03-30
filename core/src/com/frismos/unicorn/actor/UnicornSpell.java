package com.frismos.unicorn.actor;

import com.frismos.unicorn.enums.SpellType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Strings;

/**
 * Created by eavanyan on 2/28/16.
 */
public class UnicornSpell extends Spell {
    public UnicornSpell(GameStage stage) {
        super(stage);
        spellType = SpellType.CALL_UNICORNS;
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.UNICORN_SPELL;
    }
}
