package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.PlayerInfo;
import org.newdawn.slick.geom.Vector2f;


public class Ship extends Entity implements IHittable {

    public float turnspeed;
    public PlayerInfo owner;
    public Weapon activeWeapon;
    public Shield shield;
    public int hp = 100;
    public int maxHp = 100;

    public Ship() {
        this.position = new Vector2f(0, 0);
        this.direction = new Vector2f(0, 0);
        
        this.acceleration = 1.2f;
        this.speed = 0f;      
        this.maxSpeed = 50f;
        this.moving = EMoving.Stopped;
        
        this.boundsRadius = 87f;
        this.texture = "";
        
        
    }

    @Override
    public void hit(Projectile hitting) {
        /*Wenn das Projektil trifft, wird zunächst geprüft, ob das Schiff ein Schild hat.
         Falls dieses Schild vom selben Schadenstyp ist wie die Waffe, werden Punkte von der Schildenergie abgezogen.
         Falls nicht geht der Schaden aufs Leben des Schiffes.
        Sollte das Schild weniger Punkte haben als Schaden reinkommt, wird die Differenz vom Leben abgezogen.
        Sollte das Schild augfebraucht werden, wird es auf null gesetzt.
         */
        if (this.shield != null) {
            if (this.shield.resistance == hitting.damagetype) {
                if (this.shield.life > hitting.damage) {
                    this.shield.life = (this.shield.life - hitting.damage);
                } else if (this.shield.life == hitting.damage) {
                    this.shield = null;
                } else if (this.shield.life < hitting.damage) {
                     this.hp=this.hp-(hitting.damage-this.shield.life);
                     this.shield = null;
                }
            }
        }
        else
        {
            this.hp -= hitting.damage;
        }
    }

}
