package de.hochschuletrier.gdw.commons.gdx.assetloaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Santo Pfingsten
 */
public class AssetManagerX extends AssetManager {

    private static final HashMap<Class, HashMap<String, String>> assetMaps = new HashMap<Class, HashMap<String, String>>();

    public AssetManagerX() {
        this(new InternalFileHandleResolver());
    }

    public AssetManagerX(FileHandleResolver resolver) {
        super(resolver);

        setLoader(ImageX.class, new ImageXLoader(resolver));
        setLoader(AnimationX.class, new AnimationXLoader(resolver));
        setLoader(FontX.class, new FontXLoader(resolver));
        setLoader(TiledMap.class, new TiledMapLoader(resolver));
    }

    public <T> T getByName(String name, Class<T> type) {
        HashMap<String, String> map = assetMaps.get(type);
        if (map != null) {
            String filename = map.get(name);
            if (filename != null) {
                return super.get(filename, type);
            }
        }
        return null;
    }

    public ImageX getImageX(String name) {
        return getByName(name, ImageX.class);
    }

    public AnimationX getAnimationX(String name) {
        return getByName(name, AnimationX.class);
    }

    public FontX getFontX(String name) {
        return getByName(name, FontX.class);
    }

    public TiledMap getTiledMap(String name) {
        return getByName(name, TiledMap.class);
    }

    public Music getMusic(String name) {
        return getByName(name, Music.class);
    }

    public Sound getSound(String name) {
        return getByName(name, Sound.class);
    }

    public Pixmap getPixmap(String name) {
        return getByName(name, Pixmap.class);
    }

    public TextureAtlas getTextureAtlas(String name) {
        return getByName(name, TextureAtlas.class);
    }

    public Texture getTexture(String name) {
        return getByName(name, Texture.class);
    }

    public ParticleEffect getParticleEffect(String name) {
        return getByName(name, ParticleEffect.class);
    }

    @Override
    public synchronized void clear() {
        super.clear();

        assetMaps.clear();
    }

    private <T> HashMap<String, String> getBaseMap(Class<T> clazz) {
        HashMap<String, String> baseMap = assetMaps.get(clazz);
        if (baseMap == null) {
            baseMap = new HashMap<String, String>();
            assetMaps.put(clazz, baseMap);
        }
        return baseMap;
    }

    private <T> String prefixFilename(Class<T> clazz, String filename) {
        AssetLoader loader = getLoader(clazz);
        if(loader instanceof AsynchronousAssetLoaderX) {
            AsynchronousAssetLoaderX loaderX = (AsynchronousAssetLoaderX)loader;
            return loaderX.getFilePrefix() + filename;
        }
        return filename;
    }

    public <T> void loadAssetList(String filename, Class<T> clazz, AssetLoaderParameters<T> parameter) {
        try {
            HashMap<String, String> baseMap = getBaseMap(clazz);

            HashMap<String, String> map = JacksonReader.readMap(filename, String.class);
            for (Entry<String, String> entry : map.entrySet()) {
                String file = prefixFilename(clazz, entry.getValue());
                load(file, clazz, parameter);
                baseMap.put(entry.getKey(), file);
            }
        } catch (Exception e) {
            throw new com.badlogic.gdx.utils.GdxRuntimeException("Error reading file: " + filename, e);
        }
    }

    public <T, PT extends AssetLoaderParametersX<T>> void loadAssetListWithParam(String filename, Class<T> clazz, Class<PT> parameterClazz) {
        try {
            HashMap<String, String> baseMap = getBaseMap(clazz);

            HashMap<String, PT> map = JacksonReader.readMap(filename, parameterClazz);
            for (Map.Entry<String, PT> entry : map.entrySet()) {
                String file = prefixFilename(clazz, entry.getValue().filename);
                load(file, clazz, entry.getValue());
                baseMap.put(entry.getKey(), file);
            }
        } catch (Exception e) {
            throw new com.badlogic.gdx.utils.GdxRuntimeException("Error reading file: " + filename, e);
        }
    }
}
