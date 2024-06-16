package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class FindBlackNodeTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Simple.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeSimple(JsonParameterSet params) {
        testFindBlackNode(params, true, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Simple.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeSimpleOverride(JsonParameterSet params) {
        testFindBlackNode(params, true, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Complex.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeComplex(JsonParameterSet params) {
        testFindBlackNode(params, true, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Complex.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeComplexOverride(JsonParameterSet params) {
        testFindBlackNode(params, true, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Greatest.json", customConverters = "customConverters")
    public void testFindGreatestBlackNode(JsonParameterSet params) {
        testFindBlackNode(params, false, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Greatest.json", customConverters = "customConverters")
    public void testFindGreatestBlackNodeOverride(JsonParameterSet params) {
        testFindBlackNode(params, false, true);
    }

    private void testFindBlackNode(JsonParameterSet params, boolean findSmallest, boolean override) {
        TutorRBTree<Integer> tree = params.get("RBTree");
        int totalBlackHeight = params.getInt("totalBlackHeight");
        int targetBlackHeight = params.getInt("targetBlackHeight");
        int expected = params.get("expectedNode");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#findBlackNodeWithBlackHeight")
            .add("rb tree", treeToString(tree))
            .add("target black height", targetBlackHeight)
            .add("total black height", totalBlackHeight)
            .add("find smallest", findSmallest)
            .add("expected node", expected);

        if (override) {
            tree.overrideBlackHeight();
        }

        RBNode<Integer> actual = callObject(() -> tree.findBlackNodeWithBlackHeight(targetBlackHeight, totalBlackHeight, findSmallest),
            context.build(), result -> "findBlackNodeWithBlackHeight should not throw an exception");

        assertNotNull(actual, context.build(), result -> "The returned node is null");

        context.add("actual node", actual.getKey());

        assertEquals(expected, actual.getKey(), context.build(), result -> "The key of the returned node is not correct");
    }

}
