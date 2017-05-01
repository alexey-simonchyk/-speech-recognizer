package com.bsuir.speech_recognizer.sound;

import com.bsuir.speech_recognizer.mfcc.MfccValue;

public class SoundFrame {

    private byte[] frameData = null;
    private double[] normalizedFrameData = null;

    private MfccValue mfccValue;
    private int startPosition;
    private int endPosition;

    private boolean isSilence;
    private double entropyValue;


    public double getEntropyValue() {
        return this.entropyValue;
    }

    public void setEntropyValue(double entropyValue) {
        this.entropyValue = entropyValue;
    }

    public boolean isSilence() {
        return this.isSilence;
    }

    public void setSilence(boolean isSilence) {
        this.isSilence = isSilence;
    }

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

    public int getDataLength() { return endPosition - startPosition; }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public MfccValue getMfccValue() {
        return mfccValue;
    }

    public void setMfccValue(MfccValue mfccValue) {
        this.mfccValue = mfccValue;
    }
}
