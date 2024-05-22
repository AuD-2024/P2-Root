package p2.binarytree;

import p2.SearchTree;

import java.util.List;

public abstract class AbstractBinarySearchTree<T extends Comparable<T>, N extends BinaryNode<T, N>> implements SearchTree<T> {

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

    protected void insert(N node, N initialPX) {
        N x = root;
        N px = initialPX;

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

    @Override
    public void delete(T value) {

    }

    protected void inOrder(N node, List<T> result, int max) {
        if (node.hasLeft() && result.size() < max) {
            inOrder(node.getLeft(), result, max);
        }

        if (result.size() < max) result.add(node.getKey());

        if (node.hasRight() && result.size() < max) {
            inOrder(node.getRight(), result, max);
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

    public N getRoot() {
        return root;
    }

    protected abstract N createNode(T key); //TODO braucht man die noch?

}
