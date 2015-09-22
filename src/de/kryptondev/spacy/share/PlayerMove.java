package de.kryptondev.spacy.share;


public class PlayerMove {
    public enum Status{
        Start,
        Stop
    }
    public Status status;

    public PlayerMove() {
    }

    public PlayerMove(Status status) {
        this.status = status;
    }
    
}
