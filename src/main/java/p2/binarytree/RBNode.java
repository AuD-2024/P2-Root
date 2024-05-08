package p2.binarytree;

@SuppressWarnings("unused")
public class RBNode<T extends Comparable<T>> extends BinaryNode<T, RBNode<T>> {

    private Color color;

    public RBNode(Color color, T key) {
        super(key);
        this.color = color;
    }

    //--- methods for color management ---//


    public Color getColor() {
        return color;
    }

    public boolean isRed() {
        return color.isRed();
    }

    public boolean isBlack() {
        return color.isBlack();
    }

    protected void setColor(Color color) {
        this.color = color;
    }

}
