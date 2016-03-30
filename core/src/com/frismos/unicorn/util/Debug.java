package com.frismos.unicorn.util;

import com.badlogic.gdx.Gdx;

/**
 * Created by edgaravanyan on 11/11/15.
 */
public class Debug {
    public static final String LOG_TAG = "eavanyan";

    public static void log(String message) {
        Gdx.app.log(LOG_TAG, message);
    }
}


