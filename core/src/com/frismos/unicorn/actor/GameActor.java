package com.frismos.unicorn.actor;

import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;

public abstract class GameActor extends SpineActor {

    protected UserData userData;
    public ColorType colorType;

    public GameActor(GameStage stage, ColorType colorType) {
        this(stage, null, colorType);
    }

    public GameActor(GameStage stage, UserData userData, ColorType colorType) {
        super(stage);
        this.userData = userData;
        this.userData.actor = this;
        this.colorType = colorType;
        this.userData.colorType = this.colorType;
        this.debug();
    }

    @Override
    public boolean remove() {
        if(!this.hasParent()) {
            return false;
        }
        gameStage.collisionDetector.collisionListeners.removeValue(this, false);
        return super.remove();
    }

    public abstract UserData getUserData();
}