package com.bsuir.speech_recognizer.sound;

import java.util.ArrayList;

public class Word {
    private ArrayList<SoundFrame> soundFrames = new ArrayList<SoundFrame>();
    private int framesBefore;

    public ArrayList<SoundFrame> getSoundFrames() {
        return soundFrames;
    }

    public void setSoundFrames(ArrayList<SoundFrame> soundFrames) {
        this.soundFrames = soundFrames;
    }

    public int getFramesBefore() {
        return framesBefore;
    }

    public void setFramesBefore(int framesBefore) {
        this.framesBefore = framesBefore;
    }

    public void combine(Word word) {
        this.soundFrames.addAll(word.soundFrames);
    }
}
