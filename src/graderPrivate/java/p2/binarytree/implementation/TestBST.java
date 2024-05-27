package p2.binarytree.implementation;

import p2.binarytree.BSTNode;
import p2.binarytree.BinarySearchTree;

public class TestBST<T extends Comparable<T>> extends BinarySearchTree<T> {

    @Override
    protected BSTNode<T> createNode(T key) {
        return new TestBSTNode<>(key);
    }
}
