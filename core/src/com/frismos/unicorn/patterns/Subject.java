package com.frismos.unicorn.patterns;

import com.badlogic.gdx.utils.Array;

import java.util.Observer;

/**
 * Created by eavanyan on 3/15/16.
 */
public class Subject {
    public Array<Observer> listeners;

    public Subject() {
        listeners = new Array<>();
    }

    public void addObserver(Observer o) {
        if(!listeners.contains(o, false)) {
            listeners.add(o);
        }
    }

    public boolean removeObserver(Observer o) {
        return listeners.removeValue(o, false);
    }

    public void notifyObservers(Object arg) {
        for(int i = 0; i < listeners.size; i++) {
            listeners.get(i).update(null, arg);
        }
    }
}
