package de.kryptondev.spacy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public abstract class RenderObject {

    protected Vector4f color;
    protected int texture;
    protected Vector2f size;
    protected Vector2f[] texCoords;
    
    public void draw(Vector2f pos) {
        RenderState.setRenderMode(GL11.GL_QUADS);
        RenderState.setColor(color);
        RenderState.setTexture(texture);
        
        if(texCoords.length == 4)
            GL11.glTexCoord2f(texCoords[0].x, texCoords[0].y);
        GL11.glVertex2f(pos.x, pos.y);
        
        if(texCoords.length == 4)
            GL11.glTexCoord2f(texCoords[1].x, texCoords[1].y);
        GL11.glVertex2f(pos.x + size.x, pos.y);
        
        if(texCoords.length == 4)
            GL11.glTexCoord2f(texCoords[2].x, texCoords[2].y);
        GL11.glVertex2f(pos.x + size.x, pos.y + size.y);
        
        if(texCoords.length == 4)
            GL11.glTexCoord2f(texCoords[3].x, texCoords[3].y);
        GL11.glVertex2f(pos.x, pos.y + size.y);
    }

}
