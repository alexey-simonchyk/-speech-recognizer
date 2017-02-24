package com.bsuir.speech_recognizer.sound.logic;

import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.Speech;

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

}
