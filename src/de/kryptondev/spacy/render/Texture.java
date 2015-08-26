package de.kryptondev.spacy.render;

import java.io.FileInputStream;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.TextureLoader;

public class Texture extends RenderObject {
    
    public Texture(String texture) {
        this(texture, null);
    }
    
    public Texture(String texture, Rectangle rect) {        
        int texId = 0;
        org.newdawn.slick.opengl.Texture t = null;
        try {
            t = TextureLoader.getTexture("PNG", new FileInputStream(texture));
            texId = t.getTextureID();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        this.texture = texId;
        color = new Vector4f(1, 1, 1, 1);
        size = new Vector2f(rect.getWidth(), rect.getHeight());
        
        calcTexCoords(rect, t);
    }
    
    public Texture(org.newdawn.slick.opengl.Texture texture, Rectangle rect) {
        this.texture = texture.getTextureID();
        size = new Vector2f(rect.getWidth(), rect.getHeight());
        color = new Vector4f(1, 1, 1, 1);
        
        calcTexCoords(rect, texture);
    }
    
    private void calcTexCoords(Rectangle rect, org.newdawn.slick.opengl.Texture texture) {
        texCoords = new Vector2f[4];
        
        int texWidth = texture.getImageWidth();
        int texHeight = texture.getImageHeight();
        float maxX = texture.getWidth();
        float maxY = texture.getHeight();
        
        texCoords[0] = new Vector2f(maxX / texWidth * rect.getX(), maxY / texHeight * rect.getY());
        texCoords[1] = new Vector2f(maxX / texWidth * (rect.getX() + rect.getWidth()), maxY / texHeight * rect.getY());
        texCoords[2] = new Vector2f(maxX / texWidth * (rect.getX() + rect.getWidth()), maxY / texHeight * (rect.getY() + rect.getHeight()));
        texCoords[3] = new Vector2f(maxX / texWidth * rect.getX(), maxY / texHeight * (rect.getY() + rect.getHeight()));
    }

}
