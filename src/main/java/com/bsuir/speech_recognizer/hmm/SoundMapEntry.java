package com.bsuir.speech_recognizer.hmm;

import com.bsuir.speech_recognizer.mfcc.MfccValue;

import java.util.ArrayList;

public class SoundMapEntry {
    private ArrayList<MfccValue> values = new ArrayList<>(0);

    public ArrayList<MfccValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<MfccValue> values) {
        this.values = values;
    }
}
