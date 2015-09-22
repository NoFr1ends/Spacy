package de.kryptondev.spacy.data;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


public abstract class Entity {

    public Vector2f direction;
    public Vector2f position;
    public float speed;
    public Rectangle bounds;
    public long id;
    public float maxSpeed;
    public boolean visible=true; //Vorbereitung für eventuelles PowerUp "Unsichtbarkeit"

    public Entity() {
    }
    
    
    public void move() {
        Vector2f newPosition = new Vector2f();
        newPosition.x = position.x + (direction.x * speed);
        newPosition.y = position.y + (direction.y * speed);
        position = newPosition;
    }
}
