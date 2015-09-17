/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.kryptondev.spacy.data;
import de.kryptondev.spacy.*;
import java.util.ArrayList;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.*;

/**
 *
 * @author cssand
 */
public class Ship implements IHittable{
    private Vector2f position;
    private float direction;
    private float speed;
    private float turnspeed;
    private Rectangle bounds;
    private ArrayList<Weapon> weapons;
    private Weapon activeWeapon;
    private String image;
    private Shield shield;

    public Ship(Vector2f position, float direction, float speed, float turnspeed, Rectangle bounds, ArrayList<Weapon> weapons, Weapon activeWeapon, String image, Shield shield) {
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

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getTurnspeed() {
        return turnspeed;
    }

    public void setTurnspeed(float turnspeed) {
        this.turnspeed = turnspeed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Weapon getActiveWeapon() {
        return activeWeapon;
    }

    public void setActiveWeapon(Weapon activeWeapon) {
        this.activeWeapon = activeWeapon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Shield getShield() {
        return shield;
    }

    public void setShield(Shield shield) {
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
