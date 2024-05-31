package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.callObject;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class PrefixSearchTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "PrefixSearch_Simple.json", customConverters = "customConverters")
    public void testPrefixSearchSimple(JsonParameterSet params) {
        testPrefixSearch(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "PrefixSearch_Complex.json", customConverters = "customConverters")
    public void testPrefixSearchComplex(JsonParameterSet params) {
        testPrefixSearch(params);
    }

    private void testPrefixSearch(JsonParameterSet params) {

        AutoComplete autoComplete = params.get("autocomplete");
        String prefix = params.getString("prefix");
        String expectedKey = params.getString("expectedKey");

        Context.Builder<?> context = contextBuilder()
            .subject("AutoComplete#prefixSearch")
            .add("rbTree", autoComplete.toString())
            .add("prefix", prefix)
            .add("expected key", expectedKey);

        BinaryNode<String> actual = callObject(() -> autoComplete.prefixSearch(prefix), context.build(),
            result -> "prefixSearch should not throw an exception");

        context.add("actual key", actual == null ? "null" : actual.getKey());

        if (expectedKey == null) {
            assertNull(actual, context.build(), result -> "The method prefixSearch should return null");
        } else {
            assertNotNull(actual, context.build(), result -> "The method prefixSearch should not return null");

            assert actual != null;
            assertEquals(expectedKey, actual.getKey(), context.build(),
                result -> "The method prefixSearch did not return a node with the correct key");
        }

        assertTreeUnchanged((RBTree<String>) params.<AutoComplete>get("autocomplete").getSearchTree(),
            (RBTree<String>) autoComplete.getSearchTree(), context.build());
    }

}
