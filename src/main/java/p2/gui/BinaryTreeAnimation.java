package p2.gui;

import p2.binarytree.BinaryNode;

public interface BinaryTreeAnimation extends Animation {

    BinaryNode<?, ?> getRoot();

    void init(BinaryTreeAnimationScene animationScene);

}
