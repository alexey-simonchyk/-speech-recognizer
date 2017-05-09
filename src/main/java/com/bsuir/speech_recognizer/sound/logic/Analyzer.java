package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.SoundRecorder;
import com.bsuir.speech_recognizer.sound.Speech;
import com.bsuir.speech_recognizer.sound.Word;

import java.util.ArrayList;

import static com.bsuir.speech_recognizer.settings.Settings.*;

public class Analyzer {

    private final static int MIN_WORD_LENGTH_MS = 70;
    private final static int MIN_BYTES_BETWEEN_WORDS = (BYTES_IN_ONE_SECOND / 1000) * MIN_WORD_LENGTH_MS;
    private final static int MIN_BYTES_WORD = (BYTES_IN_ONE_SECOND / 1000) * 70;

    public static void analyzeWords(Speech speech) {
        ArrayList<Word> words = speech.getWords();
        combineWords(words);
        deleteNoise(words);
    }

    private static void combineWords(ArrayList<Word> words) {
        int i = 0;
        while (i < words.size() - 1) {
            Word currentWord = words.get(i);
            Word nextWord = words.get(i + 1);
            if (currentWord.getDistanceToWord(nextWord) <= MIN_BYTES_BETWEEN_WORDS) {
                currentWord.combine(nextWord);
                words.remove(nextWord);
            } else {
                i++;
            }
        }
    }

    private static void deleteNoise(ArrayList<Word> words) {
        int i = 0;
        while (i < words.size()) {
            Word word = words.get(i);
            if (word.getWordLength() <= MIN_BYTES_WORD) {
                words.remove(i);
            } else {
                i++;
            }
        }
    }
}
