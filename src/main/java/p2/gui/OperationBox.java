package p2.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class OperationBox<T extends Comparable<T>> extends VBox {

    private final TextField insertTextField = new TextField();
    private final TextField inOrderTextField = new TextField();
    private final TextField findNextTextField = new TextField();

    public OperationBox(BinaryTreeAnimationScene<T> animationScene, Function<String, T> inputParser) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        HBox insertHBOX = new HBox();
        Button insertButton =  new Button("Insert");

        insertButton.setOnAction(event -> {
            animationScene.getAnimationState().setExecuting("Insert(" + insertTextField.getText() + ")");
            animationScene.startAnimation(tree -> tree.insert(inputParser.apply(insertTextField.getText())));
        });

        insertHBOX.getChildren().addAll(insertButton, insertTextField);

        HBox inOrderHBOX = new HBox();
        Button inOrderButton = new Button("In Order");

        inOrderHBOX.getChildren().addAll(inOrderButton, inOrderTextField);

        HBox findNextHBOX = new HBox();
        Button findNextButton = new Button("Find Next");

        findNextHBOX.getChildren().addAll(findNextButton, findNextTextField);

        insertButton.minWidthProperty().bind(widthProperty().divide(3));
        inOrderButton.minWidthProperty().bind(widthProperty().divide(3));
        findNextButton.minWidthProperty().bind(widthProperty().divide(3));

        getChildren().addAll(insertHBOX, inOrderHBOX, findNextHBOX);
    }

    public void clearInputs() {
        insertTextField.clear();
        inOrderTextField.clear();
        findNextTextField.clear();
    }

}
