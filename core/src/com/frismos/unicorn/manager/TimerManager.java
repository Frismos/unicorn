package com.frismos.unicorn.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by edgaravanyan on 2/2/16.
 */
public class TimerManager implements Updatable{

    private Array<Float> timeStepArray;
    private Array<Float> timerArray;
    private Array<Integer> removeIndexArray;
    private ObjectMap<Float, Runnable> runnableMap;

    public TimerManager() {
        timeStepArray = new Array<>();
        timerArray = new Array<>();
        runnableMap = new ObjectMap<>();
        removeIndexArray = new Array<>();
    }

    public void reset() {
        timeStepArray.clear();
        timerArray.clear();
        runnableMap.clear();
    }

    public void run(float timeStep, Runnable runnable) {
        timerArray.add(0.0f);
        timeStepArray.add(timeStep);
        runnableMap.put(timeStep, runnable);
    }

    public void loop(float timeStep, Runnable runnable) {
        timerArray.add(0.0f);
        timeStepArray.add(timeStep);
        runnableMap.put(timeStep, runnable);
    }

    public void removeTimer(float timeStep) {

    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < timerArray.size; i++) {
            if(timerArray.get(i) + delta >= timeStepArray.get(i)) {
                runnableMap.get(timeStepArray.get(i)).run();
                timerArray.set(i, 0.0f);
            } else {
                timerArray.set(i, timerArray.get(i) + delta);
            }
        }
        for (int i = 0; i < removeIndexArray.size; i++) {
            runnableMap.remove(timeStepArray.get(removeIndexArray.get(i)));
            timerArray.removeIndex(removeIndexArray.get(i));
            timeStepArray.removeIndex(removeIndexArray.get(i));
        }
        removeIndexArray.clear();
    }
}
