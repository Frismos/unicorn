package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.esotericsoftware.spine.utils.SkeletonActorPool;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 11/28/2015.
 */
public class AtlasManager {


    private SkeletonRenderer skeletonRenderer;

    private ObjectMap<Class, SkeletonData> skeletonDatas = new ObjectMap<>();
    private ObjectMap<Class, SkeletonJson> skeletonJsons = new ObjectMap<>();
    private ObjectMap<SkeletonData, SkeletonActorPool> skeletonActorPools = new ObjectMap<>();

    private AssetManager manager;

    public AtlasManager() {
        manager = new AssetManager();
        skeletonRenderer = new SkeletonMeshRenderer();
    }

    public <T> T get(String fileName, Class<T> type) {
        if(manager.isLoaded(fileName)) {
            return manager.get(fileName);
        }
        manager.load(fileName, type);
        manager.finishLoading();
        return manager.get(fileName);
    }


    public SkeletonData getSkeletonData(Class type, String filePath, SkeletonJson skeletonJson) {
        SkeletonData skeletonData;
        if(skeletonDatas.containsKey(type)) {
            skeletonData = skeletonDatas.get(type);
        } else {
            skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(filePath));
            skeletonDatas.put(type, skeletonData);
        }
        return skeletonData;
    }

    public SkeletonJson getSkeletonJson(Class type, String filePath) {
        TextureAtlas textureAtlas = get(filePath, TextureAtlas.class);
        SkeletonJson skeletonJson;
        if(skeletonJsons.containsKey(type)) {
            skeletonJson = skeletonJsons.get(type);
        } else {
            skeletonJson = new SkeletonJson(textureAtlas);
            skeletonJsons.put(type, skeletonJson);
        }
        return skeletonJson;
    }

    public SkeletonActor getSkeletonActor(SkeletonData skeletonData, AnimationStateData stateData) {
        SkeletonActor skeletonActor;
        if(skeletonActorPools.containsKey(skeletonData)) {
            skeletonActor = skeletonActorPools.get(skeletonData).obtain();
        } else {
            skeletonActorPools.put(skeletonData, new SkeletonActorPool(skeletonRenderer, skeletonData, stateData));
            skeletonActor = skeletonActorPools.get(skeletonData).obtain();
        }
        return skeletonActor;
    }

    public void unloadAsset(String fileName) {
        manager.unload(fileName);
    }

    public void freeSkeletonActor(SkeletonActor skeletonActor) {
        skeletonActorPools.get(skeletonActor.getSkeleton().getData()).free(skeletonActor);
    }
}
