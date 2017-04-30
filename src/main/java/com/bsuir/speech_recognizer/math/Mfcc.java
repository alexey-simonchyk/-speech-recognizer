package com.bsuir.speech_recognizer.math;

import com.bsuir.speech_recognizer.settings.Settings;
import com.bsuir.speech_recognizer.sound.SoundFrame;
import static com.bsuir.speech_recognizer.settings.Settings.*;

public class Mfcc {



    public static double[] transform(SoundFrame soundFrame) {
        int sampleLength = soundFrame.getNormalizedFrameData().length;
        double[] fourierRaw;
        if (Settings.USE_FFT) {
            sampleLength = (int)Entropy.log(sampleLength, 2);
            fourierRaw = fourierTransformFast(soundFrame, sampleLength, true);
        } else {
            fourierRaw = fourierTransform(soundFrame, sampleLength, true);
        }



        double[][] melFilters = getMelFilters(sampleLength);
        double[] logPower = calculatePower(fourierRaw, sampleLength, melFilters);
        double[] dctRaw = dctTransform(logPower);

        return dctRaw;
    }

    private static double[] dctTransform(double[] logPower) {
        double[] dctTransform = new double[logPower.length];

        for (int i = 0; i < logPower.length; i++) {
            dctTransform[i] = 0;

            for (int k = 0; k < logPower.length; k++) {
                dctTransform[i] += logPower[k] * Math.cos(Math.PI * i * (k + 0.5) / logPower.length);
            }
        }

        return dctTransform;
    }


    private static double[] calculatePower(double[] fourierRaw, int length, double[][] melFilters) {
        double[] logPower = new double[MFCC_SIZE];

        for (int i = 0; i < MFCC_SIZE; i++) {
            logPower[i] = 0;

            for (int k = 0; k < length; k++) {
                logPower[i] = melFilters[i][k] * Math.pow(fourierRaw[k], 2);
            }

            // TODO: 4/30/17 May be need remove in future
            logPower[i] = Math.log(logPower[i]);
        }

        return logPower;
    }


    private static double[][] getMelFilters(int filterLength) {
        double[] fb = new double[MFCC_SIZE + 2];
        fb[0] = convertToMel(MFCC_FREQ_MIN);
        fb[MFCC_SIZE + 1] = convertToMel(MFCC_FREQ_MAX);

        for (int i = 0; i < MFCC_SIZE + 1; i++) {
            fb[i] = fb[0] + i * (fb[MFCC_SIZE + 1] - fb[0]) / (MFCC_SIZE + 1);
        }

        for (int i = 0; i < MFCC_SIZE + 1; i++) {
            fb[i] = convertFromMel(fb[i]);

            fb[i] = (int) ((filterLength + 1) * fb[i] / MFCC_FREQ);
        }

        double[][] filterBanks = new double[MFCC_SIZE][filterLength];

        for (int i = 1; i < MFCC_SIZE + 1; i++) {
            for (int k = 0; k < MFCC_SIZE; k++) {

                if (fb[i - 1] <= k && k <= fb[i]) {
                    filterBanks[i - 1][k] = (k - fb[i - 1]) / (fb[i] - fb[i - 1]);

                } else if (fb[i] < k && k <= fb[i + 1]) {
                    filterBanks[i - 1][k] = (fb[i + 1] - k) / (fb[i + 1] - fb[i]);

                } else {
                    filterBanks[i - 1][k] = 0;
                }
            }

        }

        return filterBanks;
    }



    private static double[] fourierTransform(SoundFrame soundFrame, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];

        double[] data = soundFrame.getNormalizedFrameData();

        Complex[] fourierTempRaw = new Complex[length];

        double window;
        window = 0.54 - 0.46 * Math.cos(2 * Math.PI * length / (length - 1));


        for(int i = 0; i < length; i++)
        {
            fourierTempRaw[i] = new Complex();
            for(int j = 0; j < length; j++)
            {
                double temp = (2 * j * i * Math.PI) / length;
                fourierTempRaw[i].real += data[j] * Math.cos(temp);
                fourierTempRaw[i].img += data[j] * Math.sin(temp);

                if (useWindow) {
                    fourierTempRaw[i].multiply(window);
                }

            }

            fourierRaw[i] = fourierTempRaw[i].getNormal();

        }

        return fourierRaw;
    }

    private static double convertToMel(double frequency) {
        double melCoefficient = Math.log(1 + frequency / 700);
        melCoefficient *= 1125;
        return melCoefficient;
    }

    private static double convertFromMel(double melCoefficient) {
        double frequency = Math.exp(melCoefficient / 1125 ) - 1;
        frequency *= 700;
        return frequency;
    }

    private static double[] fourierTransformFast(SoundFrame soundFrame, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];
        Complex[] fourierRawTemp = new Complex[length];



        return null;
    }

}
