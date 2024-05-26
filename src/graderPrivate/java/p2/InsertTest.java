package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.AbstractBinaryNode;
import p2.binarytree.AbstractBinarySearchTree;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.RBTree;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

public class InsertTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "InsertBST_Simple.json", customConverters = "customConverters")
    public void testBSTInsertSimple(JsonParameterSet params) throws Throwable {
        testBSTInsert(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "InsertBST_Complex.json", customConverters = "customConverters")
    public void testBSTInsertComplex(JsonParameterSet params) throws Throwable {
        testBSTInsert(params);
    }

    private void testBSTInsert(JsonParameterSet params) throws Throwable {
        testForBSTAndRBTree(params, (tree, className) -> testBSTInsert(params, tree, className));
    }

    private void testBSTInsert(JsonParameterSet params, SearchTree<Integer> tree, String className) throws ReflectiveOperationException {


        int input = params.get("input");
        BinarySearchTree<Integer> expectedBST = params.get("expectedBST");

        AbstractBinarySearchTree<Integer, ?> bst = (AbstractBinarySearchTree<Integer, ?>) tree;
        AbstractBinaryNode<?, ?> initialPX = (bst instanceof RBTree<?> rbTree) ? getSentinel(rbTree) : null;

        Context.Builder<?> context = contextBuilder()
            .subject("AbstractBinarySearchTree#insert(N)")
            .add("initial tree", bst.toString())
            .add("implementation", className)
            .add("input", input)
            .add("expected bst", expectedBST.toString());

        call(() -> invokeInsert(bst, invokeCreateNode(bst, input), initialPX), context.build(), result -> "insert should not throw an exception");

        context.add("actual bst", bst.toString());

        assertTreeEquals(expectedBST, bst, context.build());
    }

}
