package p2.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AnimationState {

    private final StringProperty operation = new SimpleStringProperty();
    private final ObservableList<StackTraceElement> stackTrace = FXCollections.observableArrayList();

    public StringProperty getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.set(operation);
    }

    public ObservableList<StackTraceElement> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace.setAll(stackTrace);
    }
}
