package de.kryptondev.spacy.share;

import org.newdawn.slick.geom.Vector2f;


public class PlayerRotate extends NetworkPackage  {
    public Vector2f direction;

    public PlayerRotate() {
    }

    public PlayerRotate(Vector2f direction) {
        this.direction = direction;
    }
    
}
