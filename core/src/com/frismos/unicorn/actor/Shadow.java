package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Debug;

/**
 * Created by eavanyan on 3/3/16.
 */
public class Shadow extends Image {
    public Shadow(GameStage gameStage, Enemy enemy) {
        super(gameStage.game.atlasManager.get("gfx/@atlas/pack.atlas", TextureAtlas.class).findRegion("shadow"));
        setSize(enemy.getWidth(), enemy.getWidth() / 2);
        setColor(0, 0, 0, 0.25f);
        gameStage.addActor(this);
        setZIndex(gameStage.background.getZIndex() + 1);
        setPosition(enemy.getX(), enemy.getY());
    }
}
