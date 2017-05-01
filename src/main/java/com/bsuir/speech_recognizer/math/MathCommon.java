package com.bsuir.speech_recognizer.math;

public class MathCommon {

    public static double log(double value, int base) {
        double result = 0;
        result = Math.log(value) / Math.log(base);
        return result;
    }

    public static double euclidianDistanceWithWeight(double[] firstVector, double[] secondVector, double[] weights) {
        double result = 0;
        int length = firstVector.length;
        for (int i = 0; i < length; i++) {
            double temp = firstVector[i] - secondVector[i];
            result += Math.pow(temp, 2) * weights[i];
        }
        result = Math.sqrt(result);
        return result;
    }
}
