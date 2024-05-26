package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.RBTree;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

public class RotateTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "RotateLeft.json", customConverters = "customConverters")
    public void testRotateLeft(JsonParameterSet params) throws ReflectiveOperationException {
        testRotate(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RotateRight.json", customConverters = "customConverters")
    public void testRotateRight(JsonParameterSet params) throws ReflectiveOperationException {
        testRotate(params, false);
    }

    private void testRotate(JsonParameterSet params, boolean rotateLeft) throws ReflectiveOperationException {

        RBTree<Integer> tree = params.get("RBTree");
        int node = params.getInt("node");
        RBTree<Integer> expected = params.get("expectedRBTree");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#" + (rotateLeft ? "rotateLeft" : "rotateRight"))
            .add("initial tree", tree.toString())
            .add("rotated node", node)
            .add("expected tree", expected.toString());

        if (rotateLeft) {
            call(() -> invokeRotateLeft(tree, tree.search(node)), context.build(), result  -> "rotateLeft should not throw an exception");
        } else {
            call(() -> invokeRotateRight(tree, tree.search(node)), context.build(), result -> "rotateRight should not throw an exception");
        }

        context.add("actual tree", tree);

        assertTreeEquals(expected, tree, context.build());
    }
}
