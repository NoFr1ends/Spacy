package de.kryptondev.spacy.data;
import de.kryptondev.spacy.*;
import java.util.ArrayList;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.*;

public class Ship implements IHittable{
    public Vector2f position;
    public Vector2f direction;
    //public float direction;
    public float speed;
    public float turnspeed;
    public Rectangle bounds;
    public ArrayList<Weapon> weapons;
    public Weapon activeWeapon;
    public String image;
    public Shield shield;

    public Ship(Vector2f position, Vector2f direction, float speed, float turnspeed, Rectangle bounds, ArrayList<Weapon> weapons, Weapon activeWeapon, String image, Shield shield) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.turnspeed = turnspeed;
        this.bounds = bounds;
        this.weapons = weapons;
        this.activeWeapon = activeWeapon;
        this.image = image;
        this.shield = shield;
    }

    
    /**
     *
     * @param hitting
     */
    @Override
    public void hit(Projectile hitting) {
    //Was geschieht wenn das Projektil eintrifft. 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
