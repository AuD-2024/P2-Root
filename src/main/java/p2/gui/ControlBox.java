package p2.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import p2.SearchTree;
import p2.binarytree.AbstractBinarySearchTree;
import p2.binarytree.BinaryNode;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.TreeParser;

import java.util.function.Function;

/**
 * A pane for controlling the animation.
 * it contains a button for stepping through the animation and a button for centering the graph.
 */
public class ControlBox extends HBox {

    private final Button nextStepButton;

    /**
     * Constructs a new control box.
     */
    public ControlBox(BinaryTreeAnimationScene animationScene) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        nextStepButton = new Button("Next Step");
        Button zoomOutButton = new Button("Zoom Out");
        Button zoomInButton = new Button("Zoom In");
        Button centerButton = new Button("Center Tree");
        Button loadRBTreeButton = new Button("Load Red-Black Tree");
        Button loadBSTButton = new Button("Load Binary Search Tree");

        getChildren().addAll(nextStepButton, centerButton, zoomInButton, zoomOutButton, loadRBTreeButton, loadBSTButton);

        nextStepButton.setOnAction(event -> {
            synchronized (animationScene.getAnimation().getSyncObject()) {
                animationScene.getAnimation().getSyncObject().notify();
            }
        });

        centerButton.setOnAction(event -> animationScene.getGraphPane().center());
        zoomInButton.setOnAction(event -> animationScene.getGraphPane().zoomIn());
        zoomOutButton.setOnAction(event -> animationScene.getGraphPane().zoomOut());
        loadRBTreeButton.setOnAction(event -> loadTree(animationScene, str -> TreeParser.parseRBTree(str, RBTreeAnimation::new)));
        loadBSTButton.setOnAction(event -> loadTree(animationScene, str -> TreeParser.parseBST(str, BinarySearchTreeAnimation::new)));

    }

    private void loadTree(BinaryTreeAnimationScene animationScene, Function<String, BinaryTreeAnimation> parser) {
        TextInputDialog dialog = new TextInputDialog("title");
        dialog.setTitle("Load Tree");
        dialog.showAndWait().ifPresent(str -> {
            BinaryTreeAnimation tree = parser.apply(str);
            animationScene.loadTreeAnimation(tree);
        });
    }

    public void disableNextStepButton() {
        nextStepButton.setDisable(true);
    }

}
