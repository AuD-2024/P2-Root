package p2.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * A pane for controlling the animation.
 * it contains a button for stepping through the animation and a button for centering the graph.
 */
public class ControlBox<T extends Comparable<T>> extends HBox {

    private final Button nextStepButton;

    /**
     * Constructs a new control box.
     */
    public ControlBox(BinaryTreeAnimationScene<T> animationScene) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        nextStepButton = new Button("Next Step");
        Button zoomOutButton = new Button("Zoom Out");
        Button zoomInButton = new Button("Zoom In");
        Button centerButton = new Button("Center Tree");
        CheckBox animationCheckBox = new CheckBox("Animate");

        animationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animationScene.getAnimation().turnOnAnimation();
            } else {
                animationScene.getAnimation().turnOffAnimation();
            }
        });

        animationCheckBox.setSelected(true);

        getChildren().addAll(nextStepButton, centerButton, zoomInButton, zoomOutButton, animationCheckBox);
        setAlignment(Pos.CENTER_LEFT);

        nextStepButton.setOnAction(event -> {
            synchronized (animationScene.getAnimation().getSyncObject()) {
                animationScene.getAnimation().getSyncObject().notify();
            }
        });

        centerButton.setOnAction(event -> animationScene.getTreePane().center());
        zoomInButton.setOnAction(event -> animationScene.getTreePane().zoomIn());
        zoomOutButton.setOnAction(event -> animationScene.getTreePane().zoomOut());
    }

    public void disableNextStepButton() {
        nextStepButton.setDisable(true);
    }

}
