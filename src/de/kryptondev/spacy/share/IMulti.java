package de.kryptondev.spacy.share;

/**
* For multiple packages for one type of packet (class)
* Example:      Position, Death, Chatmessage, Kill notification, Hitmarker
* 
* No IMulti:    Version, ConnectionInfo
*               This packages were sent just 1 time.
*/
public interface IMulti {
    int packageId = 0;
    
}
