package de.kryptondev.spacy.share;


public class Move {
    public enum Status{
        Start,
        Stop,
        None
    }
    public Status status;

    public Move() {
    }

    public Move(Status status) {
        this.status = status;
    }
    
}
