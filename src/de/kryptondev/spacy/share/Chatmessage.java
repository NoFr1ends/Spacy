package de.kryptondev.spacy.share;

import de.kryptondev.spacy.share.IMulti;


public class Chatmessage implements IMulti {
    public String message = "";
    public String sender = "";  

   
    public Chatmessage(String message) {
        this.sender = "SERVER";
        this.message = message;
    }
     
}
