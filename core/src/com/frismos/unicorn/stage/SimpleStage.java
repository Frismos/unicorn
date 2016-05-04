package com.frismos.unicorn.stage;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frismos.unicorn.UnicornGame;

/**
 * Created by eavanyan on 4/2/16.
 */
public class SimpleStage extends Stage {
    public UnicornGame game;

    public SimpleStage(Viewport fillViewport, PolygonSpriteBatch polygonSpriteBatch) {
        super(fillViewport, polygonSpriteBatch);
    }
}
