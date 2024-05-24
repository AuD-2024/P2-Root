package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.BinarySearchTree;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

public class InsertTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "InsertBST_Simple.json", customConverters = "customConverters")
    public void testBSTInsertSimple(JsonParameterSet params) throws ReflectiveOperationException {
        testBSTInsert(params);
    }

    private void testBSTInsert(JsonParameterSet params) throws ReflectiveOperationException {
        BinarySearchTree<Integer> bst = params.get("bst");
        int input = params.get("input");
        BinarySearchTree<Integer> expectedBST = params.get("expectedBST");

        Context.Builder<?> context = contextBuilder()
            .subject("AbstractBinarySearchTree#insert")
            .add("initial bst", bst.toString())
            .add("input", input)
            .add("expected bst", expectedBST.toString());

        call(() -> bst.insert(input), context.build(), result -> "insert should not throw an exception");

        context.add("actual bst", bst.toString());

        assertTreeEquals(expectedBST, bst, context.build());
    }

}
