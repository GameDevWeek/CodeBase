package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;

public class CreditsMenu extends LaserCatMenu {

    private CreditsMenuListener creditsMenuListener;
    private Table dummyTable;

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        menuCatImage.setVisible(false);
        table.setBackground(catSkin.getDrawable("creditsScreen"));
                
        numberOfButtons = 1;
        name = new String[numberOfButtons];
        name[0] = "Back";
        addButtonsToFrame();

        creditsMenuListener = new CreditsMenuListener();

        for (UIButton b : button) {
            b.addListener(soundListener);
            b.addListener(this.creditsMenuListener);
            b.setOverAnimation(catSkin, "bell", frameDuration);
        }
    }

    protected void addButtonsToFrame() {
        button = new UIButton[numberOfButtons];
        label = new Label[numberOfButtons];
        //table.setDebug(true);
		widgetFrame.reset();

        table.clear();
        table.setFillParent(true);
        
        dummyTable = new Table();
        table.top().add(dummyTable).top().expandX();		// put it on top already, for god's sake!
        
        label[0] = new Label(name[0], catSkin);
        button[0] = new UIButton(catSkin, "bell");
        button[0].setName("bell");
        
		dummyTable.add(titleTextImage).top().size(Value.percentWidth(0.8f,  table),Value.percentHeight(0.25f, table)).left().expandY();
		dummyTable.add(widgetFrame).right().expandY();
		
		widgetFrame.add(label[0]);
		widgetFrame.row();
		widgetFrame.add(button[0]).size(Value.percentWidth(widthOfWidgetFrame/6, table)).top().right().space(20).spaceTop(10);
		
		
        name = null;
    }

    private class CreditsMenuListener extends ClickListener {

        public void clicked(InputEvent event, float x, float y) {
            for (int i = 0; i < numberOfButtons; i++) {
                if (button[i] != event.getListenerActor()) {
                    continue;
                }

                switch (i) {
                    case 0:
                        GameStateEnum.MAINMENU.activate();
                        break;
                    default:
                        System.out.println("You just fucked up");
                        break;
                }
            }
        }
    }
}
