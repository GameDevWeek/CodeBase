package de.hochschuletrier.gdw.commons.gdx.assets;

import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.TiledMapLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AsynchronousAssetLoaderX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.matthiasmann.javafreetype.FreeTypeFont;

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

		setLoader(Animation.class, new AnimationLoader(resolver));
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

	public Animation getAnimation(String name) {
		return getByName(name, Animation.class);
	}

	public BitmapFont getFont(String name, int size) {
		BitmapFont f = getByName(name, BitmapFont.class);
		if (f == null) {
			f = generateFont(name, size);
		}
		return f;
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


	private BitmapFont generateFont(String name, int size) {
		BitmapFont font = null;
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("data/fonts/" + name));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		font = fontGenerator.generateFont(parameter);
		return font;
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
		if (loader instanceof AsynchronousAssetLoaderX) {
			AsynchronousAssetLoaderX loaderX = (AsynchronousAssetLoaderX) loader;
			return loaderX.getFilePrefix() + filename;
		}
		return filename;
	}

	public <T> void loadAssetList(String filename, Class<T> clazz,
			AssetLoaderParameters<T> parameter) {
		try {
			HashMap<String, String> baseMap = getBaseMap(clazz);

			HashMap<String, String> map = JacksonReader.readMap(filename, String.class);
			for (Entry<String, String> entry : map.entrySet()) {
				String file = prefixFilename(clazz, entry.getValue());
				load(file, clazz, parameter);
				baseMap.put(entry.getKey(), file);
			}
		} catch (Exception e) {
			throw new GdxRuntimeException("Error reading file: " + filename, e);
		}
	}

	public <T, PT extends AssetLoaderParametersX<T>> void loadAssetListWithParam(
			String filename, Class<T> clazz, Class<PT> parameterClazz) {
		try {
			HashMap<String, String> baseMap = getBaseMap(clazz);

			HashMap<String, PT> map = JacksonReader.readMap(filename, parameterClazz);
			for (Map.Entry<String, PT> entry : map.entrySet()) {
				String file = prefixFilename(clazz, entry.getValue().filename);
				load(file, clazz, entry.getValue());
				baseMap.put(entry.getKey(), file);
			}
		} catch (Exception e) {
			throw new GdxRuntimeException("Error reading file: " + filename, e);
		}
	}
}
