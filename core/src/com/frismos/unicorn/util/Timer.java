package com.frismos.unicorn.util;

import com.frismos.unicorn.manager.TimerRunnable;
import com.frismos.unicorn.manager.Updatable;

/**
 * Created by eavanyan on 2/11/16.
 */
public class Timer implements Updatable{
    private float timeStep;
    private float timer;
    private boolean isLooping;
    private TimerRunnable runnable;

    public boolean isCompleted = false;

    public Timer(float timeStep, TimerRunnable runnable, boolean isLooping) {
        this.timeStep = timeStep;
        this.runnable = runnable;
        this.isLooping = isLooping;
    }

    public void setTimeStep(float timeStep) {
        reset();
        this.timeStep = timeStep;
    }

    private void tick() {
        this.runnable.run(this);
        if(!isLooping) {
            dispose();
        }
    }

    public void dispose() {
        isCompleted = true;
    }

    @Override
    public void update(float delta) {
        timer += delta;
        if(timer >= timeStep) {
            timer = 0;
            tick();
        }
    }

    public float getTimeStep() {
        return timeStep;
    }

    public void reset() {
        timer = 0;
    }
}
