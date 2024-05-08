package p2.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import p2.gui.rb.RBTreeAnimation;
import p2.gui.rb.RBTreeAnimationScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main entry point in executing the GUI.
 */
public class MyApplication extends Application {

    public static List<Thread> animationThreads = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {


        RBTreeAnimationScene<Integer> scene = new RBTreeAnimationScene<>(new RBTreeAnimation<Integer>(tree -> {
            Random random = new Random(2);

            for (int i = 0; i < 20; i++) {
                tree.insert(random.nextInt(100));
            }
        }, tree -> tree.insert(35)));

        primaryStage.setScene(scene);
        primaryStage.setTitle(scene.getTitle());

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        for (Thread thread : animationThreads) {
            thread.interrupt();
        }
    }

}
