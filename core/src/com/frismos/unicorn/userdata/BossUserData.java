package com.frismos.unicorn.userdata;

import com.frismos.unicorn.enums.UserDataType;

/**
 * Created by edgar on 11/29/2015.
 */
public class BossUserData  extends UserData {
    public BossUserData() {
        super();
        userDataType = UserDataType.BOSS;
    }
}
