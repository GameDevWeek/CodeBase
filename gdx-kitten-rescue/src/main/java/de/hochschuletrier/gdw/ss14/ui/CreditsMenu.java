package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.gamestates.GameStateEnum;

public class CreditsMenu extends LaserCatMenu {

    private CreditsMenuListener creditsMenuListener;

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        menuCatImage.setVisible(false);
        table.setBackground(catSkin.getDrawable("startScreen"));
                
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
        table.setFillParent(true);
        table.setDebug(true);
        label[0] = new Label(name[0], catSkin);
        button[0] = new UIButton(catSkin, "bell");
        button[0].setName("bell");
        table.clear();
		table.add(titleTextImage).top().center().size(Value.percentWidth(0.8f,  table),Value.percentHeight(0.25f, table)).expandX();
		table.add(label[0]).space(20).spaceBottom(10).right();
		table.row();
		table.add(button[0]).right();
		
	
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
