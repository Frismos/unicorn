package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by edgar on 11/30/2015.
 */
public class FontsManager {
    private FreeTypeFontGenerator freeTypeFontGenerator;

    private BitmapFont font;

    public FontsManager() {
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("gfx/Open_Sans/OpenSans-Regular.ttf"));
    }

    public BitmapFont getFont(Color color, int size) {
        if(font == null) {
            FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            freeTypeFontParameter.color = color;
            freeTypeFontParameter.size = size;
            font = freeTypeFontGenerator.generateFont(freeTypeFontParameter);
        }
        font.setColor(color);
        return font;
    }


    public BitmapFont getFont(int size) {
        return getFont(Color.WHITE, size);
    }

    public void dispose() {
        freeTypeFontGenerator.dispose();
    }
}
