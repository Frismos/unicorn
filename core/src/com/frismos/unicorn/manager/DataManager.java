package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by edgaravanyan on 1/18/16.
 */
public class DataManager {

    public DataManager() {
    }

    public boolean isTutorialPassed() {
        Preferences prefs =Gdx.app.getPreferences("GameData");
        if(prefs.contains("tutorial")) {

        }
        return prefs.getBoolean("tutorial");
    }

    public void setTutorialPassed(boolean isTutorialPassed) {
        Gdx.app.getPreferences("GameData").putBoolean("tutorial", isTutorialPassed).flush();
    }
}
