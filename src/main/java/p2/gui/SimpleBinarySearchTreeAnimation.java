package p2.gui;

import javafx.application.Platform;
import p2.binarytree.BSTNode;
import p2.binarytree.SimpleBinarySearchTree;

import java.util.Arrays;

public class SimpleBinarySearchTreeAnimation<T extends Comparable<T>> extends SimpleBinarySearchTree<T> implements AnimatedBinaryTree<T> {

    private final Object syncObject = new Object();

    private BinaryTreeAnimationScene<T> animationScene;

    private boolean animate = false;
    private boolean finishWithNextStep = false;

    @Override
    public void init(BinaryTreeAnimationScene<T> animationScene) {
        this.animationScene = animationScene;
    }

    @Override
    protected BSTNode<T> createNode(T key) {
        return new AnimatedBSTNode(key);
    }

    @Override
    public void turnOnAnimation() {
        animate = true;
    }

    @Override
    public void turnOffAnimation() {
        animate = false;
    }

    @Override
    public boolean isAnimating() {
        return animate;
    }

    @Override
    public boolean isFinishingWithNextStep() {
        return finishWithNextStep;
    }

    @Override
    public void disableFinishWithNextStep() {
        finishWithNextStep = false;
    }

    @Override
    public void finishWithNextStep() {
        finishWithNextStep = true;
    }

    @Override
    public Object getSyncObject() {
        return syncObject;
    }

    private void updateState(StackTraceElement[] stackTrace, String operation) {
        animationScene.getAnimationState().setStackTrace(Arrays.stream(stackTrace)
            .filter(e -> e.getClassName().startsWith("p2"))
            .filter(e -> !e.getClassName().contains(this.getClass().getSimpleName()))
            .toArray(StackTraceElement[]::new));
        animationScene.getAnimationState().setOperation(operation);
    }

    public class AnimatedBSTNode extends BSTNode<T> {

        public AnimatedBSTNode(T key) {
            super(key);
        }

        @Override
        public BSTNode<T> getLeft() {
            BSTNode<T> left = super.getLeft();

            if (animate && left != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).getLeft()".formatted(getKey()));
                    animationScene.refresh(this, left);
                });
                waitUntilNextStep();
            }

            return left;
        }

        @Override
        protected void setLeft(BSTNode<T> left) {
            super.setLeft(left);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setLeft(%s)".formatted(getKey(), left.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, left);
                });
                waitUntilNextStep();
            }
        }

        @Override
        public BSTNode<T> getRight() {
            BSTNode<T> right = super.getRight();

            if (animate && right != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).getRight()".formatted(getKey()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }

            return right;
        }

        @Override
        protected void setRight(BSTNode<T> right) {
            super.setRight(right);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setRight(%s)".formatted(getKey(), right.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }
        }
    }

}

