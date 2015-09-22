package de.kryptondev.spacy.data;
import de.kryptondev.spacy.share.PlayerInfo;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.*;

public class Ship extends Entity implements IHittable{
    public float turnspeed;
    public PlayerInfo owner;
    public Weapon activeWeapon;
    public String image;
    public Shield shield;
   
    public Ship(Vector2f position, Vector2f direction, float speed, Rectangle bounds, Weapon activeWeapon, String image, Shield shield) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.bounds = bounds;
        this.activeWeapon = activeWeapon;
        this.image = image;
        this.shield = shield;
    }

    public Ship() {
        this.position = new Vector2f(0,0);
        this.direction = new Vector2f(0,0);
        this.speed = 0;
        this.maxSpeed=1;
        this.bounds = new Rectangle(0,0,100,100);
        this.activeWeapon = new Weapon();
        this.image = "";
        this.shield = new Shield();
        
        SpacyMath.Acceleration
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
