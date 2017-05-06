package com.bsuir.speech_recognizer.settings;

public class Settings {
    public static boolean USE_FFT = true;
    public static boolean USE_WINDOW_FUNCTION = true;

    // 882 - 10 ms
    // 1024 ~ 11.6 ms
    public static int FRAME_SIZE;
    public static int FRAME_SHIFT;

    static {
        if (USE_FFT) {
            FRAME_SIZE = 4096; // 2048
        } else {
            FRAME_SIZE = 2048;
        }
        FRAME_SHIFT = FRAME_SIZE / 2;
    }

    public final static int NUMBER_CHANNELS = 1;
    public final static float SAMPLE_RATE = 44100;
    public final static int SAMPLE_SIZE_IN_BITS = 16;
    public final static int BYTES_IN_FRAME = 2; // frame size in bytes
    public final static float FRAME_RATE = 44100; // number frames per second

    public final static int BYTES_IN_ONE_SECOND = (int)(FRAME_RATE * BYTES_IN_FRAME);

    public static final int MFCC_FREQ_MIN = 300;
    public static final int MFCC_FREQ_MAX = 8000;
    public static final int MFCC_SIZE = 12;
    public static final int MFCC_FREQ = 44100;

}
