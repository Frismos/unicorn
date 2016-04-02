package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.frismos.unicorn.UnicornGame;

import java.util.Iterator;

/**
 * Created by eavanyan on 2/29/16.
 */
public class SoundManager implements Updatable {

    public UnicornGame game;

    private ObjectMap<Music, Actor> actionActors = new ObjectMap<>();
    private Array<Music> keysToRemove = new Array<>();
    public LongMap<Sound> sounds = new LongMap<>();
    public long currentSoundId = -1;
    private float musicPitch = 1.0f;

    public SoundManager(UnicornGame game) {
        this.game = game;
    }

    public void playMusic() {
        playMusic("Color_Pony_Game_Music_Slow_", Sound.class, true, true);
    }

    public <T> void playMusic(String path, Class<T> type, boolean play) {
        playMusic(path, type, play, false);
    }

    public <T> void playMusic(String path, Class<T> type, boolean play, boolean loop) {
        String filePath = Gdx.files.internal(String.format("%s.wav", path)).exists() ? String.format("%s.wav", path) : String.format("%s.ogg", path);
        T music = game.atlasManager.get(filePath, type);
        if(music instanceof Music) {
            if(play) {
                ((Music) music).play();
            }
            ((Music) music).setLooping(true);
        } else if(music instanceof Sound) {
            if(play) {
                if(loop) {
                    do {
                        currentSoundId = ((Sound) music).play();
                    } while(currentSoundId == -1);
                    ((Sound) music).setLooping(currentSoundId, true);
                    sounds.put(currentSoundId, (Sound)music);
                } else {
                    ((Sound)music).play();
                }
            }
        }
    }

    public void addActionToMusic(final Music music, float target, float duration, Runnable runnable) {
        actionActors.put(music, new Actor());
        actionActors.get(music).addAction(Actions.sequence(Actions.alpha(target, duration), Actions.run(new Runnable() {
            @Override
            public void run() {
                keysToRemove.add(music);
            }
        }), Actions.run(runnable)));
    }

    @Override
    public void update(float delta) {
        if(currentSoundId != -1) {
            musicPitch += 0.0023f * delta;
            sounds.get(currentSoundId).setPitch(currentSoundId, musicPitch);
        }
        if(actionActors.size > 0) {
            ObjectMap.Keys<Music> keys = actionActors.keys();
            Iterator<Music> iterator = keys.iterator();
            while (iterator.hasNext()) {
                Music key = iterator.next();
                actionActors.get(key).act(delta);
                key.setVolume(actionActors.get(key).getColor().a);
            }
            for(int i = 0; i < keysToRemove.size; i++) {
                actionActors.remove(keysToRemove.get(i));
            }
            keysToRemove.clear();
        }
    }

    public void reset() {
        musicPitch = 1.0f;
    }
}
