package p2.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Main entry point in executing the GUI.
 */
public class MyApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        LoadTreeScene loadTreeScene = new LoadTreeScene(primaryStage);

        primaryStage.setScene(loadTreeScene);
        primaryStage.setTitle("Binary Tree Animation");

        primaryStage.show();
    }

    @Override
    public void stop() {
        //TODO make static
        //animationScene.stopAnimation();
    }

}
