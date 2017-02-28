package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.Speech;
import com.bsuir.speech_recognizer.sound.Word;

import java.util.ArrayList;

public class Splitter {

    public void splitSoundOnFrames(Speech speech) {
        ArrayList<SoundFrame> result = new ArrayList<SoundFrame>();
        byte[] data = speech.getData();
        int currentPosition = 0;
        boolean check = true;

        while (check) {
            SoundFrame soundFrame = new SoundFrame(data);
            int endPosition = currentPosition + SoundFrame.FRAME_SIZE;

            if (endPosition > data.length) {
                endPosition = data.length;
                check = false;
            }

            soundFrame.setStartPosition(currentPosition);
            soundFrame.setEndPosition(endPosition);

            currentPosition += SoundFrame.FRAME_SHIFT;
            result.add(soundFrame);
        }

        speech.setSoundFrames(result);
    }

    public void splitIntoWords(Speech speech) {
        ArrayList<SoundFrame> soundFrames = speech.getSoundFrames();
        ArrayList<Word> words = new ArrayList<Word>();

        if (soundFrames != null) {
            Word word = new Word();
            boolean isSilence = true;
            int counter = 0;

            for (SoundFrame soundFrame : soundFrames) {

                if (isSilence != soundFrame.isSilence()) {
                    if (soundFrame.isSilence()) {
                        words.add(word);
                        word = new Word();
                        isSilence = true;
                        counter = 1;
                    } else {
                        word.setFramesBefore(counter);
                        isSilence = false;
                        word.getSoundFrames().add(soundFrame);
                    }
                } else {
                    if (!isSilence) {
                        word.getSoundFrames().add(soundFrame);
                    } else {
                        counter++;
                    }
                }
            }
        }

        speech.setWords(words);

    }

}
