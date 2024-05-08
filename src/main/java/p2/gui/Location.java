package p2.gui;

/**
 * A data class for storing a location in a 2D plane.
 * @param x The x-coordinate.
 * @param y The y-coordinate.
 */
public record Location(double x, double y) {

    public static final Location ORIGIN = new Location(0, 0);

    Location add(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    Location subtract(int x, int y) {
        return new Location(this.x - x, this.y - y);
    }
}
