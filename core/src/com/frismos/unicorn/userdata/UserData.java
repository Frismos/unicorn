package com.frismos.unicorn.userdata;

import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.ActorDataType;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class UserData {
    protected ActorDataType actorDataType;

    public ColorType colorType;
    public GameActor actor;

    public UserData() {

    }

    public ActorDataType getActorDataType() {
        return actorDataType;
    }
}
