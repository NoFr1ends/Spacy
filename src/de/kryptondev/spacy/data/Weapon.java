package de.kryptondev.spacy.data;


public class Weapon {
    
    public Projectile ammo;
    public String name;
    public int cooldown;
    public void fire(){}

    public Weapon() {
    }
    
    public Weapon(String name, int cooldown, Projectile ammo) {
        this.name = name;
        this.cooldown = cooldown;
        this.ammo = ammo;
    }
}
