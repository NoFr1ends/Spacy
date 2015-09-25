package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.NetworkPackage;


public class Shield extends NetworkPackage{
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
