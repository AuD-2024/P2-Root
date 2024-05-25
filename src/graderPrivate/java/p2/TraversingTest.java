package p2;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

public class TraversingTest extends P2_TestBase {

    protected void testTraversing(JsonParameterSet params, TraversingMethod traversingMethod, String method) {

        boolean testPerformed = false;

        if (params.getRootNode().has("bst")) {
            testTraversing(params, params.get("bst"), traversingMethod, method, "BinarySearchTree");
            testPerformed = true;
        }
        if (params.getRootNode().has("RBTree")) {
            testTraversing(params, params.get("RBTree"), traversingMethod, method, "RBTree");
            testPerformed = true;
        }

        if (!testPerformed) {
            throw new IllegalArgumentException("Internal error: No tree found in the parameter set");
        }
    }

    private void testTraversing(JsonParameterSet params, SearchTree<Integer> tree, TraversingMethod traversingMethod, String methodName, String className) {

        int startNode = params.getInt("startNode");
        int max = params.getInt("max");
        int limit = params.getInt("limit");
        List<Integer> expected = params.get("expectedList");

        Context.Builder<?> context = contextBuilder()
            .subject(className + "#"+ methodName)
            .add("bst", tree.toString())
            .add("startNode", startNode)
            .add("max", max)
            .add("predicate", "i <= " + limit)
            .add("expected", expected);

        List<Integer> actual = new ArrayList<>();

        call(() -> traversingMethod.traverse(tree, tree.search(startNode), actual, max, i -> i <= limit), context.build(),
            result -> methodName + " should not throw an exception");

        context.add("actual", actual);

        assertEquals(expected.size(), actual.size(), context.build(),
            result -> "The list returned by " + methodName + " has the wrong size");

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), actual.get(i), context.build(),
                result -> "The list returned by " + methodName + " has the wrong element at index " + finalI);
        }

        //TODO assertTreeUnchanged(params.get("bst"), tree, context.build());
    }

    @FunctionalInterface
    protected interface TraversingMethod {
        void traverse(SearchTree<Integer> bst, Node<Integer> node, List<Integer> result, int max, Predicate<Integer> predicate);
    }

}
