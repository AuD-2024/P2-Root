package p2.binarytree;

import p2.SearchTree;

import java.util.List;
import java.util.function.Predicate;

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

    protected void findNext(N node, List<T> result, int max, Predicate<T> predicate) {
        findNext(node, null, max, result, predicate);
    }

    protected void findNext(N node, N prev, int max, List<T> result, Predicate<T> predicate) {

        if ((prev == null || node.getRight() != prev) && result.size() < max) {

            if (node == null || !predicate.test(node.getKey())) return;

            result.add(node.getKey());
        }

        if (node.hasRight() && prev != node.getRight() && result.size() < max) {
            inOrder(node.getRight(), result, max, predicate);
        }

        if (result.size() < max) {
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

    public N getRoot() {
        return root;
    }

}
