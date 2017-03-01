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
            Word word = null;
            boolean isSilence = true;

            for (SoundFrame soundFrame : soundFrames) {

                if (isSilence != soundFrame.isSilence()) {

                    if (isSilence) {
                        if (word != null) {
                            words.add(word);
                        }
                        word = new Word();
                        word.setStartPosition(soundFrame.getStartPosition());
                    }
                    isSilence = soundFrame.isSilence();
                }

                if (!isSilence) {
                    word.setEndPosition(soundFrame.getEndPosition());
                }
            }
        }

        speech.setWords(words);
    }

}
