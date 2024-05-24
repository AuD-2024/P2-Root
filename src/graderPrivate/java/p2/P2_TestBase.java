package p2;

import com.fasterxml.jackson.databind.JsonNode;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p2.binarytree.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@SkipAfterFirstFailedTest
public abstract class P2_TestBase {

    @SuppressWarnings("unused")
    public static final Map<String, Function<JsonNode, ?>> customConverters = new DefaultConvertersMap(Map.ofEntries(
        Map.entry("RBTree", JSONConverters::toIntegerRBTree),
        Map.entry("bst", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("expectedBST", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("autocomplete", JSONConverters::toAutoComplete)
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

    public void assertTreeEquals(AbstractBinarySearchTree<?, ?> expected, AbstractBinarySearchTree<?, ?> actual, Context context) throws ReflectiveOperationException {
        assertRootCorrect(expected.getRoot(), actual.getRoot(), context);
        assertRootParentCorrect(actual, context);

        if (expected.getRoot() != null) {
            if (expected.getRoot().getLeft() != null) assertNodeCorrect(expected.getRoot().getLeft(), actual.getRoot().getLeft(), actual.getRoot(), context);
            if (expected.getRoot().getRight() != null) assertNodeCorrect(expected.getRoot().getRight(), actual.getRoot().getRight(), actual.getRoot(), context);
        }

    }

    private void assertRootCorrect(BinaryNode<?> expectedRoot, BinaryNode<?> actualRoot, Context context) {

        if (expectedRoot == null) {
            assertNull(actualRoot, context, result -> "The root of the tree should be null");
        } else {
            assertNotNull(actualRoot, context, result -> "The root of the tree should not be null");
            assertEquals(expectedRoot.getKey(), actualRoot.getKey(), context, result -> "The key of the root is not correct");
        }

        if (expectedRoot.getLeft() == null) {
            assertNull(actualRoot.getLeft(), context, result -> "The left child of the root should be null");
        } else {
            assertNotNull(actualRoot.getLeft(), context, result -> "The left child of the root should not be null");
        }

        if (expectedRoot.getRight() == null) {
            assertNull(actualRoot.getRight(), context, result -> "The right child of the root should be null");
        } else {
            assertNotNull(actualRoot.getRight(), context, result -> "The right child of the root should not be null");
        }
    }

    private void assertRootParentCorrect(AbstractBinarySearchTree<?, ?> actual, Context context) throws ReflectiveOperationException {
        if (actual instanceof RBTree<?> rbTree) {
            assertSame(getSentinel(rbTree), rbTree.getRoot().getParent(), context, result -> "The parent of the root should be the sentinel node");
        } else {
            assertNull(actual.getRoot().getParent(), context, result -> "The parent of the root should be null");
        }
    }

    private void assertNodeCorrect(BinaryNode<?> expected, BinaryNode<?> actual, BinaryNode<?> parent, Context context) {

        StringBuilder nodeDescriptionviaParent = new StringBuilder();

        if (parent.getLeft() == actual) {
            nodeDescriptionviaParent.append("left child");
        } else {
            nodeDescriptionviaParent.append("right child");
        }

        nodeDescriptionviaParent.append(" of the node with key ").append(parent.getKey());

        assertEquals(expected.getKey(), actual.getKey(), context, result -> "The key of the %s is not correct".formatted(nodeDescriptionviaParent));

        String nodeDescription = "node with key %s".formatted(actual.getKey());

        if (expected.getLeft() == null) {
            assertNull(actual.getLeft(), context, result -> "The left child of the %s should be null".formatted(nodeDescription));
        } else {
            assertNotNull(actual.getLeft(), context, result -> "The left child of the %s should not be null".formatted(nodeDescription));
            assertNodeCorrect(expected.getLeft(), actual.getLeft(), actual, context);
        }

        if (expected.getRight() == null) {
            assertNull(actual.getRight(), context, result -> "The right child of the %s should be null".formatted(nodeDescription));
        } else {
            assertNotNull(actual.getRight(), context, result -> "The right child of the %s should not be null".formatted(nodeDescription));
            assertNodeCorrect(expected.getRight(), actual.getRight(), actual, context);
        }

        assertSame(parent, actual.getParent(), context, result -> "The parent of the %s should be the node with key %s".formatted(nodeDescription, parent.getKey()));
    }

    private static BinaryNode<?> getSentinel(RBTree<?> rbTree) throws ReflectiveOperationException {
        Field sentinelField = RBTree.class.getDeclaredField("sentinel");
        sentinelField.setAccessible(true);
        return (BinaryNode<?>) sentinelField.get(rbTree);
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

