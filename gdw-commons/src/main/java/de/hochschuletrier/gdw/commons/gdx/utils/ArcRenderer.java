package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Circle;

/**
 *
 * @author Santo Pfingsten
 */
public class ArcRenderer {

    private final Mesh mesh;
    private int maxVertices;

    public ArcRenderer(int maxVertices) {
        this.maxVertices = maxVertices;

        float[] vertices = new float[maxVertices * 2];
        vertices[0] = 0;
        vertices[1] = 0;
        double offset = -0.5f * Math.PI;
        double stepSize = 2 * Math.PI / (maxVertices - 3);
        int i;
        for (i = 0; i < maxVertices - 2; i++) {
            vertices[2 * i + 2] = (float) (Math.cos(offset));
            vertices[2 * i + 3] = (float) (Math.sin(offset));
            offset += stepSize;
        }
        vertices[2 * i + 2] = 0;
        vertices[2 * i + 3] = - 1;
        mesh = new Mesh(false, maxVertices, maxVertices, new VertexAttribute(VertexAttributes.Usage.Position, 2, "circle_position_alias"));
        mesh.setVertices(vertices);
        mesh.setAutoBind(true);
    }

    public void preRender(float x, float y, float radius) {
        DrawUtil.batch.flush();
        Gdx.gl11.glDisable(GL11.GL_TEXTURE_2D);
        DrawUtil.pushTransform();
        DrawUtil.translate(x, y);
        DrawUtil.scale(radius, radius);
        Color color = DrawUtil.getColor();
        Gdx.gl11.glColor4f(color.r, color.g, color.b, color.a);
    }

    public void postRender() {
        DrawUtil.popTransform();
        //todo: only if it was on before?
        Gdx.gl11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void fillProgress(float x, float y, float radius, float progress) {
        int steps = Math.round(progress * (maxVertices - 2));
        if (steps > 0) {
            if (steps > (maxVertices - 2)) {
                steps = (maxVertices - 2);
            }
            preRender(x, y, radius);
            mesh.render(GL11.GL_TRIANGLE_FAN, 0, 1 + steps);
            postRender();
        }
    }

    public void fill(float x, float y, float radius) {
        preRender(x, y, radius);
        mesh.render(GL11.GL_TRIANGLE_FAN, 0, maxVertices);
        postRender();
    }

    public void fill(Circle circle) {
        fill(circle.x, circle.y, circle.radius);
    }

    public void draw(float x, float y, float radius) {
        preRender(x, y, radius);
        mesh.render(GL11.GL_LINE_LOOP, 1, maxVertices - 2);
        postRender();
    }
}
