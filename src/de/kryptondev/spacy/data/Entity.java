package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.Move;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {

    public boolean isMoving;
    public Vector2f direction;
    public Vector2f position;
    public float speed = 10f;
    public float acceleration = 1.5f; //nötig? Carl:Ja! aber mehr als 0 ;-)
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

    public void accelerate() {
        if ((this.acceleration * this.speed + 1) < this.maxSpeed) {
            this.speed = (this.acceleration * this.speed + 1);//Lineare Beschleunigung, können aber auch was anderes nehmen.
        } else {
            this.speed = this.maxSpeed;
           
        }

    }
    /*Der Abbremsvorgang. Mal sehen wo wir das einbauen*/
public void decelerate() {
        if (((1/this.acceleration) * this.speed - 1) > 0) {
            this.speed = ((1/this.acceleration) * this.speed - 1);//Lineare Bremskraft, können aber auch was anderes nehmen.
        } else {
            this.speed = 0;
        
        }

    }
    public void move() {
        //Theoretisch kann die Verzweigung weggelassen werden.
        //Die Frage ist ob eine Verzweigung performanter ist als ein unnötiger Methodenaufruf.
        if (speed < maxSpeed & this.isMoving) {
            this.accelerate();
        }
        if(speed > 0 & !this.isMoving){
            this.decelerate();
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

    public void rotateToMouse(Vector2f mouseposition) {
        //Ich weiß, das geht auch in einer Zeile, aber dann wird es unlesbar.
        Vector2f newDirection = new Vector2f();
        newDirection.x = mouseposition.x - this.position.x;
        newDirection.y = mouseposition.y - this.position.y;

        this.direction = newDirection.normalise(); //durch .normalise() erhält der Vector die Länge 1.
    }

    public Vector2f getRenderPos() {
        return new Vector2f(position).sub(new Vector2f((bounds.width) / 2, (bounds.width) / 2));
    }
}
