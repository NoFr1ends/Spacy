package de.kryptondev.spacy.share;


public class Move {  
    public boolean status;

    public Move() {
    }

    public Move(boolean status) {
        this.status = status;
    }

    public void start(){
        status = true;
    }
    
    public void stop(){
        status = false;
    }
    
    public void invert(){
        status = !status;
    }
}
