package p2.gui;

import javafx.application.Platform;
import p2.binarytree.Color;
import p2.binarytree.RBNode;
import p2.binarytree.RBTree;

import java.util.Arrays;
import java.util.function.Consumer;

public class RBTreeAnimation<T extends Comparable<T>> extends RBTree<T> implements BinaryTreeAnimation {

    private final Object syncObject = new Object();

    private BinaryTreeAnimationScene animationScene;

    public boolean animate = false;

    private final Consumer<RBTree<T>> toAnimate;
    private final Consumer<RBTree<T>> beforeAnimation;

    public RBTreeAnimation(Consumer<RBTree<T>> beforeAnimation, Consumer<RBTree<T>> toAnimate) {
        this.toAnimate = toAnimate;
        this.beforeAnimation = beforeAnimation;
    }

    public RBTreeAnimation() {
        this(tree -> {}, tree -> {});
    }

    @Override
    public void init(BinaryTreeAnimationScene animationScene) {
        this.animationScene = animationScene;
        beforeAnimation.accept(this);
    }

    @Override
    protected void insert(RBNode<T> node, RBNode<T> initialPX) {
        animationScene.getAnimationState().setOperation("Insert(" + node.getKey() + ")");
        super.insert(node, initialPX);
    }

    @Override
    protected RBNode<T> createNode(T key) {
        return new AnimatedRBNode(key, Color.RED);
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
                    updateState(stackTrace, "(%s).setLeft(%s)".formatted(getKey(), left.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getGraphPane().setTree(getRoot()));
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
                    updateState(stackTrace, "(%s).setRight(%s)".formatted(getKey(), right.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getGraphPane().setTree(getRoot()));
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
                    javafx.scene.paint.Color fxColor = color == Color.RED ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.BLACK; //TODO how to handle null color?
                    animationScene.getGraphPane().setNodeFillColor(this, fxColor);
                    animationScene.getGraphPane().setNodeStrokeColor(this, fxColor);
                });
                waitUntilNextStep();
            }
        }

    }

}
