package com.bsuir.speech_recognizer.sound;

import java.util.ArrayList;

public class Word {
    private int startPosition;
    private int endPosition;
    public double[] result;

    private int startFrame;
    private int endFrame;
    private ArrayList<SoundFrame> frames;

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public ArrayList<SoundFrame> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<SoundFrame> frames) {
        this.frames = frames;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public void combine(Word word) {
        endPosition = word.endPosition;
        endFrame = word.endFrame;
    }

    public int getDistanceToWord(Word word) {
        return word.startPosition - endPosition;
    }

    public int getWordLength() {
        return endPosition - startPosition;
    }
}
