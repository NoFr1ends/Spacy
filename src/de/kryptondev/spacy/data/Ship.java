package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.PlayerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.newdawn.slick.geom.Vector2f;


public class Ship extends Entity implements IHittable {    
    public float turnspeed;
    public Long owner;
    public Weapon activeWeapon;
    public Shield shield;
    public int hp = 100;
    public int maxHp = 100;

    public Ship() {
        this.position = new Vector2f(0, 0);
        this.direction = new Vector2f(0, 0);
        
        this.acceleration = 5000;
        this.speed = 0f;      
        this.maxSpeed = 21f;
        this.moving = EMoving.Stopped;
        this.activeWeapon = Weapons.StandardBallisticCannon;
//        Projectile p = new Projectile();
//        p.setLifeTime(2.5f);
//        p.maxSpeed = 50;
//        p.speed = p.maxSpeed;
//        
//        p.boundsRadius = 4.5f*3;
//        p.damage = 30;
//        p.moving = EMoving.FullSpeed;
//        p.damageRange = 0;
//        p.damagetype = DamageType.balistic;
//        p.texture = "laserRed01.png";
//        p.Effects = null;
//        p.destroyOnCollision = true;
//        p.visible = true;
//        this.activeWeapon = new Weapon(
//            "Schwerer Gustav",
//            1000,
//            p);
        
        this.boundsRadius = 87f;
        this.texture = "";
        
        
    }
    
    public void SetId(long newId)
    {
        this.id = newId;
        this.activeWeapon.ammo.senderId = newId;
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
