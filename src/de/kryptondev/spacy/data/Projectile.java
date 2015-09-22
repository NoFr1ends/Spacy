package de.kryptondev.spacy.data;

import java.util.ArrayList;


public class Projectile extends Entity {

    public Projectile() {
    }

    public int damage;
    public int damagerange;
    public int lifetime;
    public DamageType damagetype;
    public ArrayList<Effect> Effects;
    public Ship sender;
    
}
