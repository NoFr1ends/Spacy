package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.Move;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {

    public boolean isMoving;
    public Vector2f direction;
    public Vector2f position;
    public float speed = 10f;
    public float acceleration = 0f; //nötig?
    public float maxSpeed = 50f;
    public Rect bounds;
    public long id;

    public String image;
    public boolean visible = true; //Vorbereitung für eventuelles PowerUp "Unsichtbarkeit"

    public Entity() {
    }

    public Entity(Vector2f direction, Vector2f position) {
        this.direction = direction;
        this.position = position;
    }

    public void move() {
        //TODO: FIX!!!
        /*if (speed < maxSpeed) {
            speed *= (acceleration + 1);
        } else {
            speed = maxSpeed;
        }
        */
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
   
    public void rotateToMouse(Vector2f mouseposition) {
        //Ich weiß, das geht auch in einer Zeile, aber dann wird es unlesbar.
        Vector2f newDirection = new Vector2f();
        newDirection.x = mouseposition.x - this.getCenter().x;
        newDirection.y = mouseposition.y - this.getCenter().y;

        this.direction = newDirection.normalise(); //durch .normalise() erhält der Vector die Länge 1.
    }
    
    public Vector2f getCenter(){
        Vector2f v = new Vector2f((this.bounds.width + this.bounds.x) / 2, (this.bounds.height + this.bounds.y) / 2);
        return new Vector2f(position).add(v);
    }
}
