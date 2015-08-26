package de.kryptondev.spacy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public abstract class RenderObject {

    protected Vector4f color;
    protected int texture;
    protected Vector2f size;
    
    public void draw(Vector2f pos) {
        RenderState.setRenderMode(GL11.GL_QUADS);
        RenderState.setColor(color);
        RenderState.setTexture(texture);
        
        GL11.glVertex2f(pos.x, pos.y);
        GL11.glVertex2f(pos.x + size.x, pos.y);
        GL11.glVertex2f(pos.x + size.x, pos.y + size.y);
        GL11.glVertex2f(pos.x, pos.y + size.y);
    }

}
