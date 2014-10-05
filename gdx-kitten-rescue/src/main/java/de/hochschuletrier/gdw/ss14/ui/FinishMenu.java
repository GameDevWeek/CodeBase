package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;

public class FinishMenu extends LaserCatMenu {
    
    public enum FinishState { WIN, LOSE };
    
    private FinishMenuListener finishMenuListener;
    private FinishState finishState = FinishState.LOSE;
    
    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        
        menuCatImage.setVisible(false);
        
        setBackgroundByState();
        
        heightOfWidgetFrame = 0.75f;
        
        numberOfButtons = 1;
        name = new String[numberOfButtons];
        name[0] = "Back to Mainmenu";
        addButtonsToFrame();

        finishMenuListener = new FinishMenuListener();

        for (UIButton b : button) {
            b.addListener(soundListener);
            b.addListener(this.finishMenuListener);
            b.setOverAnimation(catSkin, "bell", frameDuration);
        }
    }
    
    public void setFinishState(FinishState finishState) {
        this.finishState = finishState;
        
    }
    
    private void setBackgroundByState() {
        switch (this.finishState) {
        case WIN:
            table.setBackground(catSkin.getDrawable("startScreen"));
            break;
        case LOSE:
            table.setBackground(catSkin.getDrawable("main-menu-background"));
            break;
        }
    }

    protected void addButtonsToFrame() {
        button = new UIButton[numberOfButtons];
        label = new Label[numberOfButtons];

        for (int i = 0; i < numberOfButtons; i++) {
            label[i] = new Label(name[i], catSkin);
            widgetFrame.add(label[i]);
        }

        widgetFrame.row();
        for (int i = 0; i < numberOfButtons; i++) {
            button[i] = new UIButton(catSkin, "bell");
            button[i].setName("bell");
            widgetFrame.add(button[i]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).bottom();
        }
        name = null;
    }

    private class FinishMenuListener extends ClickListener {

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
