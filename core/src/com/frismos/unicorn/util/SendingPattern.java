package com.frismos.unicorn.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.WaveType;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by eavanyan on 2/29/16.
 */
public class SendingPattern {

    public Array<Integer> rowIndices = new Array<>();
    public Array<ColorType> colorTypes = new Array<>();
    public boolean isVertical;
    public int rowIndex;
    public WaveType enemyType;
    public int patternEnemiesCount;

    public SendingPattern(int size) {
        this(size, null, false);
    }

    public SendingPattern(int size, ColorType type) {
        this(size, type, true);
    }

    public SendingPattern(int size, ColorType type, boolean isVertical) {
        patternEnemiesCount = size;
        this.isVertical = isVertical;

        if(!isVertical) {
            colorTypes.add(ColorType.RED);
            colorTypes.add(ColorType.GREEN);
            colorTypes.add(ColorType.BLUE);
        } else {
            colorTypes.add(type);
            int index = 0;
            for (int i = 0; i < size; i++) {
                rowIndices.add(index++);
                if (rowIndices.get(i) >= 2) {
                    index = 0;
                }
            }
        }
    }

}
