package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.ColorUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AnimatorController {

    private final HashMap<String, TextStyle> textStyles = new HashMap();
    private final HashMap<String, Queue> queues = new HashMap();
    private final HashMap<String, Path> paths = new HashMap();
    
    public AnimatorController(AssetManagerX assetManager, String filename) throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
            InstantiationException, ParseException {
        AnimatorData credits = JacksonReader.read(filename, AnimatorData.class);
        
        for(Map.Entry<String, AnimatorData.TextStyle> entry: credits.textStyles.entrySet()) {
            AnimatorData.TextStyle value = entry.getValue();
            BitmapFont font = assetManager.getFont(value.font, value.size);
            Color color = ColorUtil.fromHexString(value.color);
            textStyles.put(entry.getKey(), new TextStyle(font, color, value.align));
        }
        
        for(Map.Entry<String, AnimatorData.Path> entry: credits.paths.entrySet()) {
            AnimatorData.Path value = entry.getValue();
        }
        
        for(Map.Entry<String, AnimatorData.Queue> entry: credits.queues.entrySet()) {
            AnimatorData.Queue value = entry.getValue();
            
        }
    }
}
