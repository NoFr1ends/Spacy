package de.kryptondev.spacy.share;

import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.share.IMulti;
import java.time.Instant;
import java.util.Date;


public class Chatmessage  {
    public String message = "";
    public String sender = "";  
    public Date sendTime = Date.from(Instant.now());
   
    public Chatmessage(String message) {
        this.sender = "SERVER";
        this.message = message;
    }
}
