package p2.gui;

import p2.Node;
import p2.binarytree.BinaryNode;

import java.util.List;
import java.util.function.Predicate;

public interface AnimatedBinaryTree<T extends Comparable<T>> {

    BinaryNode<T> getRoot();

    void init(BinaryTreeAnimationScene<T> animationScene);

    void turnOnAnimation();

    void turnOffAnimation();

    boolean isAnimating();

    /**
     * Tells the animation to finish gracefully independent of the current state.
     */
    void finishWithNextStep();

    /**
     * Tells the animation that it does not need to finish with the next step anymore.
     */
    void disableFinishWithNextStep();

    /**
     * @return {@code true} if the animation should finish with the next step, {@code false} otherwise
     */
    boolean isFinishingWithNextStep();

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

    default void waitUntilNextStep() {
        if (!isFinishingWithNextStep()) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

}
