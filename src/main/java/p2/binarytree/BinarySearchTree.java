package p2.binarytree;

import p2.Node;

import java.util.List;
import java.util.function.Predicate;

public class BinarySearchTree<T extends Comparable<T>> extends AbstractBinarySearchTree<T, BSTNode<T>> {

    @Override
    public void insert(T value) {
        insert(new BSTNode<>(value), null);
    }

    @Override
    public void inOrder(Node<T> node, List<T> result, int max, Predicate<T> predicate) {
        if (node instanceof BSTNode<T> bstNode) {
            super.inOrder(bstNode, result, max, predicate);
        }
        throw new IllegalArgumentException("Node must be of type BSTNode");
    }

    @Override
    public void findNext(Node<T> node, List<T> result, int max, Predicate<T> predicate) {
        if (node instanceof BSTNode<T> rbNode) {
            super.findNext(rbNode, result, max, predicate);
        }
        throw new IllegalArgumentException("Node must be of type BSTNode");
    }

}
