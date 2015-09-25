package de.kryptondev.spacy.share;

import java.util.Date;


public abstract class NetworkPackage {
    public long time = new Date().getTime();
    public void refreshTime(){
        time = System.currentTimeMillis();
    }
}
