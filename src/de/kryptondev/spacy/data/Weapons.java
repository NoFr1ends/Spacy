package de.kryptondev.spacy.data;
import java.util.ArrayList;

public class Weapons {
//Benennung nach bekannten echten und fiktiven Waffen
    static Weapon StandardBallisticCannon = new Weapon(
            "Schwerer Gustav",
            1000,
            ProjectileGenerator(2.5f, 50, 4.5f*3, 30, EMoving.FullSpeed, 0, DamageType.balistic, "laserRed01.png", null, true, true)
    );
    
    static Weapon AreaBallisticCannon = new Weapon (
            "",
            3000,
            ProjectileGenerator(4f, 24, 4.5f*3, 65, EMoving.FullSpeed, 25, DamageType.balistic, null, null, true, true)//Needs to be updated
    );
    
    private static Projectile ProjectileGenerator(float lifetime, float maxSpeed, float boundsRadius,
            int damage, EMoving moving, int damageRange, DamageType damagetype, String texture,
            ArrayList<Effect> Effects, boolean destroyOnCollision, boolean visible ){
        Projectile p = new Projectile();
        
        p.setLifeTime(lifetime);
        p.maxSpeed = maxSpeed;
        p.speed = p.maxSpeed;
        
        p.boundsRadius = boundsRadius;
        p.damage = damage;
        p.moving = moving;
        p.damageRange = damageRange;
        p.damagetype = damagetype;
        p.texture = texture;
        p.Effects = Effects;
        p.destroyOnCollision = destroyOnCollision;
        p.visible = visible;
        return p;
    }
}
