package p2.binarytree;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import p2.Node;
import p2.SearchTree;
import p2.binarytree.implementation.TestBSTNode;
import p2.binarytree.implementation.TestRBNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;

public class TraversingTest extends P2_TestBase {

    protected void testTraversing(JsonParameterSet params, TraversingMethod traversingMethod, String method) throws Throwable {
        testForBSTAndRBTree(params, (tree, className) -> testTraversing(params, tree, traversingMethod, method, className));
    }

    private void testTraversing(JsonParameterSet params, SearchTree<Integer> tree, TraversingMethod traversingMethod, String methodName, String className) {

        int startNodeKey = params.getInt("startNode");
        int max = params.getInt("max");
        int limit = params.getInt("limit");
        List<Integer> expected = params.get("expectedList");

        BinaryNode<Integer> startNode = (BinaryNode<Integer>) tree.search(startNodeKey);

        Context.Builder<?> context = contextBuilder()
            .subject(className + "#"+ methodName)
            .add("bst", tree.toString())
            .add("startNode", startNode)
            .add("max", max)
            .add("predicate", "i <= " + limit)
            .add("expected", expected);

        List<Integer> actual = new ArrayList<>();

        TestRBNode.startCounting();
        TestBSTNode.startCounting();
        try {
            call(() -> traversingMethod.traverse(tree, startNode, actual, max, i -> i <= limit), context.build(),
                result -> methodName + " should not throw an exception");
        } finally {
            TestRBNode.stopCounting();
            TestBSTNode.stopCounting();
        }

        context.add("actual", actual);

        assertEquals(expected.size(), actual.size(), context.build(),
            result -> "The list returned by " + methodName + " has the wrong size");

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), actual.get(i), context.build(),
                result -> "The list returned by " + methodName + " has the wrong element at index " + finalI);
        }

        checkVisitedNodes((BinaryNode<Integer>) tree.getRoot(), expected, startNodeKey, findSmallest(startNode), context.build());

        //TODO assertTreeUnchanged(params.get("bst"), tree, context.build());
    }

    private void checkVisitedNodes(BinaryNode<Integer> node, List<Integer> expected, int startNodeKey, int smallestVisited, Context context) {

        if (node == null) return;

        if (!expected.contains(node.getKey()) && node.getKey() != startNodeKey) {
            int leftCount = node instanceof TestRBNode<Integer> testRBNode ? testRBNode.getLeftCount() :
                ((TestBSTNode<Integer>) node).getLeftCount();
            int rightCount = node instanceof TestRBNode<Integer> testRBNode ? testRBNode.getRightCount() :
                ((TestBSTNode<Integer>) node).getRightCount();

            assertEquals(0, rightCount, context,
                result -> "The getRight() method of a node that should not be visited (key: " + node.getKey() + ") was called");

            if (node.getKey() < smallestVisited) {
                assertEquals(0, leftCount, context,
                    result -> "The getLeft() method of a node that should not be visited (key: " + node.getKey() + ") was called");
            }
        }

        checkVisitedNodes(node.getLeft(), expected, startNodeKey, smallestVisited, context);
        checkVisitedNodes(node.getRight(), expected, startNodeKey, smallestVisited, context);
    }

    private int findSmallest(BinaryNode<Integer> node) {
        if (node.getLeft() != null) return findSmallest(node.getLeft());
        return node.getKey();
    }

    @FunctionalInterface
    protected interface TraversingMethod {
        void traverse(SearchTree<Integer> bst, Node<Integer> node, List<Integer> result, int max, Predicate<Integer> predicate);
    }

}
