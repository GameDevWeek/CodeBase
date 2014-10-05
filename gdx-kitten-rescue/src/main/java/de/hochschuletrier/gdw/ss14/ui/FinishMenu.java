package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;
import de.hochschuletrier.gdw.ss14.ui.LaserCatMenu.SoundListener;

public class FinishMenu extends LaserCatMenu {
    
    public enum FinishState { WIN, LOSE };
    
    private FinishMenuListener finishMenuListener;
    private FinishState finishState = FinishState.LOSE;
    
    @Override
    public void init(AssetManagerX assetManager) {
      //Variables
        heightOfWidgetFrame = 0.25f;
        widthOfWidgetFrame = 0.75f;
        frameDuration = 0.05f;
                
        // Adjusts the table and adds it to the stage
        Viewport viewport = new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport);
        
        // Space for main-menu, including background
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        catSkin = new Skin(Gdx.files.internal("data/skins/MainMenuSkin.json"));
        
    
        // Sets Input so it can reach different layers, depending on focus
        Main.inputMultiplexer.addProcessor(stage);
        
        // Sets the Background for every Menu
        setBackgroundByState();
        
        // container for center labels and buttons, no background of its own
        widgetFrame = new Table();
        table.bottom().right().add(widgetFrame).size(Value.percentWidth(widthOfWidgetFrame, table), Value.percentHeight(heightOfWidgetFrame,table));
        table.row();
        

        // Debug Lines
        shapeRenderer = new ShapeRenderer();
        table.debug(Debug.all);
        widgetFrame.debug(Debug.all);
        
        soundListener=new SoundListener();
        
        setBackgroundByState();
        
        numberOfButtons = 1;
        name = new String[numberOfButtons];
        name[0] = "Back to Main Menu";
        addButtonsToFrame();

        finishMenuListener = new FinishMenuListener();

        for (UIButton b : button) {
            b.addListener(soundListener);
            b.addListener(this.finishMenuListener);
            b.setOverAnimation(catSkin, "bell", frameDuration);
        }
        
        Main.getInstance().addScreenListener(this);
    }
    
    public void setFinishState(FinishState finishState) {
        this.finishState = finishState;
        //setBackgroundByState();
    }
    
    private void setBackgroundByState() {
        switch (this.finishState) {
        case WIN:
            table.setBackground(catSkin.getDrawable("winScreen"));
            break;
        case LOSE:
            table.setBackground(catSkin.getDrawable("loseScreen"));
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
            widgetFrame.right().padRight(20).add(button[i]).size(Value.percentWidth(widthOfWidgetFrame / 6, table)).bottom();
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
