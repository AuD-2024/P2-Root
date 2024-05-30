package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

public class FixColorsTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Root.json", customConverters = "customConverters")
    public void testFixColorsRoot(JsonParameterSet params) {
        testFixColors(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Simple.json", customConverters = "customConverters")
    public void testFixColorsSimple(JsonParameterSet params) {
        testFixColors(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Complex.json", customConverters = "customConverters")
    public void testFixColorsComplex(JsonParameterSet params) {
        testFixColors(params);
    }

    private void testFixColors(JsonParameterSet params) {

        RBTree<Integer> tree = params.get("RBTree");
        int node = params.getInt("node");
        RBTree<Integer> expectedTree = params.get("expectedRBTree");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#fixColorsAfterInsertion")
            .add("initial tree", tree.toString())
            .add("node (z)", node)
            .add("expected tree", expectedTree.toString());

        call(() -> tree.fixColorsAfterInsertion(tree.search(node)), context.build(),
            result -> "fixColorsAfterInsertion should not throw an exception");

        context.add("actual tree", tree.toString());

        assertTreeEquals(expectedTree, tree, context.build(), "The resulting tree is not correct");

    }
}
