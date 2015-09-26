package de.kryptondev.spacy.data;


public class Shield {
    public DamageType resistance;
    public int life;

    public Shield() {
    }

    public Shield(DamageType resistance, int life) {
        this.resistance = resistance;
        this.life = life;
    }
}