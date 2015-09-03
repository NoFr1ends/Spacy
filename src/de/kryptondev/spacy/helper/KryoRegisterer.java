package de.kryptondev.spacy.helper;

import com.esotericsoftware.kryo.Kryo;
import de.kryptondev.spacy.share.*;
import java.util.Date;


public class KryoRegisterer {
    public static void registerAll(Kryo k){        
        k.register(Chatmessage.class);        
        k.register(ConnectionAttemptResponse.class);
        k.register(ConnectionInfo.class);
        k.register(PlayerInfo.class);
        k.register(Version.class);  
        
        
        
        k.register(int.class);
        k.register(byte[].class);
        k.register(String.class);
        k.register(Date.class);


    }
}
