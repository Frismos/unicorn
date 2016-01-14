package com.frismos.unicorn.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.frismos.unicorn.util.Debug;

/**
 * Created by edgar on 11/28/2015.
 */
public class AtlasManager {

    private ObjectMap<Class, SkeletonData> skeletonDatas = new ObjectMap<Class, SkeletonData>();
    private ObjectMap<Class, SkeletonJson> skeletonJsons = new ObjectMap<Class, SkeletonJson>();

    private AssetManager manager;

    public AtlasManager() {
        manager = new AssetManager();
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

    public void unloadAsset(String fileName) {
        manager.unload(fileName);
    }
}
