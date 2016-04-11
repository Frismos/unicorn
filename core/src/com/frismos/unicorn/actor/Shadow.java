package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by eavanyan on 3/3/16.
 */
public class Shadow extends Image {
    public Shadow(GameStage gameStage, Enemy enemy) {
        super(gameStage.game.atlasManager.get("gfx/@atlas/pack.atlas", TextureAtlas.class).findRegion("shadow"));
        setSize(enemy.getWidth() * 1.6f, enemy.getWidth() / 2);
        setColor(enemy.getColor().r, enemy.getColor().g, enemy.getColor().b, .7f);
        gameStage.addActor(this);
        setZIndex(gameStage.background.getZIndex() + 1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        super.draw(batch, parentAlpha);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }
}
