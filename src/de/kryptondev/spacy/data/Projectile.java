package de.kryptondev.spacy.data;

import java.util.ArrayList;
import org.lwjgl.util.vector.*;
import org.lwjgl.util.Rectangle;

public class Projectile {

    public Vector2f position;
    public Vector2f direction;
    //private float direction;
    public float speed;

    public double damage;
    public int damagerange;
    public int lifetime;
    public ArrayList<Effect> Effects;
    public Rectangle Bounds;
    public Ship sender;
}
