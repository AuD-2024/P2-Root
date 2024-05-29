package p2.gui;

import p2.binarytree.BinaryNode;

public interface AnimatedBinaryTree<T extends Comparable<T>> extends Animation {

    BinaryNode<T> getRoot();

    void init(BinaryTreeAnimationScene<T> animationScene);

    void turnOnAnimation();

    void turnOffAnimation();

    boolean isAnimating();

    void insert(T value);

    default void runWithoutAnimation(Runnable runnable) {
        boolean previousState = isAnimating();
        turnOffAnimation();
        runnable.run();
        if (previousState) turnOnAnimation();
    }

}
