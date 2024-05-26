package p2;

import com.fasterxml.jackson.databind.JsonNode;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import p2.binarytree.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@SkipAfterFirstFailedTest
public abstract class P2_TestBase {

    @SuppressWarnings("unused")
    public static final Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("RBTree", JSONConverters::toIntegerRBTree),
        Map.entry("bst", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("expectedBST", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("autocomplete", JSONConverters::toAutoComplete),
        Map.entry("expectedList", JSONConverters::toIntegerList)
    );

    void testForBSTAndRBTree(JsonParameterSet params, ThrowingBiConsumer<SearchTree<Integer>, String> test) throws Throwable {

        boolean testPerformed = false;

        if (params.getRootNode().has("bst")) {

            test.accept(params.get("bst"), "BinarySearchTree");
            testPerformed = true;
        }
        if (params.getRootNode().has("RBTree")) {
            test.accept(params.get("RBTree"), "RBTree");
            testPerformed = true;
        }

        if (!testPerformed) {
            throw new IllegalArgumentException("Internal error: No tree found in the parameter set");
        }
    }

    public void checkVerify(Runnable verifier, Context context, String msg) {
        try {
            verifier.run();
        } catch (AssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }

    //TODO funktioniert wirklich (wird wirklich neuer Baum erstellt?)
    public void assertTreeUnchanged(RBTree<?> expected, RBTree<?> actual, Context context) throws ReflectiveOperationException {
        if (expected.getRoot() != null) assertNodeUnchanged(expected.getRoot(), actual.getRoot(), getSentinel(actual), context);
    }

    public void assertTreeUnchanged(BinarySearchTree<?> expected, BinarySearchTree<?> actual, Context context) {
        if (expected.getRoot() != null) assertNodeUnchanged(expected.getRoot(), actual.getRoot(), null, context);
    }

    public void assertNodeUnchanged(BinaryNode<?> expected, BinaryNode<?> actual, BinaryNode<?> parent, Context context) {

        assertNotNull(actual, context, result -> "The tree should not change. The node with key %s should not be null".formatted(expected.getKey()));

        assertEquals(expected.getKey(), actual.getKey(), context, result -> "The tree should not change. The key of the node with key %s is not correct".formatted(expected.getKey()));

        if (expected.getLeft() != null) assertNodeUnchanged(expected.getLeft(), actual.getLeft(), actual, context);
        else assertNull(actual.getLeft(), context, result -> "The tree should not change. The left child of the node with key %s should be null".formatted(expected.getKey()));

        if (expected.getRight() != null) assertNodeUnchanged(expected.getRight(), actual.getRight(), actual, context);
        else assertNull(actual.getRight(), context, result -> "The tree should not change. The right child of the node with key %s should be null".formatted(expected.getKey()));

        assertSame(parent, actual.getParent(), context, result -> "The tree should not change. The parent of the node with key %s should be the node with key %s".formatted(expected.getKey(), parent.getKey()));

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

    public static AbstractBinaryNode<?, ?> getSentinel(RBTree<?> rbTree) throws ReflectiveOperationException {
        Field sentinelField = RBTree.class.getDeclaredField("sentinel");
        sentinelField.setAccessible(true);
        return (AbstractBinaryNode<?, ?>) sentinelField.get(rbTree);
    }

    public static AbstractBinaryNode<?, ?> invokeCreateNode(AbstractBinarySearchTree<?, ?> tree, Comparable<?> key) throws ReflectiveOperationException {
        Method createNode = AbstractBinarySearchTree.class.getDeclaredMethod("createNode", Comparable.class);
        createNode.setAccessible(true);
        return (AbstractBinaryNode<?, ?>) createNode.invoke(tree, key);
    }

    public static void invokeInsert(AbstractBinarySearchTree<?, ?> tree, AbstractBinaryNode<?, ?> node, AbstractBinaryNode<?, ?> initialPX) throws ReflectiveOperationException {
        Method insert = AbstractBinarySearchTree.class.getDeclaredMethod("insert", AbstractBinaryNode.class, AbstractBinaryNode.class);
        insert.setAccessible(true);
        insert.invoke(tree, node, initialPX);
    }

}

@FunctionalInterface
interface ThrowingBiConsumer<T, U> {

    void accept(T t, U u) throws Throwable;

}
