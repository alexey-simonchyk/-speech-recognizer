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
                dctTransform[i] += logPower[k] * Math.cos(Math.PI * i * (k + 0.5) / logPower.length);
            }
        }

        return dctTransform;
    }


    private static double[] calculatePower(double[] fourierRaw, int length, double[][] melFilters) {
        double[] logPower = new double[(int) MFCC_SIZE];

        for (int i = 0; i < MFCC_SIZE; i++) {
            logPower[i] = 0;

            for (int k = 0; k < length; k++) {
                logPower[i] += melFilters[i][k] * Math.pow(fourierRaw[k], 2);
            }

            // TODO: 4/30/17 May be need remove in future
            logPower[i] = Math.log10(logPower[i]);
        }

        return logPower;
    }


    private static double[][] getMelFilters(int filterLength) {
        double[] fb = new double[(int) (MFCC_SIZE)];
        double max = convertToMel(MFCC_FREQ_MAX);
        double min = convertToMel(MFCC_FREQ_MIN);


        for (int i = 0; i < MFCC_SIZE; i++) {
            fb[i] = min + i * (max - min) / (MFCC_SIZE + 1);
        }

        for (int i = 0; i < MFCC_SIZE; i++) {
            fb[i] = convertFromMel(fb[i]);

            fb[i] = filterLength * fb[i] / MFCC_FREQ;
        }

        double[][] filterBanks = new double[(int) MFCC_SIZE][filterLength];

        // TODO : make sure that it is right

        for (int i = 0; i < MFCC_SIZE; i++) {
            for (int k = 0; k < filterLength; k++) {
                if (i == 0) {
                    filterBanks[i][k] = mel(fb[i], min, fb[i + 1], k);
                } else if (i == MFCC_SIZE - 1) {
                    filterBanks[i][k] = mel(fb[i], fb[i -1], max, k);
                } else {
                    filterBanks[i][k] = mel(fb[i], fb[i - 1], fb[i + 1], k);
                }
            }

        }

        return filterBanks;
    }

    private static double mel(double current, double min, double max, int k) {
        double result = 0;
        if (min <= k && k < current) {
            result = (k - min) / (current - min);
        } else if (current < k && k <= max) {
            result = (max - k) / (max - current);
        } else if (k == current) {
            result = 1;
        }
        return result;
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