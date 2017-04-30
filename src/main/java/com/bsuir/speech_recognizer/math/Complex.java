package com.bsuir.speech_recognizer.math;

public class Complex {
    public double real = 0.0;
    public double img = 0.0;

    public void multiply(double number) {
        this.real *= number;
        this.img *= img;
    }

    public double getNormal() {
        double result;
        result = Math.pow(real, 2) + Math.pow(img, 2);
        return Math.sqrt(result);
    }
}
