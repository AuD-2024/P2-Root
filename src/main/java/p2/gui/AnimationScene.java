package p2.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import p2.binarytree.RBNode;

/**
 * A scene for displaying an animation.
 */
public abstract class AnimationScene<T extends Comparable<T>> extends Scene {


    protected final BorderPane root;

    protected GraphPane<T, RBNode<T>> graphPane;

    /**
     * Constructs a new animation scene.
     *
     * @param root the root node of the scene
     */
    public AnimationScene() {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPrefSize(700, 700);
    }

    public abstract InfoBox<?> getInfoBox();

    public abstract GraphPane<T, RBNode<T>> getGraphPane();

    /**
     * Gets the title of the scene.
     *
     * @return the title of the scene
     */
    public abstract String getTitle();
}
