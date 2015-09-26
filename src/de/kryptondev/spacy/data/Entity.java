package de.kryptondev.spacy.data;

import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
    public EMoving moving;
    public Vector2f direction;
    public Vector2f position;
    public float speed = 10f;
    public float acceleration = 2f; //nötig? Carl:Ja! aber mehr als 0 ;-)
    public float maxSpeed = 100f;
    public float boundsRadius = 0f;
    
    public Vector2f textureBounds;
    
    public long id;
    public String texture;
    
    public boolean visible = true; //Vorbereitung für eventuelles PowerUp "Unsichtbarkeit"

    public Entity() {
    }

    public Entity(Vector2f direction, Vector2f position) {
        this.direction = direction;
        this.position = position;
    }

    public void accelerate() {
        if (this.speed==0){
        this.speed=1;
        return;
        }
        if ((this.acceleration * this.speed) < this.maxSpeed) {
            this.speed = (this.acceleration * this.speed);//Lineare Beschleunigung, können aber auch was anderes nehmen.
            return;
        } else {
            this.speed = this.maxSpeed;
            moving = EMoving.FullSpeed;
        }
    }
    /*Der Abbremsvorgang. Mal sehen wo wir das einbauen*/
    public void decelerate() {
        if (((1/this.acceleration) * this.speed) > 1) {
            this.speed = ((1/this.acceleration) * this.speed);//Lineare Bremskraft, können aber auch was anderes nehmen.
        } else {
            this.speed = 0;
            moving = EMoving.Stopped;
        }

    }
    public void move(int delta) {
        //Theoretisch kann die Verzweigung weggelassen werden.
        //Die Frage ist ob eine Verzweigung performanter ist als ein unnötiger Methodenaufruf.
        //System.out.println(Float.toString(speed) + "-" + Float.toString(maxSpeed));
        
        if (moving == EMoving.Accelerating) {
            this.accelerate();
        }
        if(moving == EMoving.Deccelerating){
            this.decelerate();
        }
        Vector2f newPosition = new Vector2f();
        newPosition.x = position.x + (direction.x * speed) * (delta / 16f);
        newPosition.y = position.y + (direction.y * speed) * (delta / 16f);
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
        if(textureBounds == null){
            float r = boundsRadius;
            return new Vector2f(position).sub(new Vector2f(r,r));
        }
        else
        {
            return new Vector2f(position).sub(new Vector2f((textureBounds.x) / 2, (textureBounds.y) / 2));
        }
        
    }
    
    public Vector2f getBounds(){
        if(textureBounds == null){
            return new Vector2f(boundsRadius * 2, boundsRadius * 2);
        }
        else
        {
            return textureBounds;
        }
    }
}
