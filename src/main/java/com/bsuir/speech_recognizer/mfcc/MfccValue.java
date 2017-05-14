package com.bsuir.speech_recognizer.mfcc;

import java.io.Serializable;

public class MfccValue implements Serializable {
    private double[] value;

    public MfccValue() {

    }

    public MfccValue(double[] value) {
        this.value = value;
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }
}
