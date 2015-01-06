package de.hochschuletrier.gdw.commons.gdx.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import java.util.LinkedList;

/**
 *
 * @author Santo Pfingsten
 */
public class MenuManager implements ScreenListener, Disposable {

    private final Stage stage = new Stage(new ScreenViewport(), DrawUtil.batch);
    private final Table table;
    private final Stack stack;
    private final int width;
    private final int height;
    private Actor currentPage;
    private final LinkedList<Actor> pageStack = new LinkedList();
    private final Runnable onEmptyPop;

    public MenuManager(int width, int height, Runnable onEmptyPop) {
        this.onEmptyPop = onEmptyPop;
        this.width = width;
        this.height = height;
        stack = new Stack();
        table = new Table();
        table.setFillParent(true);
        table.setTransform(false);
        table.row().expand().fill();
        table.add().colspan(3).expand().fill();
        table.row();
        table.add().expandX().fillX();
        table.add(stack).minSize(width, height).maxSize(width, height);
        table.add().expandX().fillX();
        table.row().expand().fill();
        table.add().colspan(3).expand().fill();
        stage.addActor(table);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        stage.getViewport().update(screenWidth, screenHeight, true);
    }

    public void addLayer(Actor actor) {
        actor.setBounds(0, 0, width, height);
        stack.addActor(actor);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void render() {
        DrawUtil.batch.end();
        stage.draw();
        DrawUtil.batch.begin();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void pushPage(Actor page) {
        if (currentPage != null) {
            currentPage.setVisible(false);
            pageStack.push(currentPage);
        }
        currentPage = page;
        currentPage.setVisible(true);
    }

    public void popPage() {
        stage.unfocusAll();
        Gdx.input.setOnscreenKeyboardVisible(false);

        if (pageStack.isEmpty()) {
            if(onEmptyPop != null) {
                onEmptyPop.run();
            }
        } else {
            currentPage.setVisible(false);
            currentPage = pageStack.pop();
            currentPage.setVisible(true);
        }
    }
}
