package com.bsuir.speech_recognizer.hmm;

import com.bsuir.speech_recognizer.math.MathCommon;
import com.bsuir.speech_recognizer.mfcc.MfccValue;
import com.sun.scenario.effect.SepiaTone;

import java.io.*;
import java.util.*;

public class SoundMap {

    private static final double MFCC_WEIGHTS[] = {1.5, 1, 1, 1, 0.9, 0.9, 0.9, 0.8, 0.8, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.2, 0.1, 0.1, 0.1};

    private HashMap<String, SoundMapEntry> values = new HashMap<>(0);

    public SoundMap() {
    }

    public String[] getWords() {
        Set<String> keys = values.keySet();
        String[] result = new String[keys.size()];
        int counter = 0;
        for (String key : keys) {
            result[counter++] = key;
        }
        return result;
    }

    public void removeAllWords() {
        values.clear();
    }

    public void removeWord(String word) {
        if (values.get(word) != null) {
            values.remove(word);
        }
    }

    public ArrayList<String> getWordsValues() {
        ArrayList<String> result = new ArrayList<>(0);

        Set<String> keys = values.keySet();
        for (String key :  keys) {
            String res = "\n" + key;
            SoundMapEntry soundMapEntry = values.get(key);
            ArrayList<MfccValue> mfccValues = soundMapEntry.getValues();
            for (MfccValue mfccValue : mfccValues) {
                res += "\n" + Arrays.toString(mfccValue.getValue());
            }
            result.add(res);
        }

        return result;
    }

    public String getValue(MfccValue mfccValue) {
        double minDistance = Double.MAX_VALUE;
        String soundValue = null;

        for (Map.Entry<String, SoundMapEntry> entry : values.entrySet()) {
            SoundMapEntry soundMapEntry = entry.getValue();
            for (MfccValue value : soundMapEntry.getValues()) {

                double distance;
                distance = MathCommon.euclidianDistanceWithWeight(value.getValue(), mfccValue.getValue(), MFCC_WEIGHTS);

                if (distance < minDistance) {
                    soundValue = entry.getKey();
                    minDistance = distance;
                }
            }
        }

        return soundValue;
    }

    public void addWord(String value, ArrayList<MfccValue> keys) {
        SoundMapEntry entry = values.get(value);
        if (entry == null) {
            entry = new SoundMapEntry();
            entry.setValues(keys);
            values.put(value, entry);
        } else {
            entry.getValues().addAll(keys);
        }
    }

    public void serialize() {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream("words.sr");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserialize() {
        try (
                FileInputStream fileInputStream = new FileInputStream("words.sr");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            values = (HashMap<String, SoundMapEntry>)objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
