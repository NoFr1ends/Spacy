
package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.NetworkPackage;

public class Effect extends NetworkPackage{
    public String modifiedValue;
    public float modifier;
    public int duration;

    public Effect() {
    }

    public Effect(String modifiedValue, float modifier, int duration) {
        this.modifiedValue = modifiedValue;
        this.modifier = modifier;
        this.duration = duration;
    }
    
}
