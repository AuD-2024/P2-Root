package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class FixColorsTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Root.json", customConverters = "customConverters")
    public void testFixColorsRoot(JsonParameterSet params) {
        testFixColors(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Root.json", customConverters = "customConverters")
    public void testFixColorsRootOverride(JsonParameterSet params) {
        testFixColors(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Simple.json", customConverters = "customConverters")
    public void testFixColorsSimple(JsonParameterSet params) {
        testFixColors(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Simple.json", customConverters = "customConverters")
    public void testFixColorsSimpleOverride(JsonParameterSet params) {
        testFixColors(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Complex.json", customConverters = "customConverters")
    public void testFixColorsComplex(JsonParameterSet params) {
        testFixColors(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FixColors_Complex.json", customConverters = "customConverters")
    public void testFixColorsComplexOverride(JsonParameterSet params) {
        testFixColors(params, true);
    }

    private void testFixColors(JsonParameterSet params, boolean override) {

        TutorRBTree<Integer> tree = params.get("RBTree");
        int node = params.getInt("node");
        RBTree<Integer> expectedTree = params.get("expectedRBTree");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#fixColorsAfterInsertion")
            .add("initial tree", treeToString(tree))
            .add("node (z)", node)
            .add("expected tree", treeToString(expectedTree));

        if (override) {
            tree.overrideRotateLeft();
            tree.overrideRotateRight();
        }

        call(() -> tree.fixColorsAfterInsertion(tree.search(node)), context.build(),
            result -> "fixColorsAfterInsertion should not throw an exception");

        context.add("actual tree", tree.toString());

        assertTreeEquals(expectedTree, tree, context.build(), "The resulting tree is not correct");

    }
}
