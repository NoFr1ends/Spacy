package de.kryptondev.spacy.data;

import org.newdawn.slick.geom.Rectangle;


public class Rect {
    public float x;
    public float y;
    public float width;
    public float height;

    public Rect() {
        x = 0f;
        y = 0f;
    }

    public Rect(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
    }

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Rectangle toRectangle(){
        return new Rectangle(x, y, width, height);
    }
    
}
