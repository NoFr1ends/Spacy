package de.kryptondev.spacy.render;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/**
 * A render object to display a colored cube.
 * @author jbernemann
 */
public class ColoredRect extends RenderObject {
    
    public ColoredRect(int width, int height, Vector4f color) {
        size = new Vector2f(width, height);
        this.color = color;
        
        texCoords = new Vector2f[4];
        texCoords[0] = new Vector2f(0, 0);
        texCoords[1] = new Vector2f(0, 1);
        texCoords[2] = new Vector2f(1, 1);
        texCoords[3] = new Vector2f(1, 0);
    }
    
}
