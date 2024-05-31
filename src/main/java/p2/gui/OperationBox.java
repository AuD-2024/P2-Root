package p2.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OperationBox<T extends Comparable<T>> extends VBox {

    private final TextField value = new TextField();
    private final TextField maxTextField = new TextField();
    private final TextField limitTextField = new TextField();

    private final StringProperty lastResult = new SimpleStringProperty("-");

    public OperationBox(BinaryTreeAnimationScene<T> animationScene, Function<String, T> inputParser) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        HBox buttons = new HBox(5,
            createInsertButton(animationScene, inputParser),
            createInOrderButton(animationScene, inputParser),
            createFindNextButton(animationScene, inputParser));

        buttons.setAlignment(Pos.CENTER);

        getChildren().addAll(createInputGrid(), buttons, createLastResultLabel());
    }

    private Button createInsertButton(BinaryTreeAnimationScene<T> animationScene, Function<String, T> inputParser) {
        Button insertButton = new Button("Insert");

        insertButton.setOnAction(event -> {
            animationScene.getAnimationState().setExecuting("Insert(" + value.getText() + ")");
            animationScene.startAnimation(tree -> {
                tree.insert(inputParser.apply(value.getText()));
                Platform.runLater(() -> lastResult.set("Inserted " + value.getText()));
            });
        });

        insertButton.disableProperty().bind(value.textProperty().isEmpty());

        return insertButton;
    }

    private Button createFindNextButton(BinaryTreeAnimationScene<T> animationScene, Function<String, T> inputParser) {
        Button findNextButton = new Button("Find Next");

        findNextButton.setOnAction(event -> {
            animationScene.getAnimationState().setExecuting("FindNext(" + value.getText() + ")");
            List<Object> result = new ArrayList<>();
            animationScene.startAnimation(tree -> {
                tree.findNext(
                    tree.search(inputParser.apply(value.getText())),
                    result,
                    Integer.parseInt(maxTextField.getText()),
                    value -> value.compareTo(inputParser.apply(limitTextField.getText())) <= 0
                );

                Platform.runLater(() -> lastResult.set(result.toString()));

            });
        });

        findNextButton.disableProperty().bind(value.textProperty().isEmpty().or(maxTextField.textProperty().isEmpty()).or(limitTextField.textProperty().isEmpty()));

        return findNextButton;
    }

    private Button createInOrderButton(BinaryTreeAnimationScene<T> animationScene, Function<String, T> inputParser) {
        Button inOrderButton = new Button("In Order");

        inOrderButton.setOnAction(event -> {
            animationScene.getAnimationState().setExecuting("InOrder(");
            List<Object> result = new ArrayList<>();
            animationScene.startAnimation(tree -> {
                tree.inOrder(
                    tree.search(inputParser.apply(value.getText())),
                    result,
                    Integer.parseInt(maxTextField.getText()),
                    value -> value.compareTo(inputParser.apply(limitTextField.getText())) <= 0
                );

                Platform.runLater(() -> lastResult.set(result.toString()));

            });
        });

        inOrderButton.disableProperty().bind(value.textProperty().isEmpty().or(maxTextField.textProperty().isEmpty()).or(limitTextField.textProperty().isEmpty()));

        return inOrderButton;
    }

    private GridPane createInputGrid() {
        GridPane inputGrid = new GridPane();

        inputGrid.setHgap(10);
        inputGrid.setVgap(5);
        inputGrid.setPadding(new Insets(5, 5, 5, 5));

        inputGrid.add(new Label("Value:"), 0, 0);
        inputGrid.add(value, 1, 0);

        inputGrid.add(new Label("Max:"), 0, 1);
        inputGrid.add(maxTextField, 1, 1);

        inputGrid.add(new Label("Limit:"), 0, 2);
        inputGrid.add(limitTextField, 1, 2);

        return inputGrid;
    }

    private Label createLastResultLabel() {
        Label label = new Label();
        label.textProperty().bind(new SimpleStringProperty("Last Result: ").concat(lastResult));
        return label;
    }

    public void clearInputs() {
        value.clear();
        maxTextField.clear();
        limitTextField.clear();
    }

}
