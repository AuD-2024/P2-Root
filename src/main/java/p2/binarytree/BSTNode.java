package p2.binarytree;

/**
 * A node in a {@link BinarySearchTree}.
 *
 * @param <T> the type of the key in the node.
 *
 * @see BinarySearchTree
 * @see BinaryNode
 */
public class BSTNode<T extends Comparable<T>> extends BinaryNode<T, BSTNode<T>> {

    public BSTNode(T key) {
        super(key);
    }
}
