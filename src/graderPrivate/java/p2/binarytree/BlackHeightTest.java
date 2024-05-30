package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class BlackHeightTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "BlackHeight.json", customConverters = "customConverters")
    public void testBlackHeight(JsonParameterSet params) {

        RBTree<Integer> tree = params.get("RBTree");
        int expected = params.getInt("blackHeight");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#blackHeight")
            .add("rb tree", tree.toString())
            .add("expected blackHeight", expected);

        int actual = callObject(tree::blackHeight, context.build(), result -> "blackHeight should not throw an exception");

        context.add("actual blackHeight", actual);

        assertEquals(expected, actual, context.build(),
            result -> "The method blackHeight() did not return the correct value");
    }

}
