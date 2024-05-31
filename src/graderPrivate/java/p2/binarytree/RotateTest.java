package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class RotateTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "RotateLeft.json", customConverters = "customConverters")
    public void testRotateLeft(JsonParameterSet params) {
        testRotate(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RotateRight.json", customConverters = "customConverters")
    public void testRotateRight(JsonParameterSet params) {
        testRotate(params, false);
    }

    private void testRotate(JsonParameterSet params, boolean rotateLeft) {

        RBTree<Integer> tree = params.get("RBTree");
        int node = params.getInt("node");
        RBTree<Integer> expected = params.get("expectedRBTree");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#" + (rotateLeft ? "rotateLeft" : "rotateRight"))
            .add("initial tree", tree.toString())
            .add("rotated node", node)
            .add("expected tree", expected.toString());

        if (rotateLeft) {
            call(() -> tree.rotateLeft(tree.search(node)), context.build(), result -> "rotateLeft should not throw an exception");
        } else {
            call(() -> tree.rotateRight(tree.search(node)), context.build(), result -> "rotateRight should not throw an exception");
        }

        context.add("actual tree", tree);

        assertTreeEquals(expected, tree, context.build(), "The resulting tree is not correct");
    }
}
