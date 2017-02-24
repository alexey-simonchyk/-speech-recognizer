package com.bsuir.speech_recognizer.sound;

public class SoundFrame {
    public static int FRAME_SIZE = 882;
    public static int FRAME_SHIFT = 441;

    private byte[] frameData = null;
    private double[] normalizedFrameData = null;
    private int startPosition;

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getStartPosition() { return startPosition; }

    public SoundFrame(byte[] frameData) {
        this.frameData = frameData;
    }

    public void setFrameData(byte[] frameData) {
        this.frameData = frameData;
    }

    public byte[] getFrameData() {
        return frameData;
    }

    public double[] getNormalizedFrameData() {
        return normalizedFrameData;
    }

    public void setNormalizedFrameData(double[] normalizedFrameData) {
        this.normalizedFrameData = normalizedFrameData;
    }

    public int getDataLength() { return frameData.length; }
}
