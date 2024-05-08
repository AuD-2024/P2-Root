package p2.gui.rb;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import p2.binarytree.RBNode;
import p2.gui.AnimationScene;
import p2.gui.ControlPane;
import p2.gui.GraphPane;
import p2.gui.MyApplication;

public class RBTreeAnimationScene<T extends Comparable<T>> extends AnimationScene<T> {

    private final RBTreeAnimationState state = new RBTreeAnimationState();


    private RBTreeAnimation<T> tree;
    private RBTreeInfoBox infoPane;
    private ControlPane controlPane;

    public RBTreeAnimationScene(RBTreeAnimation<T> animation) {

        animation.init(this, state);

        graphPane = new GraphPane<>(animation.getRoot());
        root.setCenter(graphPane);

        infoPane = new RBTreeInfoBox(state);
        root.setRight(infoPane);

        controlPane = new ControlPane();
        controlPane.init(animation, graphPane);
        root.setBottom(controlPane);

        Thread animationThread = new Thread(animation::start);
        MyApplication.animationThreads.add(animationThread);
        animationThread.start();
    }

    public void refresh(RBNode<T> source, RBNode<T> target) {
        graphPane.setEdgeColor(source, target, Color.GREEN);
        graphPane.setNodeStrokeColor(source, Color.GREEN);
    }

    @Override
    public RBTreeInfoBox getInfoBox() {
        return infoPane;
    }

    @Override
    public GraphPane<T, RBNode<T>> getGraphPane() {
        return graphPane;
    }

    @Override
    public String getTitle() {
        return "Red-Black Tree Animation";
    }
}
