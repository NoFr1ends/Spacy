package de.kryptondev.spacy.share;

public class ConnectionInfo extends NetworkPackage  {       
    public Version version;

    public ConnectionInfo() {
    }

    public ConnectionInfo(Version version) {
        this.version = version;
    }
    
}
