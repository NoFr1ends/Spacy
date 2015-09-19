package de.kryptondev.spacy.helper;

import com.esotericsoftware.kryo.Kryo;
import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.*;
import java.util.Date;
import java.util.HashMap;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;


public class KryoRegisterer {
    public static void registerAll(Kryo k){        
        k.register(Chatmessage.class);        
        k.register(ConnectionAttemptResponse.class);
        k.register(ConnectionInfo.class);
        k.register(PlayerInfo.class);
        k.register(Version.class);  
        k.register(World.class);  
        k.register(Entity.class);  
        k.register(Weapon.class);    
        k.register(Ship.class);  
        k.register(Projectile.class);  
        k.register(Shield.class);
        k.register(DamageType.class);
        
        k.register(Rectangle.class);
        k.register(Vector2f.class); 
        
        k.register(boolean.class);  
        k.register(float.class);
        k.register(int.class);
        k.register(byte[].class);
        k.register(String.class);
        k.register(Date.class);
        k.register(HashMap.class);  
        
        

    }
}
