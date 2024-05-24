package p2.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import p2.binarytree.AbstractBinaryNode;
import p2.binarytree.BinaryNode;

/**
 * A scene for displaying an animation.
 */
public class BinaryTreeAnimationScene extends Scene {

    private final GraphPane graphPane = new GraphPane();

    private final AnimationState animationState = new AnimationState();

    private Animation animation;
    private Thread animationThread;

    /**
     * Constructs a new animation scene.
     */
    public BinaryTreeAnimationScene() {
        super(new BorderPane());
        BorderPane root = (BorderPane) getRoot();

        root.setPrefSize(700, 700);

        root.setCenter(graphPane);
        root.setBottom(new ControlBox(this));

        InfoBox infoBox = new InfoBox(animationState);
        root.setRight(infoBox);
    }

    public void loadTreeAnimation(BinaryTreeAnimation animation) {
        this.animation = animation;

        animationState.clear();

        animation.init(this);
        graphPane.setTree(animation.getRoot());
        graphPane.center();
        startAnimation();
    }

    public void refresh(BinaryNode<?> source, BinaryNode<?> target) {
        graphPane.setEdgeColor(source, target, Color.GREEN);
        graphPane.setNodeStrokeColor(source, Color.GREEN);
    }

    public void stopAnimation() {
        if (animationThread != null) {
            animationThread.interrupt();
        }
    }

    private void startAnimation() {
        stopAnimation();

        animationThread = new Thread(animation::start);
        animationThread.start();
    }

    public GraphPane getGraphPane() {
        return graphPane;
    }

    public Animation getAnimation() {
        return animation;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }
}
