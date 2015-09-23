package de.kryptondev.spacy.data;

import de.kryptondev.spacy.server.GameTick;
import java.util.ArrayList;
import org.newdawn.slick.geom.Vector2f;


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

    public Projectile(Ship ship, DamageType damageType, Vector2f direction, Vector2f position) {
        super(direction, position);
        this.damagetype = damagetype;
        this.sender = sender;
    }

    public Projectile(Ship ship, DamageType damageType) {
       this.sender = ship;
       this.damagetype = damageType;
    }
    
    public void setLifeTime(int seconds){
        this.lifetime = seconds * GameTick.ticksPerSecond;
        this.remainingLifetime = this.lifetime;
    }

    public Projectile(Ship sender, Vector2f direction, Vector2f position) {
        super(direction, position);
        this.sender = sender;
    }

   
    public int remainingLifetime = 0;
    public int damage = 0;
    public int damagerange = 0;
    public int lifetime = 0;
    public DamageType damagetype;
    public ArrayList<Effect> Effects;
    public Ship sender;
    
}
