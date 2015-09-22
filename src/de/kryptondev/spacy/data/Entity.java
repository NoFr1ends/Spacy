package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.Move;
import org.newdawn.slick.geom.Vector2f;


public abstract class Entity {
    public Move move;
    public Vector2f direction;
    public Vector2f position;
    public float speed;
    public float acceleration; //nötig?
    public float maxSpeed;
    public Rect bounds;
    public long id;
    
    public String image;
    public boolean visible = true; //Vorbereitung für eventuelles PowerUp "Unsichtbarkeit"

    public Entity() {
    }

    public void move() {
        if (speed<maxSpeed){
        speed=speed * acceleration+1;
        }else{
            speed=maxSpeed;
        }
        Vector2f newPosition = new Vector2f();
        newPosition.x = position.x + (direction.x * speed);
        newPosition.y = position.y + (direction.y * speed);
        position = newPosition;
    }

    public float getRotation() {
        float alpha = (float) Math.acos((double) (direction.y / (Math.sqrt(direction.x * direction.x + direction.y * direction.y))));
        if (alpha < 0) {
            alpha += 360;
        }
        return alpha;
    }
}
