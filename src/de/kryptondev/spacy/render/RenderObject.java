package de.kryptondev.spacy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public abstract class RenderObject {

    protected Vector4f color;
    protected int texture = 0;
    protected Vector2f size;
    protected Vector2f[] texCoords = new Vector2f[0];
    
    public void draw(float x, float y) {
        draw(new Vector2f(x, y));
    }
    
    public void draw(Vector2f pos) {
        RenderState.setTexture(texture);
        RenderState.setRenderMode(GL11.GL_QUADS);
        RenderState.setColor(color);
        
        /*GL11.glEnd();
        GL11.glPushMatrix();
        GL11.glRotatef(20, 0, 0, 1);
        GL11.glBegin(GL11.GL_QUADS);*/
        
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
        
        /*GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glBegin(GL11.GL_QUADS);*/
    }

}
