import com.bsuir.speech_recognizer.graphis.ApplicationWindow;
import com.bsuir.speech_recognizer.math.Entropy;
import com.bsuir.speech_recognizer.math.Normalizer;
import com.bsuir.speech_recognizer.sound.logic.AudioSplitter;
import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.SoundRecorder;
import com.bsuir.speech_recognizer.sound.Speech;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{

    public static void main(String ...args) {
        testMethod(args);
    }


    private static void testMethod(String ...args) {

        SoundRecorder recorder = new SoundRecorder(true);

        /*recorder.startRecording();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.next().equals("`")){}

        recorder.stopRecording();*/

        AudioSplitter audioSplitter = new AudioSplitter();
        Speech speech = new Speech(recorder.getBytes());

        ArrayList<SoundFrame> soundFrames;
        soundFrames = audioSplitter.splitSoundOnFrames(speech);
        speech.setSoundFrames(soundFrames);

        Entropy entropy = new Entropy();
        Normalizer normalizer = new Normalizer();

        ApplicationWindow.SIZE = soundFrames.size();
        ApplicationWindow applicationWindow = new ApplicationWindow();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results.txt"))) {

            double entropyValue;

            for (SoundFrame soundFrame : soundFrames) {

                double[] normalizedData = normalizer.normalize(soundFrame.getFrameData());
                soundFrame.setNormalizedFrameData(normalizedData);


                entropyValue = entropy.getEntropy(soundFrame);
                ApplicationWindow.draw(entropyValue);

                bufferedWriter.write(entropyValue + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationWindow.initialize(args);

    }
}
