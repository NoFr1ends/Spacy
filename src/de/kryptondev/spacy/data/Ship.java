package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Move;
import org.newdawn.slick.geom.Vector2f;


public class Ship extends Entity implements IHittable {

    public float turnspeed;
    public PlayerInfo owner;
    public Weapon activeWeapon;
    public Shield shield;
    public int hp = 100;
    public int maxHp = 100;
    public long entityId;

    public Ship(Vector2f position, Vector2f direction, float speed, Rect bounds, Weapon activeWeapon, String image, Shield shield) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.bounds = bounds;
        this.activeWeapon = activeWeapon;
        this.image = image;
        this.shield = shield;
    }

    public Ship() {
        this.position = new Vector2f(0, 0);
        this.direction = new Vector2f(0, 0);
        this.speed = 0;
        this.maxSpeed = 20;
        this.bounds = new Rect(0, 0, 100, 100);
        this.activeWeapon = new Weapon();
        this.image = "";
        this.shield = new Shield();
        
        //SpacyMath.Acceleration
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
            if (this.shield.getResistance() == hitting.damagetype) {
                if (this.shield.getLife() > hitting.damage) {
                    this.shield.setLife(this.shield.getLife() - hitting.damage);
                } else if (this.shield.getLife() == hitting.damage) {
                    this.shield = null;
                } else if (this.shield.getLife() < hitting.damage) {
                     this.hp=this.hp-(hitting.damage-this.shield.getLife());
                     this.shield = null;
                }
            }
        }
    }

}
