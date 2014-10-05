package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.jackson.*;
import de.hochschuletrier.gdw.ss14.gamestates.*;
import java.util.List;
import java.util.Set;

public class LevelMenu extends LaserCatMenu
{
    private LevelMenuListener levelMenuListener;
    private Label levelLabel;
    private String levelString;
    private static int numberOfLevels;

    private static int levelIndex;
    private static List<MapInfo> mapJson;

    @Override
    public void init(AssetManagerX assetManager)
    {
        super.init(assetManager);

        loadJson();

        levelIndex = new Integer(0);
        numberOfButtons = 4;
        name = new String[numberOfButtons];
        name[0] = "Start";
        name[1] = "Level-";
        name[2] = "Level+";
        name[3] = "Return";

        addButtonsToFrame();

        levelMenuListener = new LevelMenuListener();

        for (UIButton b : button)
        {
            b.addListener(soundListener);
            b.addListener(this.levelMenuListener);
            b.setOverAnimation(catSkin, "bell", frameDuration);
        }


    }

    private static void loadJson()
    {
        try
        {
            //Map <String, String> mapJson = JacksonReader.readMap("data/json/maps.json",String.class);
            mapJson = JacksonReader.readList("data/json/maps.json", MapInfo.class);
            numberOfLevels = mapJson.size();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    protected void addButtonsToFrame()
    {
        button = new UIButton[numberOfButtons];
        label = new Label[numberOfButtons + 1];

        label[0] = new Label(name[0], catSkin);
        label[1] = new Label(name[1], catSkin);
        label[2] = new Label("", catSkin);
        label[3] = new Label(name[2], catSkin);
        label[4] = new Label(name[3], catSkin);

        for (Label l : label)
        {
            widgetFrame.add(l).expandX().space(20).spaceBottom(10);
            l.setAlignment(Align.center);
        }

        widgetFrame.row();
        for (int i = 0; i < numberOfButtons; i++)
        {
            button[i] = new UIButton(catSkin, "bell");
            button[i].setName("bell");
        }
        widgetFrame.add(button[0]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);
        widgetFrame.add(button[1]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

        levelLabel = new Label(mapJson.get(levelIndex).name, catSkin);
        widgetFrame.add(levelLabel).center();

        widgetFrame.add(button[2]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);
        widgetFrame.add(button[3]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).top().space(20).spaceTop(10);

        name = null;
    }

    public static class MapInfo {
        public String name;
        public String path;
    }

    private class LevelMenuListener extends ClickListener
    {
        public void clicked(InputEvent event, float x, float y)
        {
            for (int i = 0; i < numberOfButtons; i++)
            {
                if (button[i] != event.getListenerActor())
                    continue;

                switch (i)
                {
                    case 0:
                        GameStateEnum.GAMEPLAY.activate();
                        System.out.println("load map: " + mapJson.get(levelIndex).name); // placeholder
                        // mapJson.get(mapKeyArray[levelIndex]);
                        break;
                    case 1:
                        levelIndex = ((levelIndex - 1) + numberOfLevels) % numberOfLevels;
                        levelLabel.setText(mapJson.get(levelIndex).name);
                        System.out.println("Decrease Level to " + levelIndex);
                        break;
                    case 2:
                        levelIndex = ((levelIndex + 1) + numberOfLevels) % numberOfLevels;
                        levelLabel.setText(mapJson.get(levelIndex).name);
                        System.out.println("Increase Level to " + levelIndex);
                        break;
                    case 3:
                        GameStateEnum.MAINMENU.activate();
                        break;
                }
            }
        }
    }

    public static String getCurrentLevel()
    {
        if (mapJson == null)
        {
            loadJson();
        }
        String path = mapJson.get(levelIndex).path;
        System.out.println("Start map: " + path);
        return path;
    }
}
