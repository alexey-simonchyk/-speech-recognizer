package com.bsuir.speech_recognizer.math;

import com.bsuir.speech_recognizer.sound.SoundFrame;

public class Entropy {
//    private final static double LIMIT = 0.1;
    private final static double LIMIT = 2.5;
    private final int MIN_ROW_VALUE = -1;
    private final int MAX_ROW_VALUE = 1;
    private final int ROWS_COUNT = 100;
    private final double ROW_SIZE = (MAX_ROW_VALUE - MIN_ROW_VALUE) / (double)ROWS_COUNT;

    public boolean isSilence(double coefficient) {
        return coefficient < LIMIT;
    }

    public double getEntropy(SoundFrame soundFrame) {

        double result = 0;
        double[] probabilities = new double[ROWS_COUNT];

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = 0;
        }

        double[] soundFrameData = soundFrame.getNormalizedFrameData();
        for (double value : soundFrameData) {
            int index;
            index = (int) Math.round(
                    Math.abs(value - MIN_ROW_VALUE) / ROW_SIZE
            );

            if (index > ROWS_COUNT) {
                index = ROWS_COUNT - 1;
            }

            probabilities[index]++;
        }

        int size = soundFrame.getDataLength();
        for (int i = 0; i < ROWS_COUNT; i++) {
            probabilities[i] /= size;
        }

        for (double temp : probabilities) {
            if (temp > 0) {
                result += temp * log(temp, 2);
            }
        }

        result = -result;

        return result;
    }

    private double log(double value, int base) {
        double result = 0;
        result = Math.log(value) / Math.log(base);
        return result;
    }

}
