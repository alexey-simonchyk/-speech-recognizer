import com.bsuir.speech_recognizer.graphis.ApplicationWindow;
import com.bsuir.speech_recognizer.hmm.SoundMap;
import com.bsuir.speech_recognizer.hmm.SoundMapEntry;
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
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static volatile boolean isServerRunning = false;
    private static SoundMap soundMap = new SoundMap();
    private static SoundRecorder recorder;

    public static void main(String ...args) {
        soundMap.deserialize();
        Scanner scanner = new Scanner(System.in);
        boolean check = true;
        while (check) {
            System.out.println("\n\t1 - recognize word\n\t2 - teach new word\n\t3 - print words" +
                    "\n\t4 - remove word\n\t5 - print word values\n\t6 - run server\n\t0 - exit\n\t` - exit without save" +
                    "\n\t7 - delete all words\n\t8 - stop server\n");

            String input = scanner.next();
            switch (input) {
                case "8":
                    isServerRunning = false;
                    System.out.println("Server stopped");
                    break;
                case "7":
                    removeAllWords();
                    break;
                case "`":
                    System.exit(0);
                    break;
                case "4":
                    removeWord();
                    break;
                case "6":
                    if (!isServerRunning) {
                        startServer();
                        System.out.println("Server started");
                    } else {
                        System.out.println("Server running");
                    }
                    break;
                case "5":
                    printWordValues();
                    break;
                case "0":
                    check = false;
                    break;
                case "3":
                    printWords();
                    break;
                case "2":
                    teach();
                    break;
                case "1":
                    recognize(args);
                    break;
            }

        }
        soundMap.serialize();
    }

    private static void removeAllWords() {
        soundMap.removeAllWords();
    }

    private static void removeWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write word to delete");
        String word = scanner.next();
        soundMap.removeWord(word);
    }

    private static void printWordValues() {
        ArrayList<String> information = soundMap.getWordsValues();
        for (String word : information) {
            System.out.println(word);
        }
    }

    private static void printWords() {
        String[] words = soundMap.getWords();
        for (String word : words) {
            System.out.println(word);
        }
    }

    private static void teach(byte[] data, String wordStr) {
        if (data == null) {
            return;
        }
        Speech speech = new Speech(data);


        double[] kix = new double[speech.getData().length];
        for (int i = 0; i < speech.getData().length; i++) {
            kix[i] = speech.getData()[i];
        }

        Splitter.splitSoundOnFrames(speech);
        ArrayList<SoundFrame> soundFrames = speech.getSoundFrames();


        double entropyValue;


        for (SoundFrame soundFrame : soundFrames) {


            double[] normalizedData = Normalizer.normalize( soundFrame.getFrameData(),
                    soundFrame.getStartPosition(),
                    soundFrame.getEndPosition());
            soundFrame.setNormalizedFrameData(normalizedData);


            entropyValue = Entropy.getEntropy(soundFrame);


            boolean isSilence;
            isSilence = Entropy.isSilence(entropyValue);
            soundFrame.setSilence(isSilence);
            soundFrame.setEntropyValue(entropyValue);

        }

        Splitter.splitIntoWords(speech);

        Analyzer analyzer = new Analyzer();
        analyzer.analyzeWords(speech);


        System.out.println("Number words = " + speech.getWords().size());

        ArrayList<Word> words = speech.getWords();

        for (Word word : words) {
            Splitter.splitInLargeFrames(word);
        }

        for (Word word : words) {

            int counter = 0;
            double[] temp = new double[Settings.MFCC_USE];
            for (int j = 0; j < Settings.MFCC_USE; j++) {
                temp[j] = 0.0;
            }

            for (SoundFrame soundFrame : word.getFrames()) {

                counter++;

                double[] t = new double[soundFrame.getEndPosition() - soundFrame.getStartPosition()];
                for (int j = 0; j < soundFrame.getEndPosition() - soundFrame.getStartPosition(); j++) {
                    t[j] = kix[j + soundFrame.getStartPosition()] - 0.95 * kix[j - 1 + soundFrame.getStartPosition()];
                }
                soundFrame.setNormalizedFrameData(t);


                soundFrame.setMfccValue(Mfcc.transform(soundFrame));

                MfccValue mfccValue = soundFrame.getMfccValue();

                for (int k = 0; k < Settings.MFCC_USE; k++) {
                    temp[k] += mfccValue.getValue()[k];
                }
            }


            /*for (int k = 0; k < Settings.MFCC_USE; k++) {
                temp[k] /= counter;
            }*/
            word.result = temp;
        }

        ArrayList<MfccValue> mfccValues = new ArrayList<>();
        for (Word word : words) {
            mfccValues.add(new MfccValue(word.result));
        }

        soundMap.addWord(wordStr, mfccValues);
        System.out.println("New word was added");
    }

    private static void teach() {
        recorder = new SoundRecorder(true);

        System.out.println("Recording started, to end enter `");
        recorder.startRecording();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.next().equals("`")){}

        recorder.stopRecording();
        System.out.println("Recording stopped");

        byte[] data = recorder.getBytes();
        if (data == null) {
            return;
        }
        Speech speech = new Speech(data);


        double[] kix = new double[speech.getData().length];
        for (int i = 0; i < speech.getData().length; i++) {
            kix[i] = speech.getData()[i];
        }

        Splitter.splitSoundOnFrames(speech);
        ArrayList<SoundFrame> soundFrames = speech.getSoundFrames();


        double entropyValue;


        for (SoundFrame soundFrame : soundFrames) {


            double[] normalizedData = Normalizer.normalize( soundFrame.getFrameData(),
                    soundFrame.getStartPosition(),
                    soundFrame.getEndPosition());
            soundFrame.setNormalizedFrameData(normalizedData);


            entropyValue = Entropy.getEntropy(soundFrame);


            boolean isSilence;
            isSilence = Entropy.isSilence(entropyValue);
            soundFrame.setSilence(isSilence);
            soundFrame.setEntropyValue(entropyValue);

        }

        Splitter.splitIntoWords(speech);

        Analyzer analyzer = new Analyzer();
        analyzer.analyzeWords(speech);


        System.out.println("Number words = " + speech.getWords().size());

        ArrayList<Word> words = speech.getWords();

        for (Word word : words) {
            Splitter.splitInLargeFrames(word);
        }

        for (Word word : words) {

            int counter = 0;
            double[] temp = new double[Settings.MFCC_USE];
            for (int j = 0; j < Settings.MFCC_USE; j++) {
                temp[j] = 0.0;
            }

            for (SoundFrame soundFrame : word.getFrames()) {

                counter++;

                double[] t = new double[soundFrame.getEndPosition() - soundFrame.getStartPosition()];
                for (int j = 0; j < soundFrame.getEndPosition() - soundFrame.getStartPosition(); j++) {
                    t[j] = kix[j + soundFrame.getStartPosition()] - 0.95 * kix[j - 1 + soundFrame.getStartPosition()];
                }
                soundFrame.setNormalizedFrameData(t);


                soundFrame.setMfccValue(Mfcc.transform(soundFrame));

                MfccValue mfccValue = soundFrame.getMfccValue();

                for (int k = 0; k < Settings.MFCC_USE; k++) {
                    temp[k] += mfccValue.getValue()[k];
                }
            }


           /* for (int k = 0; k < Settings.MFCC_USE; k++) {
                temp[k] /= counter;
            }*/
            word.result = temp;
        }

        ArrayList<MfccValue> mfccValues = new ArrayList<>();
        for (Word word : words) {
            mfccValues.add(new MfccValue(word.result));
        }

        System.out.println("Which word is it");
        String wordName = scanner.next();
        soundMap.addWord(wordName, mfccValues);
        System.out.println("New word was added");
    }


    private static void recognize(String ...args) {

        recorder = new SoundRecorder(true);

        System.out.println("Recording started, to end enter `");
        recorder.startRecording();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.next().equals("`")){}

        recorder.stopRecording();
        System.out.println("Recording stopped");

        byte[] data = recorder.getBytes();
        if (data == null) {
            return;
        }
        recognize(data);


//        printHelpData(speech.getWords(), args);

    }


    private static ArrayList<String> recognize(byte[] data) {
        ArrayList<String> result = null;
        Speech speech = new Speech(data);


        double[] kix = new double[speech.getData().length];
        for (int i = 0; i < speech.getData().length; i++) {
            kix[i] = speech.getData()[i];
        }

        Splitter.splitSoundOnFrames(speech);
        ArrayList<SoundFrame> soundFrames = speech.getSoundFrames();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results.txt"))) {

            double entropyValue;


            for (SoundFrame soundFrame : soundFrames) {


                double[] normalizedData = Normalizer.normalize( soundFrame.getFrameData(),
                        soundFrame.getStartPosition(),
                        soundFrame.getEndPosition());
                soundFrame.setNormalizedFrameData(normalizedData);


                entropyValue = Entropy.getEntropy(soundFrame);

                bufferedWriter.write(entropyValue + "\n");

                boolean isSilence;
                isSilence = Entropy.isSilence(entropyValue);
                soundFrame.setSilence(isSilence);
                soundFrame.setEntropyValue(entropyValue);

            }

            Splitter.splitIntoWords(speech);

            Analyzer analyzer = new Analyzer();
            analyzer.analyzeWords(speech);


            System.out.println(speech.getWords().size());

            ArrayList<Word> words = speech.getWords();


            result = getMelByFrames(words, kix);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }






    private static void printHelpData(ArrayList<Word> words, String ...args) {
        ApplicationWindow applicationWindow = new ApplicationWindow();
        applicationWindow.initialize(words, args);
    }


    private static ArrayList<String> getMelByFrames(ArrayList<Word> words, double[] kix) {

        ArrayList<String> result = new ArrayList<>(0);
        for (Word word : words) {
            Splitter.splitInLargeFrames(word);
        }


        int fileCounter = 0;

        for (Word word : words) {

            int counter = 0;
            double[] temp = new double[Settings.MFCC_USE];
            for (int j = 0; j < Settings.MFCC_USE; j++) {
                temp[j] = 0.0;
            }

            System.out.println("\nword");
            System.out.println("Start frame = " + word.getStartFrame() + " position " + word.getStartPosition());
            System.out.println("End frame = " + word.getEndFrame() + " position " + word.getEndPosition());

            for (SoundFrame soundFrame : word.getFrames()) {
                counter++;

                double[] t = new double[soundFrame.getEndPosition() - soundFrame.getStartPosition()];
                for (int j = 0; j < soundFrame.getEndPosition() - soundFrame.getStartPosition(); j++) {
                    if (j > 0) {
                        t[j] = kix[j + soundFrame.getStartPosition()] - 0.95 * kix[j - 1 + soundFrame.getStartPosition()];
                    } else {
                        t[j] = kix[j + soundFrame.getStartPosition()];
                    }
                }
                soundFrame.setNormalizedFrameData(t);


                soundFrame.setMfccValue(Mfcc.transform(soundFrame));

                MfccValue mfccValue = soundFrame.getMfccValue();


                for (int k = 0; k < Settings.MFCC_USE; k++) {
                    temp[k] += mfccValue.getValue()[k];
                }
            }


            /*for (int k = 0; k < Settings.MFCC_USE ; k++) {
                temp[k] /= counter;
            }*/
            word.result = temp;
            System.out.println("Res = " + Arrays.toString(temp));
            String tempWord = soundMap.getValue(new MfccValue(temp));
            System.out.println(tempWord);
            result.add(tempWord);


            recorder = new SoundRecorder(true);
            int length = word.getEndPosition() - word.getStartPosition();
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = word.getFrames().get(0).getFrameData()[word.getStartPosition() + i];
            }
            recorder.getBytes();
            recorder.getInputStream(data, "Word" + fileCounter++ + ".wav");
        }
        return result;
    }

    private static final String RECOGNIZE = "RECOGNIZE";
    private static final String ADD_WORD = "ADD";

    private static void startServer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - home, 2 - mobile");
        String res = scanner.next();
        String ip;
        if (res.equals("1")) {
            ip = "192.168.0.11";
        } else {
            ip = "192.168.43.176";
        }

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(7071, 0, InetAddress.getByName(ip))) {


                Main.isServerRunning = true;
                while (Main.isServerRunning) {
                    try (
                            Socket socket = serverSocket.accept();
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    ){

                        String command;

                        byte[] inputData = new byte[500000];
                        command = dataInputStream.readUTF();

                        byte[] data;

                        if (command.contains(ADD_WORD)) {
                            String word = command.split("_")[1].toLowerCase();

                            int length = Integer.valueOf(command.split("_")[2]);

                            readAudioData(length, dataInputStream, inputData);

                            data = new byte[length];
                            System.arraycopy(inputData, 0, data, 0, length);

                            teach(data, word);
                            dataOutputStream.writeUTF(word);
                            dataOutputStream.flush();

                        } else {

                            int length = Integer.valueOf(command.split("_")[1]);

                            readAudioData(length, dataInputStream, inputData);

                            System.out.println(length);
                            data = new byte[length];
                            System.arraycopy(inputData, 0, data, 0, length);

//                            SoundRecorder soundRecorder = new SoundRecorder(true);
//                            soundRecorder.getBytes();
//                            soundRecorder.getInputStream(data, "FileName.wav");


                            ArrayList<String> words = recognize(data);
                            StringBuilder stringBuilder = new StringBuilder("");
                            if (words != null) {
                                for (String word : words) {
                                    stringBuilder.append(word);
                                    stringBuilder.append(" ");
                                }
                            }
//                            Thread.sleep(1000);
                            dataOutputStream.writeUTF(stringBuilder.toString());
                            dataOutputStream.flush();
                        }

                    } catch (IOException exception) {

                        exception.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void readAudioData(int length, DataInputStream dataInputStream, byte[] inputData) throws IOException {
        byte[] buffer = new byte[1024];
        int tempLength, count;
        tempLength = 0;
        while (tempLength != length && (count = dataInputStream.read(buffer)) != -1) {
            System.arraycopy(buffer, 0, inputData, tempLength, count);
            tempLength += count;
        }
    }

}
