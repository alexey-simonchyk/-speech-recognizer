package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.Word;

import java.util.ArrayList;

public class Analyzer {

    private final static int MIN_WORD_FRAMES = 32;
    private final static int MIN_FRAMES_BETWEEN_WORDS = MIN_WORD_FRAMES / 2;

    public void analyzeWords(ArrayList<Word> words) {
        int i = 1;
        while (i < words.size()) {
            Word word = words.get(i);

            if (word.getFramesBefore() < MIN_FRAMES_BETWEEN_WORDS) {
                words.get(i - 1).combine(word);
                words.remove(i);
            } else {
                i++;
            }

        }

        i = 0;

        while (i < words.size()) {
            Word word = words.get(i);

            if (word.getSoundFrames().size() < MIN_WORD_FRAMES) {
                words.remove(i);
            } else {
                i++;
            }
        }
    }

}
