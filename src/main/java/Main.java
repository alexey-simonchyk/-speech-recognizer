import com.bsuir.speech_recognizer.graphis.ApplicationWindow;
import com.bsuir.speech_recognizer.hmm.SoundMap;
import com.bsuir.speech_recognizer.math.Entropy;
import com.bsuir.speech_recognizer.math.Mfcc;
import com.bsuir.speech_recognizer.math.Normalizer;
import com.bsuir.speech_recognizer.mfcc.MfccValue;
import com.bsuir.speech_recognizer.settings.Settings;
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
import java.util.Arrays;
import java.util.Scanner;

public class Main {

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
//                ApplicationWindow.draw(entropyValue);

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

            getMelByFrames(words, soundFrames);
//            getMelByWord(words);


        } catch (IOException e) {
            e.printStackTrace();
        }

        printHelpData(speech.getWords(), args);

    }

    private static void printHelpData(ArrayList<Word> words, String ...args) {

        ApplicationWindow applicationWindow = new ApplicationWindow();
        applicationWindow.initialize(words, args);
    }

    private static void getMelByWord(ArrayList<Word> words) {
        for (Word word : words) {
            System.out.println(Arrays.toString(Mfcc.transform(word)));
        }
    }

    private static void getMelByFrames(ArrayList<Word> words, ArrayList<SoundFrame> soundFrames) {
        SoundMap soundMap = new SoundMap();

        int counter = 0;
        for (Word word : words) {

            double[] temp = new double[Settings.MFCC_SIZE];
            for (int j = 0; j < Settings.MFCC_SIZE; j++) {
                temp[j] = 0.0;
            }

            System.out.println("\nword");
            System.out.println("Start frame = " + word.getStartFrame() + " position " + word.getStartPosition());
            System.out.println("End frame = " + word.getEndFrame() + " position " + word.getEndPosition());

            for (int i = word.getStartFrame(); i <= word.getEndFrame(); i++) {
                SoundFrame soundFrame = soundFrames.get(i);


                counter++;

                soundFrame.setMfccValue(Mfcc.transform(soundFrame));

                MfccValue mfccValue = soundFrame.getMfccValue();


//                System.out.println(soundMap.getValue(mfccValue));

                System.out.println(Arrays.toString(mfccValue.getValue()));


                for (int k = 0; k < Settings.MFCC_SIZE; k++) {
                    temp[k] += mfccValue.getValue()[k];
                }
            }


            for (int k = 0; k < Settings.MFCC_SIZE; k++) {
                temp[k] /= counter;
            }
            System.out.println("Res = " + Arrays.toString(temp));
        }
    }

}
