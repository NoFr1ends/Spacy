package de.kryptondev.spacy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

/**
 * RenderState Manager
 * 
 * This class is used to manage the current render state.
 * It's for preventing texture switching then it's not necessary.
 * @author jbernemann
 */
public class RenderState {
    
    private static int renderMode = 0;
    private static Vector4f color = new Vector4f(0, 0, 0, 1);
    private static int texture = 0;
    
    public static void setRenderMode(int renderMode) {
        if(RenderState.renderMode == renderMode)
            return;
        
        GL11.glEnd();
        if(renderMode > 0)
            GL11.glBegin(renderMode);
        
        RenderState.renderMode = renderMode;
    }
    
    public static void setColor(Vector4f color) {
        if(RenderState.color.equals(color))
            return;
        
        GL11.glColor4f(color.x, color.y, color.z, color.w);
        
        RenderState.color = color;
    }
    
    public static void setTexture(int texture) {
        if(RenderState.texture == texture)
            return;
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        
        RenderState.texture = texture;
    }
    
}
