package com.frismos.unicorn.actor;

import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgaravanyan on 2/1/16.
 */
public class Star extends MainCharacter {
    private static final int MINI_UNICORNS_COUNT = 23;

    private static int miniUnicornsIndex = 0;
    private static final float WAVE_TIME_STEP = 0.2f;
    private static float waveTimeCounter = 0.0f;

    private static Array<MiniUnicorn> miniUnicorns = new Array<>();

    public Star(GameStage stage, UnicornType unicornType) {
        super(stage, unicornType);

        for (int i = 0; i < MINI_UNICORNS_COUNT; i++) {
            miniUnicorns.add(new MiniUnicorn(gameStage));
        }
    }

    @Override
    public void useAbility() {
        if(miniUnicornsIndex == 0) {
            miniUnicornsIndex = MINI_UNICORNS_COUNT;
        }
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.STAR;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.STAR_SCALE_RATIO;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        waveTimeCounter += delta;
        if(waveTimeCounter >= WAVE_TIME_STEP) {
            waveTimeCounter = 0.0f;
            if (miniUnicornsIndex > 0) {
                gameStage.collisionDetector.collisionListeners.add(miniUnicorns.get(miniUnicornsIndex - 1));
                gameStage.addActor(miniUnicorns.get(miniUnicornsIndex - 1));
//                miniUnicorns.get(miniUnicornsIndex - 1).resetPosition();
                miniUnicornsIndex--;
            }
        }
    }
}
