package de.kryptondev.spacy.data;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {

    public EMoving moving;
    public Vector2f direction;
    public Vector2f position;
    public float speed = 10f;
    public float acceleration = 3f; //nötig? Carl:Ja! aber mehr als 0 ;-)
    public float maxSpeed = 100f;
    public float boundsRadius = 0f;
    public long movementChangedTime;
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

    public void accelerate(int delta, float timeOffSetPercentage) {
        if (this.speed == 0) {
            this.speed = 1;
            return;
        }
        
        float next = 3*(timeOffSetPercentage * timeOffSetPercentage)+ this.speed;
        //System.out.println("Time : "+ currentTime + "-next= "+next+" ; (3*"+ TimeOffSetPercentage+ "("+ movementChangedTime+")" +"²+"+this.speed + ")");
        if (next < this.maxSpeed) {
            this.speed = next;
        } else {
            this.speed = this.maxSpeed;
            moving = EMoving.FullSpeed;
        }
    }

    public void deccelerate(int delta, float timeOffSetPercentage) {
        
        float next = this.speed - 3*(timeOffSetPercentage * timeOffSetPercentage);
        System.out.println(next);
        if (next > 1) {
            this.speed = next;
        } else {
            this.speed = 0;
            moving = EMoving.Stopped;
        }

    }

    public void drawRotation(Graphics g) {
        float size = 50f;
        g.setColor(Color.yellow);
        g.drawLine(this.position.x, this.position.y, this.position.x + this.direction.x * size, this.position.y + this.direction.y * size);
    }

    public void drawBounds(Graphics g) {
        g.setColor(Color.magenta);
        g.drawOval(this.position.x - this.boundsRadius / 2, this.position.y - this.boundsRadius / 2, this.boundsRadius, this.boundsRadius);
    }

    public void move( int delta) {
        //System.out.println(Float.toString(speed) + "-" + Float.toString(maxSpeed));

        Vector2f oldPos = position;
        long currentTime = (System.nanoTime() /1000000) - delta;
        float TimeOffSet = currentTime - movementChangedTime;
        float TimeOffSetPercentage = TimeOffSet / (this.acceleration  * 1000);
        
        if (moving == EMoving.Accelerating) {
            //moving = EMoving.FullSpeed;
            this.accelerate(delta, TimeOffSetPercentage);
        }
        if (moving == EMoving.Deccelerating) {
            //moving = EMoving.Stopped;
            this.deccelerate(delta, TimeOffSetPercentage);
        }
        if (moving == EMoving.FullSpeed) {
            this.speed = maxSpeed;
        }
        if (moving == EMoving.Stopped) {
            this.speed = 0;
            return;
        }
        Vector2f newPosition = new Vector2f();
        newPosition.x = position.x + (direction.x * speed) * (delta / 16f);
        newPosition.y = position.y + (direction.y * speed) * (delta / 16f);
        position = newPosition;
        
        //System.out.println(id + ": " + moving + " with speed " + speed + " in direction " + direction);
    }

    public float getRotation() {
//        float alpha = (float) Math.acos((double) (direction.y / (Math.sqrt(direction.x * direction.x + direction.y * direction.y))));
//        if (alpha < 0) {
//            alpha += 360;
//        }
//        return alpha;

        /*if(this instanceof Ship) {
            System.out.println(direction);
        }*/
        return (float) (Math.atan2(direction.y, direction.x) * 180 / Math.PI) + 90;
    }

    public void rotateToMouse(Vector2f mouseposition) {
        //Ich weiß, das geht auch in einer Zeile, aber dann wird es unlesbar.
        Vector2f newDirection = new Vector2f();
        newDirection.x = mouseposition.x - this.position.x;
        newDirection.y = mouseposition.y - this.position.y;

        this.direction = newDirection.normalise(); //durch .normalise() erhält der Vector die Länge 1.
    }

    public Vector2f getCenteredRenderPos() {
        if (textureBounds == null) {
            float r = boundsRadius;
            return new Vector2f(position).sub(new Vector2f(r, r));
        } else {
            return new Vector2f(position).sub(new Vector2f((textureBounds.x) / 2, (textureBounds.y) / 2));
        }

    }

    public Vector2f getBulletRenderPos() {
        if (textureBounds == null) {
            float r = boundsRadius;
            return new Vector2f(position).sub(new Vector2f(r, 0));
        } else {
            return new Vector2f(position).sub(new Vector2f((textureBounds.x) / 2, 0));
        }
    }

    public Vector2f getBounds() {
        if (textureBounds == null) {
            return new Vector2f(boundsRadius * 2, boundsRadius * 2);
        } else {
            return textureBounds;
        }
    }
}
