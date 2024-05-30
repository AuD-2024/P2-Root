package p2.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Main entry point in executing the GUI.
 */
public class MyApplication extends Application {

    public static BinaryTreeAnimationScene<?> currentScene = null;

    @Override
    public void start(Stage primaryStage) {

        LoadTreeScene loadTreeScene = new LoadTreeScene(primaryStage);

        primaryStage.setScene(loadTreeScene);
        primaryStage.setTitle("Binary Tree Animation");

        primaryStage.show();
    }

    @Override
    public void stop() {
        currentScene.stopAnimation();
    }

}
