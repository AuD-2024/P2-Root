package p2.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import p2.gui.rb.RBTreeInfoBox;

public class AbstractBSTAnimationScene<T extends Comparable<T>, N extends AnimatedAbstractBST<T,N>.AnimatedBinaryNode> extends Scene {

    private final BorderPane root;

    private GraphPane<T, N> graphPane;
    private AnimatedAbstractBST<T, N> tree;
    private RBTreeInfoBox infoPane;
    private ControlPane controlPane;

    public AbstractBSTAnimationScene() {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPrefSize(700, 700);
    }

    public void init(AnimatedAbstractBST<T, N> tree) {
        this.tree = tree;
    }

    public void refresh(N source, N target) {
        graphPane.setEdgeColor(source, target, Color.GREEN);
        graphPane.setNodeStrokeColor(source, Color.GREEN);
    }

    public RBTreeInfoBox getInfoPane() {
        return infoPane;
    }

    public GraphPane<T, N> getGraphPane() {
        return graphPane;
    }

    public AnimatedAbstractBST<T, N> getAnimatedTree() {
        return tree;
    }

}
