package com.bsuir.speech_recognizer.graphis;

import com.bsuir.speech_recognizer.settings.Settings;
import com.bsuir.speech_recognizer.sound.SoundFrame;
import com.bsuir.speech_recognizer.sound.Word;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ApplicationWindow extends Application {
    private static ArrayList<Word> words;
    private VBox vBox;
    public void initialize(ArrayList<Word> words,  String ...args) {
        this.words = words;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        vBox = new VBox();
        draw();
        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();
    }

    private void draw() {
        String temp = "Word ";
        /*for (Word word : words) {
            LineChart lineChart = new LineChart(new NumberAxis(), new NumberAxis());
            for (SoundFrame soundFrame : word.getFrames()) {
                lineChart.getData().add(new XYChart.Series<>(temp + Integer.toString(2), FXCollections.observableArrayList(getCharts(soundFrame))));
            }

            this.vBox.getChildren().add(lineChart);
        }*/

        for (Word word : words) {
            LineChart lineChart = new LineChart(new NumberAxis(), new NumberAxis());
                lineChart.getData().add(new XYChart.Series<>(temp + Integer.toString(2), FXCollections.observableArrayList(getCharts(word.result))));

            this.vBox.getChildren().add(lineChart);
        }

    }

    private ArrayList<XYChart.Data<Object, Object>> getCharts(SoundFrame soundFrame) {
        ArrayList<XYChart.Data<Object, Object>> result = new ArrayList<>();
        double[] mfcc = soundFrame.getMfccValue().getValue();

        for (int i = 2; i < Settings.MFCC_USE; i++) {
            result.add(new XYChart.Data<Object, Object>(i, mfcc[i]));
        }
        return result;
    }

    private ArrayList<XYChart.Data<Object, Object>> getCharts(double[] data) {
        ArrayList<XYChart.Data<Object, Object>> result = new ArrayList<>();

        for (int i = 0; i < Settings.MFCC_USE; i++) {
            result.add(new XYChart.Data<Object, Object>(i, data[i]));
        }
        return result;
    }


}
