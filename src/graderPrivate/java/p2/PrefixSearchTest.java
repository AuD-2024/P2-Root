package p2;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.AutoComplete;
import p2.binarytree.RBNode;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

public class PrefixSearchTest extends P2_TestBase {


    @ParameterizedTest
    @JsonParameterSetTest(value = "PrefixSearch_Simple.json", customConverters = "customConverters")
    public void testPrefixSimple(JsonParameterSet params) throws ReflectiveOperationException {
        testPrefixSearch(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "PrefixSearch_Complex.json", customConverters = "customConverters")
    public void testPrefixComplex(JsonParameterSet params) throws ReflectiveOperationException {
        testPrefixSearch(params);
    }

    private void testPrefixSearch(JsonParameterSet params) throws ReflectiveOperationException {

        AutoComplete autoComplete = params.get("autocomplete");
        String prefix = params.getString("prefix");
        String expectedKey = params.getString("expectedKey");

        Context.Builder<?> context = contextBuilder()
            .subject("AutoComplete#prefixSearch")
            .add("rbTree", autoComplete.toString())
            .add("prefix", prefix)
            .add("expected key", expectedKey);

        RBNode<String> actual = callObject(() -> autoComplete.prefixSearch(prefix), context.build(),
            result -> "prefixSearch should not throw an exception");

        context.add("actual key", actual == null ? "null" : actual.getKey());

        if (expectedKey == null) {
            assertNull(actual, context.build(), result -> "The method prefixSearch should return null");
        } else {
            assertNotNull(actual, context.build(), result -> "The method prefixSearch should not return null");

            assertEquals(expectedKey, actual.getKey(), context.build(),
                result -> "The method prefixSearch did not return a node with the correct key");
        }

        assertTreeUnchanged(params.get("autocomplete"), autoComplete, context.build());
    }

}
