package p2.binarytree;

import p2.Node;

import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> extends AbstractBinarySearchTree<T, BSTNode<T>> {

    @Override
    public void insert(T value) {
        insert(createNode(value), null);
    }

    @Override
    public void inOrder(Node<T> node, List<T> result, int max) {
        if (node instanceof BSTNode<T> bstNode) {
            super.inOrder(bstNode, result, max);
        }
        throw new IllegalArgumentException("Node must be of type BSTNode");
    }

    @Override
    protected BSTNode<T> createNode(T key) {
        return new BSTNode<>(key);
    }
}
