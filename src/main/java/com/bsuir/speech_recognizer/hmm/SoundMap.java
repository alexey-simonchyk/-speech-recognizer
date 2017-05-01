package com.bsuir.speech_recognizer.hmm;

import com.bsuir.speech_recognizer.math.MathCommon;
import com.bsuir.speech_recognizer.mfcc.MfccValue;

import java.util.HashMap;
import java.util.Map;

public class SoundMap {

    private static final double MFCC_WEIGHTS[] = {1.2, 1.1, 1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};

    private HashMap<String, SoundMapEntry> values = new HashMap<>(0);

    public HashMap<String, SoundMapEntry> getValues() {
        return values;
    }

    public void setValues(HashMap<String, SoundMapEntry> values) {
        this.values = values;
    }

    public String getValue(MfccValue mfccValue) {
        double minDistance = Double.MAX_VALUE;
        String soundValue = null;

        for (Map.Entry<String, SoundMapEntry> entry : values.entrySet()) {
            SoundMapEntry soundMapEntry = entry.getValue();
            for (MfccValue value : soundMapEntry.getValues()) {

                double distance;
                distance = MathCommon.euclidianDistanceWithWeight(value.getValue(), mfccValue.getValue(),MFCC_WEIGHTS);

                if (distance < minDistance) {
                    soundValue = entry.getKey();
                    minDistance = distance;
                }
            }
        }

        return soundValue;
    }
}
