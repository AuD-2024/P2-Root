package p2.binarytree;

import p2.Node;

/**
 * A base implementation for a node in a {@link AbstractBinarySearchTree}.
 * <p>
 * It contains exactly one key and has at most two children, where the left child is smaller than the parent and the
 * right child is greater than the parent. It also contains a reference to the parent node.
 * <p>
 * The key can be of any type that implements the {@link Comparable} interface.
 *
 * @param <T> the type of the key in the node.
 * @param <N> the concrete type of the node, e.g. {@link BSTNode} or {@link RBNode}.
 *
 * @see Node
 * @see AbstractBinarySearchTree
 */
public abstract class BinaryNode<T extends Comparable<T>, N extends BinaryNode<T, N>> implements Node<T> {

    /**
     * The key of the node.
     */
    private final T key;

    /**
     * The left child of the node. Can be {@code null} if the node has no left child.
     */
    private N left;

    /**
     * The right child of the node. Can be {@code null} if the node has no right child.
     */
    private N right;

    /**
     * The parent of the node. Can be {@code null} if the node has no parent.
     */
    private N parent;

    /**
     * Creates a new node with the given key.
     * @param key the key of the node.
     */
    public BinaryNode(T key) {
        this.key = key;
    }


    //--- methods for key extraction ---//


    /**
     * Returns the key of the node.
     *
     * @return the key of the node.
     */
    public T getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getKeys() {
        return (T[]) new Comparable[]{key};
    }


    //--- methods for left child management ---//


    /**
     * Returns the left child of the node.
     * If the node has no left child, {@code null} is returned.
     *
     * @return the left child of the node.
     */
    public N getLeft() {
        return left;
    }

    /**
     * Checks whether the node has a left child.
     *
     * @return {@code true} if the node has a left child, {@code false} otherwise.
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Sets the left child of the node to the given node.
     *
     * @param left the new left child of the node.
     */
    protected void setLeft(N left) {
        this.left = left;
    }


    //--- methods for right child management ---//


    /**
     * Returns the right child of the node.
     * If the node has no right child, {@code null} is returned.
     *
     * @return the right child of the node.
     */
    public N getRight() {
        return right;
    }

    /**
     * Checks whether the node has a right child.
     *
     * @return {@code true} if the node has a right child, {@code false} otherwise.
     */
    public boolean hasRight() {
        return right != null;
    }

    /**
     * Sets the right child of the node to the given node.
     *
     * @param right the new right child of the node.
     */
    protected void setRight(N right) {
        this.right = right;
    }


    //--- methods for parent management ---//


    /**
     * Returns the parent of the node.
     * If the node has no parent, {@code null} is returned.
     *
     * @return the parent of the node.
     */
    public N getParent() {
        return parent;
    }

    /**
     * Sets the parent of the node to the given node.
     *
     * @param parent the new parent of the node.
     */
    protected void setParent(N parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "BinaryNode{" +
            "key=" + key +
            '}';
    }

    /**
     * Creates a string representation of the node and its children and appends it to the given builder.
     * It has the format {@code [left,key,right]}, where {@code left} and {@code right} are the string
     * representations of the left and right children, respectively. If a child is {@code null}, it is represented
     * by an empty string. Additional information can be appended by overriding this method.
     *
     * @param builder the builder to append the string representation to.
     */
    protected void buildString(StringBuilder builder) {
        builder.append("[");

        if (getLeft() != null) getLeft().buildString(builder);

        builder.append(",").append(getKey()).append(",");

        if (getRight() != null) getRight().buildString(builder);

        builder.append("]");
    }
}
