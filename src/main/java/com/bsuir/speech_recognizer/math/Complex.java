package com.bsuir.speech_recognizer.math;

public class Complex {
    double real = 0.0;
    double img = 0.0;

    void multiply(double number) {
        this.real *= number;
        this.img *= img;
    }

    void multiply(Complex number) {
        double tempReal;
        tempReal = real * number.real - img * number.img;

        double tempImg;
        tempImg = real * number.img + img * number.real;

        this.real = tempReal;
        this.img = tempImg;

    }

    Complex plus(Complex number) {
        Complex complex = new Complex();
        complex.real = this.real + number.real;
        complex.img = this.img + number.img;
        return complex;
    }

    Complex minus(Complex number) {
        Complex complex = new Complex();
        complex.real = this.real - number.real;
        complex.img = this.img - number.img;
        return complex;
    }

    double getNormal() {
        double result;
        result = Math.pow(real, 2) + Math.pow(img, 2);
        return Math.sqrt(result);
//        return real;
    }

    Complex() {

    }

    Complex(double real) {
        this.real = real;
    }
}
