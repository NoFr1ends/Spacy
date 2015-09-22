package de.kryptondev.spacy.data;

import de.kryptondev.spacy.server.GameTick;
import java.util.ArrayList;


public class Projectile extends Entity {

    public Projectile() {
    }

    public Projectile(Ship sender) {
        this.sender = sender;
    }

    public Projectile(DamageType damagetype, Ship sender) {
        this.damagetype = damagetype;
        this.sender = sender;
    }
    
    public void setLifeTime(int seconds){
        this.lifetime = seconds * GameTick.ticksPerSecond;
    }

    public int damage= 0;
    public int damagerange = 0;
    public int lifetime = 0;
    public DamageType damagetype;
    public ArrayList<Effect> Effects;
    public Ship sender;
    
}
