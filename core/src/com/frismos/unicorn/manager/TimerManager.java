package com.frismos.unicorn.manager;

import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.util.Timer;

/**
 * Created by edgaravanyan on 2/2/16.
 */
public class TimerManager implements Updatable{

    private Array<Timer> timers = new Array<>();
    private Array<Timer> removeTimers = new Array<>();

    public TimerManager() {

    }

    public void reset() {
        timers.clear();
    }

    public Timer run(float timeStep, TimerRunnable runnable) {
        timers.add(new Timer(timeStep, runnable, false));
        return timers.get(timers.size - 1);
    }

    public Timer loop(float timeStep, TimerRunnable runnable) {
        timers.add(new Timer(timeStep, runnable, true));
        return timers.get(timers.size - 1);
    }

    public void removeTimer(Timer timer) {
        timers.removeValue(timer, false);
    }

    @Override
    public void update(float delta) {
        for(int i = 0; i < timers.size; i++) {
            if(timers.get(i).isCompleted) {
                removeTimers.add(timers.get(i));
            } else {
                timers.get(i).update(delta);
            }
        }
        for (int i = 0; i < removeTimers.size; i++) {
            timers.removeValue(removeTimers.get(i), false);
        }
        removeTimers.clear();
    }
}
