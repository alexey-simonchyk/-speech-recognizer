package com.bsuir.speech_recognizer.sound;

import java.util.ArrayList;

public class Word {
    private int startPosition;
    private int endPosition;

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
    }

    public int getDistanceToWord(Word word) {
        return word.startPosition - endPosition;
    }

    public int getWordLength() {
        return endPosition - startPosition;
    }
}
