package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by eavanyan on 3/3/16.
 */
public class Shadow extends Image {
    public Shadow(GameStage gameStage, Enemy enemy) {
        super(gameStage.game.atlasManager.get("gfx/@atlas/pack.atlas", TextureAtlas.class).findRegion("shadow"));
        setSize(enemy.getWidth()*1.6f, enemy.getWidth() / 2);
        setColor(enemy.getColor().r, enemy.getColor().g, enemy.getColor().b, .7f);
        gameStage.addActor(this);
        setZIndex(gameStage.background.getZIndex() + 1);
    }
}
