package com.bsuir.speech_recognizer.sound;

import com.bsuir.speech_recognizer.settings.Settings;
import javafx.collections.ArrayChangeListener;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static com.bsuir.speech_recognizer.settings.Settings.*;

public class SoundRecorder {


    private final static String FILE_NAME = "temp.wav";

    private File lastRecord = null;
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private AudioFormat format;
    private TargetDataLine mike;

    public SoundRecorder(boolean isBigEndian) {
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, NUMBER_CHANNELS, BYTES_IN_FRAME, FRAME_RATE, isBigEndian);
    }

    public void startRecording() {
        lastRecord = new File(FILE_NAME);
        new Thread() {
            @Override
            public void run() {

                DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported" + info);
                }

                try {
                    mike = (TargetDataLine) AudioSystem.getLine(info);
                    mike.open(format, mike.getBufferSize());
                    AudioInputStream sound = new AudioInputStream(mike);
                    mike.start();
                    AudioSystem.write(sound, fileType, lastRecord);
                } catch (LineUnavailableException | IOException exception) {
                    exception.printStackTrace();
                }

            }
        }.start();
    }

    public void stopRecording() {
        mike.stop();
        mike.close();
    }

    public byte[] getBytes() {

        //TODO: Временно
        lastRecord = new File("temp.wav");


        byte[] result = null;

        if (lastRecord == null) {
            return null;
        }

        AudioInputStream audioInputStream;
        long framesCount;
        long dataLength;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(lastRecord);

            framesCount = audioInputStream.getFrameLength(); // number frames

            dataLength = framesCount * SAMPLE_SIZE_IN_BITS * NUMBER_CHANNELS / 8;

            result = new byte[(int) dataLength];

            audioInputStream.read(result);

        } catch (UnsupportedAudioFileException | IOException exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
