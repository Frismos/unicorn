package com.frismos.unicorn.userdata;

import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UserDataType;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class UserData {
    protected UserDataType userDataType;

    public ColorType colorType;
    public GameActor actor;

    public UserData() {

    }

    public UserDataType getUserDataType() {
        return userDataType;
    }
}
