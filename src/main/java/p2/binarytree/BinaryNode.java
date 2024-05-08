package p2.binarytree;

import p2.Node;

public abstract class BinaryNode<T extends Comparable<T>, N extends BinaryNode<T, N>> implements Node<T> {

    private final T key;

    private N left;

    private N right;

    private N parent;

    public BinaryNode(T key) {
        this.key = key;
    }


    //--- methods for key extraction ---//


    public T getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getKeys() {
        return (T[]) new Comparable[]{key};
    }


    //--- methods for left child management ---//


    public N getLeft() {
        return left;
    }

    public boolean hasLeft() {
        return left != null;
    }

    protected void setLeft(N left) {
        this.left = left;
    }


    //--- methods for right child management ---//


    public N getRight() {
        return right;
    }

    public boolean hasRight() {
        return right != null;
    }

    protected void setRight(N right) {
        this.right = right;
    }


    //--- methods for parent management ---//


    public N getParent() {
        return parent;
    }

    protected void setParent(N parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "BinaryNode{" +
            "key=" + key +
            '}';
    }
}
