package de.kryptondev.spacy.data;

import de.kryptondev.spacy.server.GameTick;
import java.util.ArrayList;
import org.newdawn.slick.geom.Vector2f;


public class Projectile extends Entity {   
    public int remainingLifetime = 0;
    public int damage = 0;
    public int damagerange = 0;
    public int lifetime = 0;
    public DamageType damagetype;
    public ArrayList<Effect> Effects;
    public long senderId;
    public boolean destroyOnCollision = true;
            
    public Projectile() {
        
    }

    public Projectile(long senderId, Vector2f direction, Vector2f position) {
        super(direction, position);
        this.senderId = senderId;
    }

    public Projectile(DamageType damagetype, long senderId, Vector2f direction, Vector2f position) {
        super(direction, position);
        this.damagetype = damagetype;
        this.senderId = senderId;
    }

    public Projectile(DamageType damagetype, ArrayList<Effect> Effects, long senderId, Vector2f direction, Vector2f position) {
        super(direction, position);
        this.damagetype = damagetype;
        this.Effects = Effects;
        this.senderId = senderId;
    }
    
   
    
    public void setLifeTime(float seconds){
        this.lifetime = (int)(seconds * GameTick.ticksPerSecond);
        this.remainingLifetime = this.lifetime;
    }



    
}
