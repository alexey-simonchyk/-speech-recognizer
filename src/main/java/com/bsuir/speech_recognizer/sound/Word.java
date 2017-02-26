package com.bsuir.speech_recognizer.sound;

import java.util.ArrayList;

public class Word {
    private ArrayList<SoundFrame> soundFrames = new ArrayList<SoundFrame>();

    public ArrayList<SoundFrame> getSoundFrames() {
        return soundFrames;
    }

    public void setSoundFrames(ArrayList<SoundFrame> soundFrames) {
        this.soundFrames = soundFrames;
    }
}
