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
import com.frismos.unicorn.util.Debug;

import java.util.Iterator;

/**
 * Created by eavanyan on 2/29/16.
 */
public class SoundManager implements Updatable {

    public static final String ERROR = "error";
    public static final String CHANGE_COLOR = "guyn@ pokhel";
    public static final String KNOCK = "knock";
    public static final String KNOCK_BOUNCE = "knock_bounce";
    public static final String UNICORN_FIRE = "krakel glkhavor";
    public static final String FIRE = "krakel";
    public static final String BOSS_FALL = "monster @nknel";
    public static final String BOSS_HIT = "monster poch";
    public static final String BOSS_VOICE = "monster dzayn";
    public static final String BOSS_EXPLODE = "monster paytel";
    public static final String BOSS_TAIL = "monster astghik";
    public static final String EXPLODE = "paytel@ arag";
    public static final String TEETH_CHATTER = "tetth chatter";
    public static final String JUMP = "trnel";
    public static final String BUTTON = "button";
    public static final String FIRING_LAUGH = "paytel cicagh";
    public static final String RUNNING_DIE = "paytel kosht";
    public static final String ATTACKING_ENEMY_ATTACK = "woodpecker";
    public static final String ATTACKING_ENEMY_DIE = "paytel_attack";
    public static final String MOTHER_BOSS_SCREAM = "scream cartoon";
    public static final String CHEWING_ENEMY = "swallow";
    public static final String CHEWING_ENEMY_CHEW = "eat1";
    public static final String MOTHER_ENEMY_DIE = "paytel6 dzayn";
    public static final String WALKING_ENEMY_HIT = "doorcnock";
    public static final String MOTHER_ENEMY_HIT = "box beat";
    public static final String EYE_BEAT = "beat";
    public static final String COIN = "coin";
    public static final String CRUNCH = "crunch";

    public UnicornGame game;

    private ObjectMap<Music, Actor> actionActors = new ObjectMap<>();
    private Array<Music> keysToRemove = new Array<>();
    public LongMap<Sound> sounds = new LongMap<>();
    public long currentSoundId = -1;
    private float musicPitch = 1.0f;
    public Music currentMusic;

    public SoundManager(UnicornGame game) {
        this.game = game;
    }

    public void playMusic() {
        playMusic("Color_Pony_Game_Music_Slow_", Sound.class, true, true);
    }

    public <T> void playMusic(String path, Class<T> type, boolean play) {
        playMusic(path, type, play, false, 0.5f);
    }

    public <T> void playMusic(String path, Class<T> type, boolean play, boolean loop) {
        float volume = loop ? 1 : 0.5f;
        playMusic(path, type, play, loop, volume);
    }

    public <T> void playMusic(String path, Class<T> type, boolean play, boolean loop, float volume) {
        String filePath = Gdx.files.internal(String.format("sounds/%s.wav", path)).exists() ? String.format("sounds/%s.wav", path) :
                Gdx.files.internal(String.format("sounds/%s.mp3", path)).exists() ? String.format("sounds/%s.mp3", path) : String.format("sounds/%s.ogg", path);
        T music = game.atlasManager.get(filePath, type);
        if(music instanceof Music) {
            if(play) {
                currentMusic = (Music)music;
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
                    long id = ((Sound)music).play();
                    ((Sound)music).setVolume(id, volume);
                    sounds.put(id, (Sound)music);
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

    public void stop() {
        LongMap.Keys keys = sounds.keys();
        while (keys.hasNext) {
            sounds.get(keys.next()).stop();
        }
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }
}
