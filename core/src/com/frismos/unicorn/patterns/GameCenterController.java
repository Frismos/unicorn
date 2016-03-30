package com.frismos.unicorn.patterns;

import com.badlogic.gdx.Application;

/**
 * Created by edgaravanyan on 1/15/16.
 */
public interface GameCenterController {
    public void login();
    public void loadLeaderboards ();
    public void showLeaderboardView (String identifier);
    public void setKeyWindowRootViewController(Application app);
    public void reportScore (String identifier, long score);
}
