package p2;

import p2.binarytree.AutoComplete;

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

        String prefix = "z";
        int max = 10;

        autoCompleteExample(prefix, max, true);
        autoCompleteExample(prefix, max, false);

        //Application.launch(MyApplication.class, args);
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
