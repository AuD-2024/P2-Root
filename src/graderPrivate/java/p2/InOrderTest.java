package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

public class InOrderTest extends TraversingTest {

    @ParameterizedTest
    @JsonParameterSetTest(value = "InOrder_Simple.json", customConverters = "customConverters")
    public void testInOrderSimple(JsonParameterSet params) {
        testTraversing(params, SearchTree::inOrder, "inOrder");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "InOrder_Complex.json", customConverters = "customConverters")
    public void testInOrderComplex(JsonParameterSet params) {
        testTraversing(params, SearchTree::inOrder, "inOrder");
    }



}
