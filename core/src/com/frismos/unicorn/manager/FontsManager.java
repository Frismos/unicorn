package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by edgar on 11/30/2015.
 */
public class FontsManager {
    private FreeTypeFontGenerator freeTypeFontGenerator;

    private BitmapFont font;

    public FontsManager() {
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("gfx/londrina.otf"));
    }

    public BitmapFont getFont(Color color, int size) {
        if(font == null) {
            FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            freeTypeFontParameter.color = color;
            freeTypeFontParameter.size = size;
            freeTypeFontParameter.borderColor = Color.BLACK;
            freeTypeFontParameter.borderWidth = 3;
            freeTypeFontParameter.shadowColor = new Color(0, 0, 0, 0.25f);
            freeTypeFontParameter.minFilter = Texture.TextureFilter.Linear;
            freeTypeFontParameter.magFilter = Texture.TextureFilter.Linear;
            font = freeTypeFontGenerator.generateFont(freeTypeFontParameter);
            font.setUseIntegerPositions(false);
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
