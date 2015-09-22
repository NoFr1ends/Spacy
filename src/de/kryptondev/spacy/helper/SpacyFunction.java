package de.kryptondev.spacy.helper;

public class SpacyFunction {
    
    double a = 0, potA = 0, b = 0, potB = 0, c = 0, potC = 0;

    public SpacyFunction(double a, double potA) {
        this.a = a;
        this.potA = potA;
    }

    public SpacyFunction(double a, double potA, double b, double potB) {
        this.a = a;
        this.potA = potA;
        this.b = b;
        this.potB = potB;
    }

    public SpacyFunction(double a, double potA, double b, double potB, double c, double potC) {
        this.a = a;
        this.potA = potA;
        this.b = b;
        this.potB = potB;
        this.c = c;
        this.potC = potC;
    }
    
    public double getValue(double x)
    {
      return a * Math.pow(x, potA) + b * Math.pow(x, potB) + c * Math.pow(x, potC);
    }
}