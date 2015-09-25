package de.kryptondev.spacy.share;


public class Version extends NetworkPackage {

    public Version(int major, int minor, int bugfix) {
        this.major = major;
        this.minor = minor;
        this.bugfix = bugfix;
    }
    
    public static boolean isCompatible(Version ver1, Version ver2){
        return (ver1.major == ver2.major &
                ver1.minor == ver1.minor);
    }
    
    public boolean isCompatible(Version ver){
        return (this.major == ver.major &
                this.minor == ver.minor);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + bugfix;
    }    

    public Version() {
    }
    
    public int major;
    public int minor;
    public int bugfix;
}
