package p2.binarytree;

import p2.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RBTree<T extends Comparable<T>> extends AbstractBinarySearchTree<T, RBNode<T>> {

    protected final RBNode<T> sentinel = new RBNode<>(null, Color.BLACK);

    public RBTree() {
        sentinel.setParent(sentinel);
        sentinel.setLeft(sentinel);
        sentinel.setRight(sentinel);
    }

    @Override
    public void insert(T value) {
        RBNode<T> z = new RBNode<>(value, Color.RED);
        insert(z, sentinel);
        fixColorsAfterInsertion(z);
    }

    private void fixColorsAfterInsertion(RBNode<T> z) {

        while (z.getParent().isRed()) {
            if (z.getParent() == z.getParent().getParent().getLeft()) {
                RBNode<T> y = z.getParent().getParent().getRight();
                if (y != null && y.isRed()) {
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getRight()) {
                        z = z.getParent();
                        rotateLeft(z);
                    }
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    rotateRight(z.getParent().getParent());
                }
            } else {
                RBNode<T> y = z.getParent().getParent().getLeft();
                if (y != null && y.isRed()) {
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeft()) {
                        z = z.getParent();
                        rotateRight(z);
                    }
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    rotateLeft(z.getParent().getParent());
                }
            }
        }

        root.setColor(Color.BLACK);
    }

    private void rotateLeft(RBNode<T> x) {

        RBNode<T> y = x.getRight();
        x.setRight(y.getLeft());

        if (y.hasLeft()) {
            y.getLeft().setParent(x);
        }

        y.setParent(x.getParent());

        if (x.getParent() == sentinel) {
            root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }

        y.setLeft(x);
        x.setParent(y);
    }

    private void rotateRight(RBNode<T> y) {

        RBNode<T> x = y.getLeft();
        y.setLeft(x.getRight());

        if (x.hasRight()) {
            x.getRight().setParent(y);
        }

        x.setParent(y.getParent());

        if (y.getParent() == sentinel) {
            root = x;
        } else if (y == y.getParent().getRight()) {
            y.getParent().setRight(x);
        } else {
            y.getParent().setLeft(x);
        }

        x.setRight(y);
        y.setParent(x);
    }

    @Override
    public void delete(T value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void inOrder(Node<T> node, List<T> result, int max, Predicate<T> predicate) {
        if (node instanceof RBNode<T> rbNode) {
            super.inOrder(rbNode, result, max, predicate);
        }
        throw new IllegalArgumentException("Node must be of type RBNode");
    }

    public void join(RBTree<T> other, T joinKey) {

        int leftBH = blackHeight();
        int rightBH = other.blackHeight();

        int minBH = Math.min(leftBH, rightBH);
        boolean useLeftRoot = leftBH <= rightBH;

        RBNode<T> newNode = new RBNode<>(joinKey, Color.RED);

        RBNode<T> leftNode = findBlackNodeWithBlackHeight(minBH, leftBH, false);
        RBNode<T> rightNode = other.findBlackNodeWithBlackHeight(minBH, rightBH, true);

        newNode.setLeft(leftNode);
        newNode.setRight(rightNode);

        if (leftBH == rightBH) {
            root = newNode;
            newNode.setParent(sentinel);
        } else {
            RBNode<T> parent = useLeftRoot ? rightNode.getParent() : leftNode.getParent();

            newNode.setParent(parent);

            if (parent.getLeft() == leftNode || parent.getLeft() == rightNode) parent.setLeft(newNode);
            else parent.setRight(newNode);

            if (useLeftRoot) {
                root = other.root;
                root.setParent(sentinel);
            }
        }

        leftNode.setParent(newNode);
        rightNode.setParent(newNode);

        fixColorsAfterInsertion(newNode);
    }

    int blackHeight() {
        return blackHeight(root);
    }

    private int blackHeight(RBNode<T> node) {
        if (node == null) return 0;

        return blackHeight(node.getLeft()) + (node.isBlack() ? 1 : 0);
    }

    RBNode<T> findBlackNodeWithBlackHeight(int targetBlackHeight, int totalBlackHeight, boolean smallest) {

        int currentBlackHeight = totalBlackHeight;
        RBNode<T> currentNode = root;

        while (currentNode.isRed() || currentBlackHeight > targetBlackHeight) {

            if (currentNode.isBlack()) currentBlackHeight--;

            if (smallest) {
                if (currentNode.hasLeft()) currentNode = currentNode.getLeft();
                else currentNode = currentNode.getRight();
            } else {
                if (currentNode.hasRight()) currentNode = currentNode.getRight();
                else currentNode = currentNode.getLeft();
            }
        }

        return currentNode;
    }

    @Override
    public void findNext(Node<T> node, List<T> result, int max, Predicate<T> predicate) {
        if (node instanceof RBNode<T> rbNode) {
            super.findNext(rbNode, result, max, predicate);
        }
        throw new IllegalArgumentException("Node must be of type RBNode");
    }


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
