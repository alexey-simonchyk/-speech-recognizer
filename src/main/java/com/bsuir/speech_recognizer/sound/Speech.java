package com.bsuir.speech_recognizer.sound;

import java.util.ArrayList;

public class Speech {
    private byte[] data;
    private ArrayList<SoundFrame> soundFrames = null;
    private ArrayList<Word> words = null;

    public Speech(byte[] data) {
        this.data = data;
    }

    public void setSoundFrames(ArrayList<SoundFrame> soundFrames) {
        this.soundFrames = soundFrames;
    }

    public ArrayList<SoundFrame> getSoundFrames() { return this.soundFrames; }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
