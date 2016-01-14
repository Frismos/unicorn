package com.frismos.unicorn.userdata;

import com.frismos.unicorn.enums.UserDataType;

/**
 * Created by edgaravanyan on 10/13/15.
 */
public class EnemyUserData extends UserData {

    public EnemyUserData() {
        super();
        this.userDataType = UserDataType.ENEMY;
    }
}
