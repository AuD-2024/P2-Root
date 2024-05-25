package p2.binarytree;

import p2.SearchTree;

import java.util.List;
import java.util.function.Predicate;

/**
 * A base implementation of a binary search tree containing common methods.
 * <p>
 * It contains the root node of the tree and provides methods for searching and inserting elements.
 * <p>
 * It assumes that only binary nodes are used, i.e. every node contains exactly one key and has at most two children,
 * where the left child is smaller than the parent and the right child is greater than the parent.
 *
 * @param <T> the type of the keys in the tree.
 * @param <N> the type of the nodes in the tree.
 *
 * @see SearchTree
 * @see AbstractBinaryNode
 */
public abstract class AbstractBinarySearchTree<T extends Comparable<T>, N extends AbstractBinaryNode<T, N>> implements SearchTree<T> {

    /**
     * The root node of the tree.
     */
    protected N root;

    @Override
    public N search(T value) {

        N x = root;

        while (x != null && x.getKey().compareTo(value) != 0) {
            if (x.getKey().compareTo(value) > 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        return x;
    }

    /**
     * Inserts the given node into the tree.
     *
     * @param node the node to insert.
     * @param initialPX The initial value used for the pointer to the parent node, i.e., the parent of the root node.
     *                  This is required for implementations that use a sentinel node. For normal trees, this value
     *                  should be {@code null}.
     */
    protected void insert(N node, N initialPX) {
        N x = root;
        N px = initialPX; //TODO mit root.getParent() austauschen?

        while (x != null) {
            px = x;
            if (x.getKey().compareTo(node.getKey()) > 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        node.setParent(px);
        if (px == initialPX) {
            root = node;
        } else if (px.getKey().compareTo(node.getKey()) > 0) {
            px.setLeft(node);
        } else {
            px.setRight(node);
        }
    }

    /**
     * Adds all elements in the subtree represented by the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * If later elements exist that satisfy the predicate, they are excluded as well.
     *
     * @param node The root of the subtree to traverse.
     * @param result The list to store the elements in.
     * @param max The maximum number of elements to include in the result.
     * @param predicate The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *                  the traversal stops.
     */
    protected void inOrder(N node, List<T> result, int max, Predicate<T> predicate) {
        if (node.hasLeft() && result.size() < max) {
            inOrder(node.getLeft(), result, max, predicate);
        }

        if (!predicate.test(node.getKey())) return;

        if (result.size() < max) result.add(node.getKey());

        if (node.hasRight() && result.size() < max) {
            inOrder(node.getRight(), result, max, predicate);
        }
    }

    /**
     * Adds all elements in the tree that are greater than or equal to the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * If later elements exist that satisfy the predicate, they are excluded as well.
     *
     * @param node The node to start the search from. The node itself is included in the search.
     * @param result The list to store the elements in.
     * @param max The maximum number of elements to include in the result.
     * @param predicate The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *                  the traversal stops.
     */
    protected void findNext(N node, List<T> result, int max, Predicate<T> predicate) {
        findNext(node, null, max, result, predicate);
    }

    private void findNext(N node, N prev, int max, List<T> result, Predicate<T> predicate) {

        if ((prev == null || node.getRight() != prev) && result.size() < max) {

            if (node == null || !predicate.test(node.getKey())) return;

            result.add(node.getKey());
        }

        if (node.hasRight() && prev != node.getRight() && result.size() < max) {
            inOrder(node.getRight(), result, max, predicate);
        }

        if (result.size() < max && node != root) {
            findNext(node.getParent(), node, max, result, predicate);
        }
    }

    @Override
    public N findSmallest() {
        N x = root;
        while (x.hasLeft()) {
            x = x.getLeft();
        }
        return x;
    }

    @Override
    public N getRoot() {
        return root;
    }

    /**
     * Creates a new node with the given key.
     * <p>
     * The type of the node is determined by the concrete implementation. If the implementation uses additional
     * information within the node, a standard value is used for them, e.g., red for the color of a node in a
     * red-black tree.
     *
     * @param key the key of the new node.
     * @return a new node with the given key.
     */
    protected abstract N createNode(T key);

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (root == null) {
            sb.append("[]");
        } else {
            root.buildString(sb);
        }

        return sb.toString();
    }

}
