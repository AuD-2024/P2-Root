package p2.binarytree;

@SuppressWarnings("unused")
public class RBNode<T extends Comparable<T>> extends BinaryNode<T, RBNode<T>> {

    private Color color;

    public RBNode(T key, Color color) {
        super(key);
        this.color = color;
    }


    //--- methods for color management ---//


    public Color getColor() {
        return color;
    }

    public boolean isRed() {
        return color == Color.RED;
    }

    public boolean isBlack() {
        return color == Color.BLACK;
    }

    protected void setColor(Color color) {
        this.color = color;
    }

    protected void buildString(StringBuilder builder) {
        builder.append("[");

        if (getLeft() != null) getLeft().buildString(builder);

        builder.append(",")
            .append(getKey())
            .append(",")
            .append(color == Color.RED ? "R" : color == Color.BLACK ? "B": "null")
            .append(",");

        if (getRight() != null) getRight().buildString(builder);

        builder.append("]");
    }
}
