package com.bsuir.speech_recognizer.math;

public class Normalizer {
    public double[] normalize(byte[] data, int startPosition, int endPosition) {
        double[] result = new double[endPosition - startPosition];
        double vectorLength = getVectorLength(data, startPosition, endPosition);
        int counter = 0;
        for (int i = startPosition; i < endPosition; i++) {
            result[counter++] = data[i] / vectorLength;
        }
        return result;
    }

    private double getVectorLength(byte[] vector, int startPosition, int endPosition) {
        double result = 0;
        for (int i = startPosition; i < endPosition; i++) {
            byte temp = vector[i];
            result += Math.pow(temp, 2);
        }
        result = Math.sqrt(result);
        return result;
    }

    public double[] normalize(double[] data, int startPosition, int endPosition) {
        double[] result = new double[endPosition - startPosition];
        double vectorLength = getVectorLength(data, startPosition, endPosition);
        int counter = 0;
        for (int i = startPosition; i < endPosition; i++) {
            result[counter++] = data[i] / vectorLength;
        }
        return result;
    }

    private double getVectorLength(double[] vector, int startPosition, int endPosition) {
        double result = 0;
        for (int i = startPosition; i < endPosition; i++) {
            double temp = vector[i];
            result += Math.pow(temp, 2);
        }
        result = Math.sqrt(result);
        return result;
    }

    public double[] normalize(double[] data) {
        double[] result = new double[data.length];
        double vectorLength = getVectorLength(data);
        int counter = 0;
        for (int i = 0; i < data.length; i++) {
            result[counter++] = data[i] / vectorLength;
        }
        return result;
    }

    private double getVectorLength(double[] vector) {
        double result = 0;
        for (int i = 0; i < vector.length; i++) {
            double temp = vector[i];
            result += Math.pow(temp, 2);
        }
        result = Math.sqrt(result);
        return result;
    }
}
