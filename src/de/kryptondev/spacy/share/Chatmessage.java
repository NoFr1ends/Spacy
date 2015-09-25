package de.kryptondev.spacy.share;

import java.util.Date;


public class Chatmessage extends NetworkPackage  {
    public String message = "";
    public String sender = "";  
    public Date sendTime = new Date();

    public Chatmessage() {
    }
   
    public Chatmessage(String message) {
        this.sender = "SERVER";
        this.message = message;
    }
}
