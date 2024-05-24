package p2;

import com.fasterxml.jackson.databind.JsonNode;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@SkipAfterFirstFailedTest
public abstract class P2_TestBase {

    @SuppressWarnings("unused")
    public static final Map<String, Function<JsonNode, ?>> customConverters = new DefaultConvertersMap(Map.ofEntries(
        Map.entry("RBTree", JSONConverters::toIntegerRBTree),
        Map.entry("BinarySearchTree", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("valid", JsonNode::asBoolean)
    ));

    public void checkVerify(Runnable verifier, Context context, String msg) {
        try {
            verifier.run();
        } catch (AssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }

    private static class DefaultConvertersMap extends HashMap<String, Function<JsonNode, ?>> {

        public DefaultConvertersMap(Map<? extends String, ? extends Function<JsonNode, ?>> m) {
            super(m);
        }

        @Override
        public Function<JsonNode, ?> get(Object key) {
            return super.getOrDefault(key, JsonNode::asInt);
        }
    }

}

