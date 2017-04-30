package com.bsuir.speech_recognizer.math;

import com.bsuir.speech_recognizer.sound.SoundFrame;

public class Mfcc {

    private static final int MFCC_FREQ_MIN = 300;
    private static final int MFCC_FREQ_MAX = 4000;
    private static final int MFCC_SIZE = 12;
    private static final int MFCC_FREQ = 44100;

    public static double[] transform(SoundFrame soundFrame) {
        int sampleLength = soundFrame.getNormalizedFrameData().length;

        double[] fourierRaw = fourierTransform(soundFrame, sampleLength, true);

        double[][] melFilters = getMelFilters(sampleLength);
        double[] logPower = calculatePower(fourierRaw, sampleLength, melFilters);
        double[] dctRaw = dctTransform(logPower);

        return dctRaw;
    }

    public static double[] calculatePower(double[] fourierRaw, int length, double[][] melFilters) {
        return null;
    }

    public static double[] dctTransform(double[] logPower) {
        return null;
    }

    public static double[] fourierTransform(SoundFrame soundFrame, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];

        double[] data = soundFrame.getNormalizedFrameData();

        Complex[] fourierTempRaw = new Complex[length];

        System.out.println("The coefficients are: ");
        for(int i = 0; i < length; i++)
        {
            fourierTempRaw[i] = new Complex();
            for(int j = 0; j < length; j++)
            {
                double temp = (2 * j * i * Math.PI) / length;
                fourierTempRaw[i].real += data[j] * Math.cos(temp);
                fourierTempRaw[i].img += data[j] * Math.sin(temp);

                double window = 1;
                if (useWindow) {
                    window = 0.54 - 0.46 * Math.cos(2 * Math.PI * length / (length - 1));
                }

                fourierTempRaw[i].multiply(window);
            }

            fourierRaw[i] = fourierTempRaw[i].getNormal();

        }

        return fourierRaw;
    }



    public static double filter(SoundFrame soundFrame) {
        return 0;
    }

    public static double[][] getMelFilters(int filterLength) {
        return null;
    }


    public static double[] fourierTransformFast(SoundFrame soundFrame, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];
        double[] fourierRawTemp = new double[length];

        return null;
    }

}
