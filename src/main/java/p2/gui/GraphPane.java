package p2.gui;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import p2.binarytree.BinaryNode;
import p2.binarytree.RBNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static p2.gui.GraphStyle.*;

/**
 * A {@link Pane} that displays a {@link p2.binarytree.AbstractBinarySearchTree}.
 */
public class GraphPane extends Pane {

    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;
    private static final double MAX_SCALE = 100;
    private static final double MIN_SCALE = 3;

    private final AtomicReference<Point2D> lastPoint = new AtomicReference<>();
    private Affine transformation = new Affine();

    private final Text positionText = new Text();

    private final Map<BinaryNode<?>, LabeledNode> nodes = new HashMap<>();
    private final Map<BinaryNode<?>, Location> nodeLocations = new HashMap<>();

    private final Map<Edge, Line> edges = new HashMap<>();
    private final Map<BinaryNode<?>, Map<BinaryNode<?>, Edge>> nodesToEdge = new HashMap<>();

    private final List<Line> grid = new ArrayList<>();

    private boolean alreadyCentered = false;

    /**
     * Creates a new, empty {@link GraphPane}.
     */
    public GraphPane() {
        this(null);
    }

    public GraphPane(BinaryNode<?> root) {
        // avoid division by zero when scale = 1
        transformation.appendScale(MIN_SCALE, MIN_SCALE);

        initListeners();
        drawGrid();
        drawPositionText();
        positionText.setFill(TEXT_COLOR);

        setTree(root);
        center();
    }

    public void setTree(BinaryNode<?> root) {
        clear();
        Map<BinaryNode<?>, Double> xOffsets = new HashMap<>();
        calculateXOffsets(root, xOffsets, 0);

        addTree(root, 0, xOffsets);

        for (LabeledNode node : nodes.values()) {
            node.ellipse().toFront();
            node.text().toFront();
        }

        redrawMap();
    }

    private void addTree(BinaryNode<?> root, int depth, Map<BinaryNode<?>, Double> xOffsets) {
        if (root == null) {
            return;
        }

        Location rootLocation = new Location(xOffsets.get(root), depth * NODE_VERTICAL_DISTANCE);
        addNode(root, rootLocation);

        if (root.hasLeft()) {
            addTree(root.getLeft(), depth + 1, xOffsets);
            addEdge(root, root.getLeft());
        }

        if (root.hasRight()) {
            addTree(root.getRight(), depth + 1, xOffsets);
            addEdge(root, root.getRight());
        }
    }

    // --- Edge Handling --- //

    /**
     * Adds an {@linkplain Edge edge} to this {@link GraphPane} and displays it.
     */
    public void addEdge(BinaryNode<?> source, BinaryNode<?> target) {

        Edge edge = new Edge(source, target);

        edges.put(edge, drawEdge(source, target));
        nodesToEdge.computeIfAbsent(source, k -> new HashMap<>()).put(target, edge);
    }

    /**
     * Removes the given {@linkplain Edge edge} from this {@link GraphPane}.
     * The {@linkplain Edge edge} will not be displayed after anymore calling this method.
     * If the given {@linkplain Edge edge} is not part of this {@linkplain GraphPane} the method does nothing.
     */
    public void removeEdge(BinaryNode<?> source, BinaryNode<?> target) {
        Line line = edges.remove(getEdge(source, target));
        nodesToEdge.get(source).remove(target);

        if (line != null) {
            getChildren().remove(line);
        }
    }

    /**
     * Updates the color used to draw the given {@linkplain Edge edge}.
     *
     * @param color The new color.
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link GraphPane}.
     */
    public void setEdgeColor(BinaryNode<?> source, BinaryNode<?> target, Color color) {
        getEdgeLine(source, target).setStroke(color);
    }

    /**
     * Updates the dashing settings of the stroke used to draw the given {@linkplain Edge edge}.
     *
     * @param dash       Whether to use dashing or not.
     * @param dashLength The length of the individual dashes.
     * @param gapLength  The length of the gaps between the dashes.
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link GraphPane}.
     */
    public void setEdgeDash(BinaryNode<?> source, BinaryNode<?> target, boolean dash, double dashLength, double gapLength) {
        Line line = getEdgeLine(source, target);

        line.getStrokeDashArray().clear();

        if (dash) {
            line.getStrokeDashArray().addAll(dashLength, gapLength);
        }
    }

    /**
     * Resets the color used to draw the given {@linkplain Edge edge} to the default color ({@link GraphStyle#DEFAULT_EDGE_COLOR}).
     *
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link GraphPane}.
     */
    public void resetEdgeColor(BinaryNode<?> source, BinaryNode<?> target) {
        setEdgeColor(source, target, DEFAULT_EDGE_COLOR);
    }

    /**
     * Updates the position of all {@linkplain Edge edges} on this {@link GraphPane}.
     */
    public void redrawEdges() {
        for (Edge edge : edges.keySet()) {
            redrawEdge(edge);
        }
    }

    /**
     * Updates the position of the given {@linkplain Edge edge}.
     *
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link GraphPane}.
     */
    public void redrawEdge(BinaryNode<?> source, BinaryNode<?> target) {
        redrawEdge(getEdge(source, target));
    }

    private void redrawEdge(Edge edge) {
        Point2D transformedPointA = transform(getLocation(edge.source()));
        Point2D transformedPointB = transform(getLocation(edge.target()));

        Line line = edges.get(edge);

        line.setStartX(transformedPointA.getX());
        line.setStartY(transformedPointA.getY());

        line.setEndX(transformedPointB.getX());
        line.setEndY(transformedPointB.getY());
    }

    // --- Node Handling --- //

    /**
     * Adds a node to this {@link GraphPane} and displays it.
     *
     * @param node The node to display.
     */
    public void addNode(BinaryNode<?> node, Location location) {
        nodeLocations.put(node, location);
        nodes.put(node, drawNode(node));
    }

    /**
     * Removes the given node from this {@link GraphPane}.<p>
     * {@linkplain Edge edge}s connected to the removed node will not get removed.
     * The node will not be displayed after anymore calling this method.
     * If the given node is not part of this {@linkplain GraphPane} the method does nothing.
     *
     * @param node The node to remove.
     */
    public void removeNode(BinaryNode<?> node) {
        LabeledNode labeledNode = nodes.remove(node);
        nodeLocations.remove(node);
        nodesToEdge.remove(node);

        if (labeledNode != null) {
            getChildren().removeAll(labeledNode.ellipse(), labeledNode.text());
        }
    }

    /**
     * Updates the color used to draw the circumference of given node.
     *
     * @param node  The node to update.
     * @param color The new color.
     * @throws IllegalArgumentException If the given node is not part of this {@link GraphPane}.
     */
    public void setNodeStrokeColor(BinaryNode<?> node, Color color) {
        LabeledNode labeledNode = nodes.get(node);

        if (labeledNode == null) {
            throw new IllegalArgumentException("The given node is not part of this GraphPane");
        }

        labeledNode.setStrokeColor(color);
    }

    /**
     * Updates the color used to fill the given N.
     *
     * @param node  The N to update.
     * @param color The new color.
     * @throws IllegalArgumentException If the given N is not part of this {@link GraphPane}.
     */
    public void setNodeFillColor(BinaryNode<?> node, Color color) {
        LabeledNode labeledNode = nodes.get(node);

        if (labeledNode == null) {
            throw new IllegalArgumentException("The given node is not part of this GraphPane");
        }

        labeledNode.setFillColor(color);
    }

    /**
     * Resets the color used to draw the given N to the default color ({@link GraphStyle#DEFAULT_NODE_COLOR}).
     *
     * @param node The N to update.
     * @throws IllegalArgumentException If the given N is not part of this {@link GraphPane}.
     */
    public void resetNodeColor(BinaryNode<?> node) {
        setNodeStrokeColor(node, DEFAULT_NODE_COLOR);
    }

    /**
     * Updates the position of all nodes on this {@link GraphPane}.
     */
    public void redrawNodes() {
        for (BinaryNode<?> node : nodes.keySet()) {
            redrawNode(node);
        }
    }

    /**
     * Updates the position of the given node.
     *
     * @param node The node to update.
     * @throws IllegalArgumentException If the given node is not part of this {@link GraphPane}.
     */
    public void redrawNode(BinaryNode<?> node) {
        if (!nodes.containsKey(node)) {
            throw new IllegalArgumentException("The given node is not part of this GraphPane");
        }

        Point2D transformedMidPoint = transform(midPoint(node));

        LabeledNode labeledNode = nodes.get(node);

        labeledNode.ellipse().setCenterX(transformedMidPoint.getX());
        labeledNode.ellipse().setCenterY(transformedMidPoint.getY());

        labeledNode.text().setX(transformedMidPoint.getX() - labeledNode.text().getLayoutBounds().getWidth() / 2.0);
        labeledNode.text().setY(transformedMidPoint.getY() + labeledNode.text().getLayoutBounds().getHeight() / 4.0);

        if (node instanceof RBNode<?> rbNode) {
            Color color = rbNode.getColor() == null ? DEFAULT_NODE_COLOR : rbNode.isRed() ? Color.RED : Color.BLACK;

            labeledNode.setFillColor(color);
            labeledNode.setStrokeColor(color);
        }
    }

    // --- Other Util --- //

    /**
     * Removes all components from this {@link GraphPane}.
     */
    public void clear() {
        nodes.clear();
        nodeLocations.clear();
        nodesToEdge.clear();
        edges.clear();

        getChildren().clear();
    }

    /**
     * Updates the position of all components on this {@link GraphPane}.
     */
    public void redrawMap() {
        redrawEdges();
        redrawNodes();

    }

    /**
     * Tries to center this {@link GraphPane} as good as possible such that each node is visible while keeping the zoom factor as high as possible.
     */
    public void center() {

        if (getHeight() == 0.0 || getWidth() == 0.0) {
            return;
        }

        if (nodes.isEmpty()) {
            transformation.appendScale(20, 20);
            redrawGrid();
            return;
        }

        double maxX = nodeLocations.values().stream().mapToDouble(Location::x).max().orElse(0);

        double maxY = nodeLocations.values().stream().mapToDouble(Location::y).max().orElse(0);

        double minX = nodeLocations.values().stream().mapToDouble(Location::x).min().orElse(0);

        double minY = nodeLocations.values().stream().mapToDouble(Location::y).min().orElse(0);

        if (minX == maxX) {
            minX = minX - 1;
            maxX = maxX + 1;
        }

        if (minY == maxY) {
            minY = minY - 1;
            maxY = maxY + 1;
        }

        double widthPadding = (maxX - minX) / 10;
        double heightPadding = (maxY - minY) / 10;

        double width = maxX - minX + 2 * widthPadding;
        double height = maxY - minY + 2 * heightPadding;

        Affine inverse = new Affine();

        inverse.appendTranslation(minX - widthPadding, minY - heightPadding);
        inverse.appendScale((width) / getWidth(), (height) / getHeight());

        try {
            transformation = inverse.createInverse();
        } catch (NonInvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }

        redrawGrid();
        redrawMap();

        alreadyCentered = true;
    }

    /**
     * Increases the zoom of the current graphPane, i.e. the size of the covered area is decreased.
     */
    public void zoomIn() {
        zoom(getWidth() / 2.0, getHeight() / 2.0, SCALE_IN);
    }

    /**
     * Decreases the zoom of the current graphPane, i.e. the size of the covered area is increased.
     */
    public void zoomOut() {
        zoom(getWidth() / 2.0, getHeight() / 2.0, SCALE_OUT);
    }



    // --- Private Methods --- //

    private void initListeners() {

        setOnMouseDragged(event -> {
                Point2D point = new Point2D(event.getX(), event.getY());
                Point2D diff = getDifference(point, lastPoint.get());

                transformation.appendTranslation(diff.getX() / getTransformScaleX(), diff.getY() / getTransformScaleY());

                redrawMap();
                redrawGrid();
                updatePositionText(point);

                lastPoint.set(point);

            }
        );

        setOnScroll(event -> {
            if (event.getDeltaY() == 0) {
                return;
            }

            zoom(event.getX(), event.getY(), event.getDeltaY() > 0 ? SCALE_IN : SCALE_OUT);
        });

        setOnMouseMoved(event -> {
            Point2D point = new Point2D(event.getX(), event.getY());
            lastPoint.set(point);
            updatePositionText(point);
        });

        widthProperty().addListener((obs, oldValue, newValue) -> {
            setClip(new Rectangle(0, 0, getWidth(), getHeight()));

            if (alreadyCentered) {
                redrawGrid();
                redrawMap();
            } else {
                center();
            }

            drawPositionText();
        });

        heightProperty().addListener((obs, oldValue, newValue) -> {
            setClip(new Rectangle(0, 0, getWidth(), getHeight()));

            if (alreadyCentered) {
                redrawGrid();
                redrawMap();
            } else {
                center();
            }

            drawPositionText();
        });
    }

    private void zoom(double x, double y, double scale) {
        if (((getTransformScaleX() < MIN_SCALE || getTransformScaleY() < MIN_SCALE) && scale < 1)
            || ((getTransformScaleX() > MAX_SCALE || getTransformScaleY() > MAX_SCALE) && scale > 1)) {
            return;
        }

        Point2D point = inverseTransform(x, y);
        transformation.appendScale(scale, scale, point.getX(), point.getY());

        redrawMap();
        redrawGrid();
    }

    private Line drawEdge(BinaryNode<?> source, BinaryNode<?> target) {
        Location a = getLocation(source);
        Location b = getLocation(target);

        Point2D transformedA = transform(a);
        Point2D transformedB = transform(b);

        Line line = new Line(transformedA.getX(), transformedA.getY(), transformedB.getX(), transformedB.getY());
        //line.getStrokeDashArray().addAll(50.0, 10.0);

        line.setStrokeWidth(EDGE_STROKE_WIDTH);

        getChildren().add(line);

        return line;
    }

    private LabeledNode drawNode(BinaryNode<?> node) {
        Point2D transformedPoint = transform(getLocation(node));

        Ellipse ellipse = new Ellipse(transformedPoint.getX(), transformedPoint.getY(), NODE_DIAMETER, NODE_DIAMETER);
        ellipse.setStroke(DEFAULT_NODE_COLOR);
        ellipse.setFill(DEFAULT_NODE_COLOR);
        ellipse.setStrokeWidth(NODE_STROKE_WIDTH);
        setMouseTransparent(false);

        Text text = new Text(transformedPoint.getX(), transformedPoint.getY(), node.getKey().toString());
        text.setStroke(TEXT_COLOR);

        getChildren().addAll(ellipse, text);

        return new LabeledNode(ellipse, text);
    }

    private void drawGrid() {

        int stepX = (int) (getTransformScaleX() / 2);
        int stepY = (int) (getTransformScaleY() / 2);

        int offsetX = (int) getTransformTranslateX();
        int offsetY = (int) getTransformTranslateY();

        // Vertical Lines
        for (int i = 0, x = offsetX % (stepX * 5); x <= getWidth(); i++, x += stepX) {
            Float strokeWidth = getStrokeWidth(i, offsetX % (stepX * 10) > stepX * 5);
            if (strokeWidth == null) continue;
            Line line = new Line(x, 0, x, getHeight());
            line.setStrokeWidth(strokeWidth);
            line.setStroke(GRID_LINE_COLOR);
            getChildren().add(line);
            grid.add(line);
        }

        // Horizontal Lines
        for (int i = 0, y = offsetY % (stepY * 5); y <= getHeight(); i++, y += stepY) {
            Float strokeWidth = getStrokeWidth(i, offsetY % (stepY * 10) > stepY * 5);
            if (strokeWidth == null) continue;

            var line = new Line(0, y, getWidth(), y);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(GRID_LINE_COLOR);
            getChildren().add(line);
            grid.add(line);
        }
    }

    private Float getStrokeWidth(int i, boolean inverted) {
        float strokeWidth;
        if (i % 10 == 0) {
            strokeWidth = inverted ? GRID_TEN_TICKS_WIDTH : GRID_FIVE_TICKS_WIDTH;
        } else if (i % 5 == 0) {
            strokeWidth = inverted ? GRID_FIVE_TICKS_WIDTH : GRID_TEN_TICKS_WIDTH;
        } else {
            return null;
        }
        return strokeWidth;
    }

    private Edge getEdge(BinaryNode<?> source, BinaryNode<?> target) {
        return nodesToEdge.get(source).get(target);
    }

    private Line getEdgeLine(BinaryNode<?> source, BinaryNode<?> target) {
        Line line = edges.get(getEdge(source, target));

        if (line == null) {
            throw new IllegalArgumentException("The given edge is not part of this GraphPane");
        }
        return line;
    }

    private Location getLocation(BinaryNode<?> node) {
        return nodeLocations.getOrDefault(node, Location.ORIGIN);
    }

    private Point2D locationToPoint2D(Location location) {
        return new Point2D(location.x(), location.y());
    }

    private Point2D getDifference(Point2D p1, Point2D p2) {
        return new Point2D(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    private Point2D midPoint(Location location) {
        return new Point2D(location.x(), location.y());
    }

    private Point2D midPoint(BinaryNode<?> node) {
        return midPoint(getLocation(node));
    }

    private void redrawGrid() {
        getChildren().removeAll(grid);
        grid.clear();
        drawGrid();
    }

    private void drawPositionText() {
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
        positionText.setText("(-, -)");
        if (!getChildren().contains(positionText)) {
            getChildren().add(positionText);
        }
    }

    private void updatePositionText(Point2D point) {
        Point2D inverse = inverseTransform(point);
        positionText.setText("(%d, %d)".formatted((int) inverse.getX(), (int) inverse.getY()));
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
    }

    private Point2D inverseTransform(Point2D point) {
        return inverseTransform(point.getX(), point.getY());
    }

    private Point2D inverseTransform(double x, double y) {
        try {
            return transformation.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }
    }

    private Point2D transform(Point2D point) {
        return transformation.transform(point);
    }

    private Point2D transform(Location location) {
        return transformation.transform(locationToPoint2D(location));
    }

    private double getTransformScaleX() {
        return transformation.getMxx();
    }

    private double getTransformScaleY() {
        return transformation.getMyy();
    }

    private double getTransformTranslateX() {
        return transformation.getTx();
    }

    private double getTransformTranslateY() {
        return transformation.getTy();
    }

    private double calculateXOffsets(BinaryNode<?> root, Map<BinaryNode<?>, Double> xOffsets, double currentOffset) {

        if (root == null) {
            return currentOffset;
        }

        double leftMaxOffset = currentOffset;

        if (root.hasLeft()) {
            leftMaxOffset = calculateXOffsets(root.getLeft(), xOffsets, currentOffset);
        }

        double rightMaxOffset = currentOffset;

        if (root.hasRight()) {
            rightMaxOffset = calculateXOffsets(root.getRight(), xOffsets, leftMaxOffset + NODE_DISTANCE);
        }

        if (root.hasLeft() && root.hasRight()) {

            double leftOffset = xOffsets.get(root.getLeft());
            double rightOffset = xOffsets.get(root.getRight());

            xOffsets.put(root, leftOffset + (rightOffset - leftOffset) / 2.0);
        } else {
            xOffsets.put(root, leftMaxOffset + (rightMaxOffset - leftMaxOffset) / 2.0);
        }

        return rightMaxOffset;
    }

    private record Edge(BinaryNode<?> source, BinaryNode<?> target) {

    }

    private static class LabeledNode {
        private final Ellipse ellipse;
        private final Text text;

        private LabeledNode(Ellipse ellipse, Text text) {
            this(ellipse, text, DEFAULT_NODE_COLOR);
        }

        private LabeledNode(Ellipse ellipse, Text text, Color color) {
            this.ellipse = ellipse;
            this.text = text;
            setStrokeColor(color);
        }

        public Ellipse ellipse() {
            return ellipse;
        }

        public Text text() {
            return text;
        }

        public void setStrokeColor(Color strokeColor) {
            ellipse.setStroke(strokeColor);
        }

        public void setFillColor(Color fillColor) {
            ellipse.setFill(fillColor);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (LabeledNode) obj;
            return Objects.equals(this.ellipse, that.ellipse) &&
                Objects.equals(this.text, that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ellipse, text);
        }

    }

    /**
     * A data class for storing a location in a 2D plane.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    private record Location(double x, double y) {

        public static final Location ORIGIN = new Location(0, 0);

        Location add(int x, int y) {
            return new Location(this.x + x, this.y + y);
        }

        Location subtract(int x, int y) {
            return new Location(this.x - x, this.y - y);
        }
    }
}
