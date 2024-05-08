package p2.gui;

import javafx.application.Platform;
import p2.binarytree.AbstractBinarySearchTree;
import p2.binarytree.BinaryNode;

public abstract class AnimatedAbstractBST<T extends Comparable<T>, N extends AnimatedAbstractBST<T,N>.AnimatedBinaryNode> extends AbstractBinarySearchTree<T, N> implements Animation {

    protected final Object syncObject = new Object();

    private final AbstractBSTAnimationScene<T, N> animationScene;

    private final GraphPane<T, N> graphPane;

    protected AnimatedAbstractBST(AbstractBSTAnimationScene<T, N> animationScene, GraphPane<T, N> graphPane) {
        this.animationScene = animationScene;
        this.graphPane = graphPane;
    }

    @Override
    protected void insert(N node, N initialPX) {
        super.insert(node, initialPX);
        animationScene.getInfoPane().setOperation("Insert(" + node.getKey() + ")");

        waitUntilNextStep();
    }

    @Override
    public Object getSyncObject() {
        return syncObject;
    }

    public abstract class AnimatedBinaryNode extends BinaryNode<T, N> implements Animation {

        public AnimatedBinaryNode(T key) {
            super(key);
        }

        @Override
        protected void setLeft(N left) {
            super.setLeft(left);
            Platform.runLater(() -> animationScene.refresh((N) this, left));
            waitUntilNextStep();
        }

        @Override
        protected void setRight(N right) {
            super.setRight(right);
            Platform.runLater(() -> animationScene.refresh((N) this, right));
            waitUntilNextStep();
        }

        @Override
        public Object getSyncObject() {
            return syncObject;
        }
    }

}

