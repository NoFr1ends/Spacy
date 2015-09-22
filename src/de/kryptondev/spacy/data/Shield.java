package de.kryptondev.spacy.data;


public class Shield {
    private DamageType resistance;
    private int life;

    public Shield() {
    }

    public DamageType getResistance() {
        return resistance;
    }

    public void setResistance(DamageType resistance) {
        this.resistance = resistance;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
    
}
