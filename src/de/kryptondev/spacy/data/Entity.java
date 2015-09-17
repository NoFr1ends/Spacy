package de.kryptondev.spacy.data;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public abstract class Entity {
public Vector2f direction;
public Vector2f position;
public float speed;
public Rectangle bounds;
public long id;
}
