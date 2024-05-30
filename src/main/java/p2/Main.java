package p2;

import javafx.application.Application;
import p2.binarytree.AutoComplete;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.RBTree;
import p2.binarytree.SimpleBinarySearchTree;
import p2.gui.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * <h2>GUI Guide:</h2>
     * <p>
     * After starting the gui you can load an empty red-black or simple binary search tree by clicking the respective
     * buttons. When entering a string representation of a tree used in the test, that tree will be loaded instead.
     * <p>
     * When a tree is loaded, you can view it in the center. On the top right you can enter input values and execute
     * the respective operation by clicking the buttons below.
     * <ul>
     *     <li>Inserts: Invokes insert with the value given in "value".</li>
     *     <li>In Order: Invokes inOrder with the value given in "value" as the start node, "Max" as the maximum amount
     *     of values to return and {@code x -> x <= "Limit"} as the predicate.</li>
     *     <li>Find Next: Invokes findNext with the value given in "value" as the start node, "Max" as the maximum amount
     *      of values to return and {@code x -> x <= "Limit"} as the predicate.
     * </ul>
     *
     * When the "Animate" checkbox at the bottom left is selected, the program will stop after each invocation of
     * {@code {get,set}{Left,Right}} and {@code setColor} and highlight the respective nodes. You can then continue the
     * animation by clicking the "Next Step" button at the bottom left. When stopped, the current stack trace and the
     * last performed operation is shown at the top right.
     * <p>
     * You can change the appearance and colors of the tree in the class {@link p2.gui.GraphStyle}.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {

        // Uncomment the following line to run the AutoComplete example for task H3 d)
        // runAutoCompleteExample();

        Application.launch(MyApplication.class, args);
    }

    private static void runAutoCompleteExample() {
        String prefix = "z";
        int max = 10;
        autoCompleteExample(prefix, max, true);
        autoCompleteExample(prefix, max, false);
    }

    private static void autoCompleteExample(String prefix, int max, boolean useRBTree) {

        AutoComplete acRBTree = new AutoComplete("words_alpha.txt", useRBTree);
        String type = useRBTree ? "Red-Black Tree" : "Simple Binary Tree";

        List<String> result = acRBTree.autoComplete(prefix, max);

        System.out.printf("Initialization time using %s: %.2fms\n", type, (acRBTree.getInitializationTime() / 10000) / 100d);
        System.out.printf("Computation time using %s: %.2fms\n", type, (acRBTree.getLastComputationTime() / 10000) / 100d);

        System.out.printf("Results for prefix=\"%s\" and max=%s using %s:\n", prefix, max, type);
        for (int i = 0; i < result.size(); i++) {
            System.out.printf("%d: %s\n", i + 1, result.get(i));
        }
    }
}
