package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.SearchTree;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class InsertBSTTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "InsertBST_Simple.json", customConverters = "customConverters")
    public void testBSTInsertSimple(JsonParameterSet params) {
        testBSTInsert(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "InsertBST_Complex.json", customConverters = "customConverters")
    public void testBSTInsertComplex(JsonParameterSet params) {
        testBSTInsert(params);
    }

    private void testBSTInsert(JsonParameterSet params) {
        testForBSTAndRBTree(params, (tree, className) -> testBSTInsert(params, tree, className));
    }

    @SuppressWarnings("unchecked")
    private <N extends AbstractBinaryNode<Integer, N>> void testBSTInsert(JsonParameterSet params, SearchTree<Integer> tree, String className) {

        int input = params.get("input");
        SimpleBinarySearchTree<Integer> expectedBST = params.get("expectedBST");

        AbstractBinarySearchTree<Integer, N> bst = (AbstractBinarySearchTree<Integer, N>) tree;
        N initialPX = (bst instanceof RBTree<?> rbTree) ? (N) rbTree.sentinel : null;

        Context.Builder<?> context = contextBuilder()
            .subject("AbstractBinarySearchTree#insert(N, N)")
            .add("initial tree", treeToString(bst))
            .add("implementation", className)
            .add("input", input)
            .add("expected bst", treeToString(expectedBST));

        call(() -> bst.insert(bst.createNode(input), initialPX), context.build(), result -> "insert should not throw an exception");

        context.add("actual bst", treeToString(bst));

        assertTreeEquals(expectedBST, bst, context.build(), "The resulting tree is not correct");
    }

}
