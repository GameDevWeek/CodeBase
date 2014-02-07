package de.hochschuletrier.gdw.commons.gdx.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;

/**
 *
 * @author Santo Pfingsten
 */
public class ImageXLoader extends AsynchronousAssetLoader<ImageX, ImageXLoader.ImageXParameter> {
	public ImageXLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle fileHandle, ImageXParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
        TextureParameter params = new TextureParameter();
        params.format = parameter.format;
        params.genMipMaps = parameter.genMipMaps;
        params.minFilter = parameter.minFilter;
        params.magFilter = parameter.magFilter;
        params.wrapU = parameter.wrapU;
        params.wrapV = parameter.wrapV;
		deps.add(new AssetDescriptor(fileHandle, Texture.class, params));
		return deps;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle fileHandle, ImageXParameter parameter) {
	}

	@Override
	public ImageX loadSync(AssetManager manager, String fileName, FileHandle fileHandle, ImageXParameter parameter) {
		return new ImageX(manager.get(fileName, Texture.class));
	}

	/** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if additional configuration is
	 * necessary for the {@link ImageX}. */
	static public class ImageXParameter extends AssetLoaderParameters<ImageX> {
		/** the format of the final Texture. Uses the source images format if null **/
		public Pixmap.Format format = null;
		/** whether to generate mipmaps **/
		public boolean genMipMaps = false;
		public TextureFilter minFilter = TextureFilter.Nearest;
		public TextureFilter magFilter = TextureFilter.Nearest;
		public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
		public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
	}
}

