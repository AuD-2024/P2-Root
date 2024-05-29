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
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {

//        String prefix = "z";
//        int max = 10;
//
//        autoCompleteExample(prefix, max, true);
//        autoCompleteExample(prefix, max, false);

        SearchTree<Integer> bst = new SimpleBinarySearchTree<>();
        SearchTree<Integer> rb = new RBTree<>();

        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            l.add(i);
        }

        Collections.shuffle(l);

        for (int i : l) {
            bst.insert(i);
            rb.insert(i);
        }

        System.out.println(bst);
        System.out.println(rb);


        Application.launch(MyApplication.class, args);
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
