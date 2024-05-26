package p2.gui;

import javafx.application.Platform;
import p2.binarytree.BSTNode;
import p2.binarytree.BinarySearchTree;

import java.util.Arrays;
import java.util.function.Consumer;

public class BinarySearchTreeAnimation<T extends Comparable<T>> extends BinarySearchTree<T> implements BinaryTreeAnimation {

    private final Object syncObject = new Object();

    private BinaryTreeAnimationScene animationScene;

    public boolean animate = false;

    private final Consumer<BinarySearchTree<T>> toAnimate;
    private final Consumer<BinarySearchTree<T>> beforeAnimation;

    public BinarySearchTreeAnimation(Consumer<BinarySearchTree<T>> beforeAnimation, Consumer<BinarySearchTree<T>> toAnimate) {
        this.toAnimate = toAnimate;
        this.beforeAnimation = beforeAnimation;
    }

    public BinarySearchTreeAnimation() {
        this(tree -> {}, tree -> {});
    }

    @Override
    public void init(BinaryTreeAnimationScene animationScene) {
        this.animationScene = animationScene;
        beforeAnimation.accept(this);
    }

    @Override
    public void insert(T value) {
        animationScene.getAnimationState().setOperation("Insert(" + value + ")");
        super.insert(value);
    }

    @Override
    protected BSTNode<T> createNode(T key) {
        return new AnimatedBSTNode(key);
    }

    @Override
    public void start() {
        animate = true;
        toAnimate.accept(this);
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

    private void runWithoutAnimation(Runnable runnable) {
        animate = false;
        runnable.run();
        animate = true;
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
                        animationScene.getGraphPane().setTree(getRoot()));
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
                        animationScene.getGraphPane().setTree(getRoot()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }
        }
    }

}

