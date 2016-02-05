package com.frismos.unicorn.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgaravanyan on 2/2/16.
 */
public class TimerManager implements Updatable{

    private Array<Float> timeStepArray;
    private Array<Float> timerArray;
    private Array<Integer> removeIndexArray;
    private Array<Integer> removeIndexes;
    private ObjectMap<Float, Runnable> runnableMap;

    public TimerManager() {
        timeStepArray = new Array<>();
        timerArray = new Array<>();
        runnableMap = new ObjectMap<>();
        removeIndexArray = new Array<>();
        removeIndexes = new Array<>();
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
        removeIndexArray.add(timeStepArray.size - 1);
        Debug.Log("timer run method: " + timerArray.size);
    }

    public void loop(float timeStep, Runnable runnable) {
        timerArray.add(0.0f);
        timeStepArray.add(timeStep);
        runnableMap.put(timeStep, runnable);
    }

    public void removeTimer(float timeStep) {
        removeIndexArray.removeIndex(timeStepArray.indexOf(timeStep, true));
        runnableMap.remove(timeStep);
        timerArray.removeIndex(timeStepArray.indexOf(timeStep, true));
        timeStepArray.removeValue(timeStep, true);
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < timerArray.size; i++) {
            if(timerArray.get(i) + delta >= timeStepArray.get(i)) {
                runnableMap.get(timeStepArray.get(i)).run();
                if(removeIndexArray.contains(i, true)) {
                    removeIndexes.add(i);
                    removeIndexArray.removeValue(i, true);
                } else {
                    timerArray.set(i, 0.0f);
                }
            } else {
                timerArray.set(i, timerArray.get(i) + delta);
            }
        }
        for (int i = 0; i < removeIndexes.size; i++) {
            Debug.Log("removed timer time step: " + timeStepArray.get(removeIndexes.get(i)));
            Debug.Log("timers size: " + timeStepArray.size);
            runnableMap.remove(timeStepArray.get(removeIndexes.get(i)));
            timerArray.removeIndex(removeIndexes.get(i));
            timeStepArray.removeIndex(removeIndexes.get(i));
        }
        removeIndexes.clear();
    }
}
