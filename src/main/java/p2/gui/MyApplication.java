package p2.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Main entry point in executing the GUI.
 */
public class MyApplication extends Application {

    private BinaryTreeAnimationScene animationScene;

    @Override
    public void start(Stage primaryStage) {

        BinaryTreeAnimationScene animationScene = new BinaryTreeAnimationScene();
        this.animationScene = animationScene;

        animationScene.loadTreeAnimation(new RBTreeAnimation<Integer>(tree -> {
            Random random = new Random(2);

            for (int i = 0; i < 20; i++) {
                tree.insert(random.nextInt(100));
            }
//            RBTree<Integer> tree2 = new RBTree<>();
//
//            for (int i = 0; i < 40; i++) {
//
//                tree.insert(i);
//                if (i < 20) tree2.insert(i + 100);
//
//            }
//
//            tree.join(tree2, 20);

        }, tree -> tree.insert(35)));

        primaryStage.setScene(animationScene);
        primaryStage.setTitle("Binary Tree Animation");

        primaryStage.show();
    }

    @Override
    public void stop() {
        animationScene.stopAnimation();
    }

}
