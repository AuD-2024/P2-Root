package p2.gui;

import javafx.application.Platform;
import p2.binarytree.Color;
import p2.binarytree.RBNode;
import p2.binarytree.RBTree;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class RBTreeAnimation<T extends Comparable<T>> extends RBTree<T> implements AnimatedBinaryTree<T> {

    private final Object syncObject = new Object();

    private BinaryTreeAnimationScene<T> animationScene;

    private boolean animate = false;
    private boolean finishWithNextStep = false;

    @Override
    public void init(BinaryTreeAnimationScene<T> animationScene) {
        this.animationScene = animationScene;
    }

    @Override
    protected RBNode<T> createNode(T key) {
        return new AnimatedRBNode(key, Color.RED);
    }

    @Override
    public String toString() {
        AtomicReference<String> result = new AtomicReference<>();
        runWithoutAnimation(() -> result.set(super.toString()));
        return result.get();
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

    public class AnimatedRBNode extends RBNode<T> {

        public AnimatedRBNode(T key, Color color) {
            super(key, color);
        }

        @Override
        public RBNode<T> getLeft() {
            RBNode<T> left = super.getLeft();

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
        protected void setLeft(RBNode<T> left) {
            super.setLeft(left);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setLeft(%s)".formatted(getKey(), left == null ? "null" : left.getKey()));
                    runWithoutAnimation(() -> animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, left);
                });
                waitUntilNextStep();
            }
        }

        @Override
        public RBNode<T> getRight() {
            RBNode<T> right = super.getRight();

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
        protected void setRight(RBNode<T> right) {
            super.setRight(right);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setRight(%s)".formatted(getKey(), right == null ? "null" : right.getKey()));
                    runWithoutAnimation(() -> animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setColor(%s)".formatted(getKey(), color));
                    runWithoutAnimation(() -> animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this);
                });
                waitUntilNextStep();
            }
        }

        @Override
        protected void buildString(StringBuilder builder) {
            runWithoutAnimation(() -> super.buildString(builder));
        }
    }

}
