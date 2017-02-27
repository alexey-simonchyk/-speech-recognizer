package com.bsuir.speech_recognizer.graphis;

import com.bsuir.speech_recognizer.sound.SoundFrame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ApplicationWindow extends Application {

    private static final int LENGTHINESS = 5;
    private static final int SCALE = -200;
    private static final int OFFSET = 880;
    private static final int GRAPHIC_OFFSET = 20;

    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;

    public static int SIZE = 0;

    private static ArrayList<Double> drawValues = new ArrayList<>();

    private volatile GraphicsContext graphicsContext;
    private double currentXPosition = GRAPHIC_OFFSET / 2;
    private double currentYPosition = 0;

    public void initialize(String ...args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();

        Canvas canvas = new Canvas(SIZE * LENGTHINESS + GRAPHIC_OFFSET * 2, SCREEN_HEIGHT);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");
        group.getChildren().add(scrollPane);
        graphicsContext = canvas.getGraphicsContext2D();

        Scene scene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        draw();
        drawHelpLines();
        primaryStage.show();
    }

    private void drawHelpLines() {
        double temp = 2.2;
        temp = temp * SCALE + OFFSET;
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(GRAPHIC_OFFSET / 2, temp, SIZE * LENGTHINESS + GRAPHIC_OFFSET / 2, temp);

        temp = 2.35;
        temp = temp * SCALE + OFFSET;
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.strokeLine(GRAPHIC_OFFSET / 2, temp, SIZE * LENGTHINESS + GRAPHIC_OFFSET / 2, temp);

        temp = 2.4;
        temp = temp * SCALE + OFFSET;
        graphicsContext.setStroke(Color.BROWN);
        graphicsContext.strokeLine(GRAPHIC_OFFSET / 2, temp, SIZE * LENGTHINESS + GRAPHIC_OFFSET / 2, temp);

        temp = 2.5;
        temp = temp * SCALE + OFFSET;
        graphicsContext.setStroke(Color.GREEN);
        graphicsContext.strokeLine(GRAPHIC_OFFSET / 2, temp, SIZE * LENGTHINESS + GRAPHIC_OFFSET / 2, temp);

        temp = 2.7;
        temp = temp * SCALE + OFFSET;
        graphicsContext.setStroke(Color.PINK);
        graphicsContext.strokeLine(GRAPHIC_OFFSET / 2, temp, SIZE * LENGTHINESS + GRAPHIC_OFFSET / 2, temp);
    }

    public static void draw(double value) {

        drawValues.add(value);
    }

    private void draw() {
//        System.out.println(drawValues.size());
        int counter = 10;
        for (double value : drawValues) {
            double temp;
            temp = value * SCALE + OFFSET;
            if (currentYPosition != 0) {
                graphicsContext.strokeLine(currentXPosition, currentYPosition, currentXPosition + LENGTHINESS, temp);
            } else {
                graphicsContext.strokeLine(currentXPosition, temp, currentXPosition + LENGTHINESS, temp);
            }

            currentYPosition = temp;
            currentXPosition += LENGTHINESS;
            counter += 5;
            if (counter % 500 == 0) {
                drawSecondLine(currentXPosition);
            }
        }
    }

    private void drawSecondLine(double offset) {
        graphicsContext.strokeLine(offset, SCREEN_HEIGHT, offset, SCREEN_HEIGHT - 250);
    }
}
