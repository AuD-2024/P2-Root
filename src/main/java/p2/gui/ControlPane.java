package p2.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * A pane for controlling the animation.
 * it contains a button for stepping through the animation and a button for centering the graph.
 */
public class ControlPane extends HBox {

    private final Button nextStepButton = new Button("Next Step");
    private final Button centerButton = new Button("Center Graph");
    private final Button zoomInButton = new Button("Zoom In");
    private final Button zoomOutButton = new Button("Zoom Out");

    private GraphPane<?, ?> graphPane;

    /**
     * Constructs a new control pane.
     */
    public ControlPane() {
        super(5);
        setPadding(new Insets(5));
    }

    /**
     * Initializes the control pane.
     *
     * @param animation the animation to control
     * @param graphPane the graph pane to control
     */
    public void init(Animation animation, GraphPane<?, ?> graphPane) {
        this.graphPane = graphPane;

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        getChildren().addAll(nextStepButton, centerButton, zoomInButton, zoomOutButton);

        nextStepButton.setOnAction(event -> {
            synchronized (animation.getSyncObject()) {

                animation.getSyncObject().notify();
            }
        });

        centerButton.setOnAction(event -> this.graphPane.center());

        zoomInButton.setOnAction(event -> graphPane.zoomIn());

        zoomOutButton.setOnAction(event -> graphPane.zoomOut());
    }

    public void disableNextStepButton() {
        nextStepButton.setDisable(true);
    }

    public void setGraphPane(GraphPane<?, ?> graphPane) {
        this.graphPane = graphPane;
    }

}
