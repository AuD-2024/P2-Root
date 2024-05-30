package p2.gui;

import p2.Node;
import p2.binarytree.BinaryNode;

import java.util.List;
import java.util.function.Predicate;

public interface AnimatedBinaryTree<T extends Comparable<T>> extends Animation {

    BinaryNode<T> getRoot();

    void init(BinaryTreeAnimationScene<T> animationScene);

    void turnOnAnimation();

    void turnOffAnimation();

    boolean isAnimating();

    BinaryNode<T> search(T value);

    void insert(T value);

    void inOrder(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate);

    void findNext(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate);

    default void runWithoutAnimation(Runnable runnable) {
        boolean previousState = isAnimating();
        turnOffAnimation();
        runnable.run();
        if (previousState) turnOnAnimation();
    }

}
