package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.BinarySearchTree;

import java.util.ArrayList;
import java.util.List;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

public class InOrderTest extends P2_TestBase {


    @ParameterizedTest
    @JsonParameterSetTest(value = "InOrder_Simple.json", customConverters = "customConverters")
    public void testInOrderSimple(JsonParameterSet params) {
        testInOrder(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "InOrder_Complex.json", customConverters = "customConverters")
    public void testInOrderComplex(JsonParameterSet params) {
        testInOrder(params);
    }

    private void testInOrder(JsonParameterSet params) {

        BinarySearchTree<Integer> bst = params.get("bst");
        int max = params.getInt("max");
        List<Integer> expected = params.get("expectedList");
        int limit = params.getInt("limit");

        Context.Builder<?> context = contextBuilder()
            .subject("BinarySearchTree#inOrder")
            .add("bst", bst.toString())
            .add("max", max)
            .add("predicate", "i <= " + limit)
            .add("expected", expected);

        List<Integer> actual = new ArrayList<>();

        call(() -> bst.inOrder(bst.getRoot(), actual, max, i -> i <= limit), context.build(),
            result -> "inOrder should not throw an exception");

        context.add("actual", actual);

        assertEquals(expected.size(), actual.size(), context.build(),
            result -> "The list returned by inOrder has the wrong size");

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), actual.get(i), context.build(),
                result -> "The list returned by inOrder has the wrong element at index " + finalI);
        }

        assertTreeUnchanged(params.get("bst"), bst, context.build());
    }

}
