package p2.gui.rb;

import javafx.scene.control.Label;
import p2.gui.InfoBox;

public class RBTreeInfoBox extends InfoBox<RBTreeAnimationState> {

    private final Label operationLabel = new Label();

    public RBTreeInfoBox(RBTreeAnimationState state) {
        super(state);
        getChildren().add(0, operationLabel);
    }

    public void setOperation(String operation) {
        operationLabel.setText("Current operation: " + operation);
    }

}
