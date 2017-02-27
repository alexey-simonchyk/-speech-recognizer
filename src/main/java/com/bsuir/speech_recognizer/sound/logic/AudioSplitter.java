package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.Speech;
import com.bsuir.speech_recognizer.sound.Word;

import java.util.ArrayList;

public class AudioSplitter {

    public ArrayList<SoundFrame> splitSoundOnFrames(Speech speech) {
        ArrayList<SoundFrame> result = new ArrayList<SoundFrame>();
        byte[] data = speech.getData();
        int currentPosition = 0;
        boolean check = true;

        while (check) {
            int temp = currentPosition + SoundFrame.FRAME_SIZE;
            int length;

            if (temp >= data.length - 1) {
                length = data.length - currentPosition;
                check = false;
            } else {
                length = SoundFrame.FRAME_SIZE;
            }

            byte[] frameData = new byte[length];
            System.arraycopy(data, currentPosition, frameData, 0, length);
            SoundFrame soundFrame = new SoundFrame(frameData);
            result.add(soundFrame);
            currentPosition += SoundFrame.FRAME_SHIFT;
        }
        return result;
    }

    public ArrayList<Word> splitFramesOnWords(ArrayList<SoundFrame> soundFrames) {
        ArrayList<Word> result = new ArrayList<Word>();
        Word word = new Word();
        boolean isSilence = true;
        int counter = 0;

        for (SoundFrame soundFrame : soundFrames) {

            if (isSilence != soundFrame.isSilence()) {
                if (soundFrame.isSilence()) {
                    result.add(word);
                    word = new Word();
                    isSilence = true;
                    counter = 1;
                } else {
                    word.setFramesBefore(counter);
//                    counter = 0;
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

        return result;
    }

}
