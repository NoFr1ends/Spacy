package de.kryptondev.spacy.render;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class Texture extends RenderObject {

    public Texture() {        
        size = new Vector2f(100, 100);
        color = new Vector4f(1, 0, 0, 1);
    }

}
