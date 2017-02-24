package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.Speech;
import com.bsuir.speech_recognizer.sound.Word;

import java.util.ArrayList;

public class WordsSplitter {

    private Speech speech;

    public WordsSplitter() {}

    public WordsSplitter(Speech speech) {
        this.speech = speech;
    }

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(Speech speech) {
        this.speech = speech;
    }

    public ArrayList<Word> splitIntoWords() {
        ArrayList<Word> result = new ArrayList<Word>();

        return result;
    }
}
