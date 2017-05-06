package com.bsuir.speech_recognizer.math;

import com.bsuir.speech_recognizer.mfcc.MfccValue;
import com.bsuir.speech_recognizer.settings.Settings;
import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.Word;

import static com.bsuir.speech_recognizer.settings.Settings.*;

public class Mfcc {
    public static MfccValue transform(SoundFrame soundFrame) {
        int sampleLength = soundFrame.getNormalizedFrameData().length;
        double[] fourierRaw;
        if (Settings.USE_FFT) {
            sampleLength = (int)Math.pow(2, (int)MathCommon.log(sampleLength, 2));
            fourierRaw = fourierTransformFast(soundFrame.getNormalizedFrameData(), sampleLength, USE_WINDOW_FUNCTION);
        } else {
            fourierRaw = fourierTransform(soundFrame.getNormalizedFrameData(), sampleLength, USE_WINDOW_FUNCTION);
        }
        soundFrame.fourier =fourierRaw;


        double[][] melFilters = getMelFilters(sampleLength);
        double[] logPower = calculatePower(fourierRaw, sampleLength, melFilters);
        double[] dctRaw = dctTransform(logPower);

        return new MfccValue(dctRaw);
    }

    private static double[] dctTransform(double[] logPower) {
        double[] dctTransform = new double[logPower.length];

        for (int i = 0; i < logPower.length; i++) {
            dctTransform[i] = 0;

            for (int k = 0; k < logPower.length; k++) {
                dctTransform[i] += logPower[k] * Math.cos(Math.PI * (i + 1) * (k + 1.5) / logPower.length);
            }
        }

        return dctTransform;
    }


    private static double[] calculatePower(double[] fourierRaw, int length, double[][] melFilters) {
        double[] logPower = new double[MFCC_SIZE];

        for (int i = 0; i < MFCC_SIZE; i++) {
            logPower[i] = 0;

            for (int k = 0; k < length; k++) {
                logPower[i] += melFilters[i][k] * Math.pow(fourierRaw[k], 2);
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

        for (int m = 1; m < MFCC_SIZE + 1; m++) {
            fb[m] = fb[0] + m * (fb[MFCC_SIZE + 1] - fb[0]) / (MFCC_SIZE + 1);
        }

        for (int m = 0; m < MFCC_SIZE + 2; m++) {

            fb[m] = convertFromMel(fb[m]);

            fb[m] = (int)((filterLength + 1) * fb[m] / (double) MFCC_FREQ);

        }

        double[][] filterBanks = new double[MFCC_SIZE][filterLength];


        for (int m = 1; m < MFCC_SIZE + 1; m++) {
            for (int k = 0; k < filterLength; k++) {
                if (fb[m - 1] <= k && k <= fb[m]) {
                    filterBanks[m - 1][k] = (k - fb[m - 1]) / (fb[m] - fb[m - 1]);

                } else if (fb[m] < k && k <= fb[m + 1]) {
                    filterBanks[m - 1][k] = (fb[m + 1] - k) / (fb[m + 1] - fb[m]);

                } else {
                    filterBanks[m - 1][k] = 0;
                }
            }
        }


        return filterBanks;
    }



    private static double[] fourierTransform(double[] data, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];

        Complex[] fourierTempRaw = new Complex[length];

        double window;
        window = 0.54 - 0.46 * Math.cos(2 * Math.PI * length / (length - 1));


        for(int i = 0; i < length; i++)
        {
            fourierTempRaw[i] = new Complex();
            for(int j = 0; j < length; j++)
            {
                if (useWindow) {
                    fourierTempRaw[i].multiply(window);
                }

                double temp = (2 * j * i * Math.PI) / length;
                fourierTempRaw[i].real += data[j] * Math.cos(temp);
                fourierTempRaw[i].img += data[j] * Math.sin(temp);


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


    private static double[] fourierTransformFast(double[] data, int length, boolean useWindow) {
        double[] fourierRaw = new double[length];
        Complex[] fourierRawTemp = new Complex[length];

        for (int i = 0; i < length; i++) {

            fourierRawTemp[i] = new Complex(data[i]);

            if (useWindow) {
                fourierRawTemp[i].multiply(0.54 - 0.46 * Math.cos(2 * Math.PI * i / (length - 1)));
            }
        }

        fourierTransformFastRecursion(fourierRawTemp);



        for (int i = 0; i < length; i++) {
            fourierRaw[i] = fourierRawTemp[i].getNormal();
        }

        return fourierRaw;
    }

    private static void fourierTransformFastRecursion(Complex[] data) {
        int length = data.length;

        if (length <= 1) {
            return;
        }

        int temp = data.length / 2;
        Complex[] even = new Complex[temp];
        Complex[] odd = new Complex[temp + data.length % 2];

        int evenCounter = 0;
        int oddCounter = 0;

        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                odd[oddCounter++] = data[i];
            } else {
                even[evenCounter++] = data[i];
            }
        }

        fourierTransformFastRecursion(even);
        fourierTransformFastRecursion(odd);

        for (int i = 0; i < length / 2; i++) {
            Complex tempComplex = new Complex();
            tempComplex.real = Math.cos(-2 * Math.PI * i / length);
            tempComplex.img = Math.sin(-2 * Math.PI * i / length);
            tempComplex.multiply(odd[i]);

            data[i] = even[i].plus(tempComplex);
            data[i + length / 2] = even[i].minus(tempComplex);

        }
    }





    public static double[] transform(Word word) {
        int sampleLength = 0;
        for (int i = word.getStartFrame(); i <= word.getEndFrame(); i++) {
            sampleLength += word.getFrames().get(i).getNormalizedFrameData().length;
        }

        double[] normalizedData = new double[sampleLength];
        int position = 0;
        for (int i = word.getStartFrame(); i <= word.getEndFrame(); i++) {
            SoundFrame soundFrame = word.getFrames().get(i);
            int length = soundFrame.getNormalizedFrameData().length;
            System.arraycopy(soundFrame.getNormalizedFrameData(), 0, normalizedData, position, length);
            position += length;
        }

        double[] fourierRaw;
        if (Settings.USE_FFT) {
            sampleLength = (int)Math.pow(2, (int)MathCommon.log(sampleLength, 2));
            fourierRaw = fourierTransformFast(normalizedData, sampleLength, USE_WINDOW_FUNCTION);
        } else {
            fourierRaw = fourierTransform(normalizedData, sampleLength, USE_WINDOW_FUNCTION);
        }



        double[][] melFilters = getMelFilters(sampleLength);
        double[] logPower = calculatePower(fourierRaw, sampleLength, melFilters);
        double[] dctRaw = dctTransform(logPower);

        return dctRaw;
    }




}