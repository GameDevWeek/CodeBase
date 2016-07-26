package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;

public class ShaderProgramLoader extends
        AsynchronousAssetLoader<ShaderProgram, ShaderProgramLoader.ShaderProgramParameter> {
    
    public ShaderProgramLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    static public class ShaderProgramParameter extends
            AssetLoaderParametersX<ShaderProgram> {

        public String vertex;
        public String fragment;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
            ShaderProgramParameter parameter) {
    }

    @Override
    public ShaderProgram loadSync(AssetManager manager, String fileName,
            FileHandle file, ShaderProgramParameter parameter) {
        final FileHandle vertexFile = Gdx.files.internal(parameter.vertex);
        final FileHandle fragmentFile = Gdx.files.internal(parameter.fragment);
        return new ShaderProgram(vertexFile, fragmentFile);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
            ShaderProgramParameter parameter) {
        return null;
    }

}
