package com.bsuir.speech_recognizer.settings;

public class Settings {
    public static boolean USE_FFT = true;
    public static boolean USE_WINDOW_FUNCTION = true;

    // 882 - 10 ms
    // 1024 ~ 11.6 ms
    public static int FRAME_SIZE;
    public static int FRAME_SHIFT;

    static {
        FRAME_SIZE = 1024;
        FRAME_SHIFT = FRAME_SIZE / 2;
    }

    public final static int NUMBER_CHANNELS = 1;
    public final static float SAMPLE_RATE = 8000;
    public final static int SAMPLE_SIZE_IN_BITS = 16;
    public final static int BYTES_IN_FRAME = 2; // frame size in bytes
    public final static float FRAME_RATE = 8000; // number frames per second

    public final static int BYTES_IN_ONE_SECOND = (int)(FRAME_RATE * BYTES_IN_FRAME);

    public static final int MFCC_FREQ_MIN = 0;
    public static final int MFCC_FREQ_MAX = 4000;
    public static final int MFCC_SIZE = 40;
    public static final int MFCC_USE = 20;
    public static final int MFCC_FREQ = 8000;

}
