import com.bsuir.speech_recognizer.sound.logic.AudioSplitter;
import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.SoundRecorder;
import com.bsuir.speech_recognizer.sound.Speech;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String ...args) {
        SoundRecorder recorder = new SoundRecorder(true);
        recorder.startRecording();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.next().equals("end")){}

        recorder.stopRecording();

        AudioSplitter audioSplitter = new AudioSplitter();
        Speech speech = new Speech(recorder.getBytes());

        ArrayList<SoundFrame> soundFrames;
        soundFrames = audioSplitter.splitSoundOnFrames(speech);
        speech.setSoundFrames(soundFrames);

        audioSplitter.splitSoundOnFrames(speech);

    }

}
