package de.kryptondev.spacy.data;

public class DebugTickDelta {
    public int tickDelta;
    public int ticksPerSecond;
    public DebugTickDelta() {
    }

    public DebugTickDelta(int tickDelta, int ticksPerSecond) {
        this.tickDelta = tickDelta;
        this.ticksPerSecond = ticksPerSecond;
    }     
}