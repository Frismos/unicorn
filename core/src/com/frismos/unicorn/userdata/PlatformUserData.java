package com.frismos.unicorn.userdata;

import com.frismos.unicorn.enums.ActorDataType;

/**
 * Created by edgaravanyan on 10/14/15.
 */
public class PlatformUserData extends UserData {

    public PlatformUserData() {
        super();
        this.actorDataType = ActorDataType.PLATFORM;
    }
}
