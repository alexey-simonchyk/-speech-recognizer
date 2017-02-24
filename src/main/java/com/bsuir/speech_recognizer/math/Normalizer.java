package com.bsuir.speech_recognizer.math;

public class Normalizer {
    public double[] normalize(byte[] data) {
        double[] result = new double[data.length];
        double vectorLength = getVectorLength(data);
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i] / vectorLength;
        }
        return result;
    }

    private double getVectorLength(byte[] vector) {
        double result = 0;
        for (byte temp : vector) {
            result += Math.pow(temp, 2);
        }
        result = Math.sqrt(result);
        return result;
    }
}
