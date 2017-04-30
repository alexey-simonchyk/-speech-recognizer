import com.bsuir.speech_recognizer.graphis.ApplicationWindow;
import com.bsuir.speech_recognizer.math.Entropy;
import com.bsuir.speech_recognizer.math.Mfcc;
import com.bsuir.speech_recognizer.math.Normalizer;
import com.bsuir.speech_recognizer.sound.Word;
import com.bsuir.speech_recognizer.sound.logic.Analyzer;
import com.bsuir.speech_recognizer.sound.logic.Splitter;
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

        Splitter splitter = new Splitter();
        Speech speech = new Speech(recorder.getBytes());

        Entropy entropy = new Entropy();
        Normalizer normalizer = new Normalizer();
        splitter.splitSoundOnFrames(speech);
        ArrayList<SoundFrame> soundFrames = speech.getSoundFrames();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results.txt"))) {

            double entropyValue;

            for (SoundFrame soundFrame : soundFrames) {

                double[] normalizedData = normalizer.normalize(soundFrame.getFrameData(),
                                                                soundFrame.getStartPosition(),
                                                                soundFrame.getEndPosition());
                soundFrame.setNormalizedFrameData(normalizedData);


                entropyValue = entropy.getEntropy(soundFrame);
                ApplicationWindow.draw(entropyValue);

                bufferedWriter.write(entropyValue + "\n");

                boolean isSilence;
                isSilence = entropy.isSilence(entropyValue);
                soundFrame.setSilence(isSilence);
                soundFrame.setEntropyValue(entropyValue);

            }

            splitter.splitIntoWords(speech);

            Analyzer analyzer = new Analyzer();
            analyzer.analyzeWords(speech);

            System.out.println(speech.getWords().size());

            ArrayList<Word> words = speech.getWords();
            for (Word word : words) {
                System.out.println("\nword");
                System.out.println("Start frame = " + word.getStartFrame() + " position " + word.getStartPosition());
                System.out.println("End frame = " + word.getEndFrame() + " position " + word.getEndPosition());
            }

            // getting mel coefficients
            /*ArrayList<Word> words = speech.getWords();
            for (Word word : words) {
                for (int i = word.getStartFrame(); i <= word.getEndFrame(); i++) {

                    System.out.println(i);

                    SoundFrame soundFrame = soundFrames.get(i);
                    soundFrame.setMfcc(Mfcc.transform(soundFrame));
                }
            }*/


        } catch (IOException e) {
            e.printStackTrace();
        }

        ApplicationWindow.SIZE = soundFrames.size();
        ApplicationWindow applicationWindow = new ApplicationWindow();
        applicationWindow.initialize(args);

    }

}
