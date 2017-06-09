package com.example.bpdiagnostics;
public class Point {

    private static final double DOUBLE_DELTA = 1E-10;
    private double x;
    private double y;

    /**
     * Creates a new point in R^2.
     *
     * @param x the x-coordinate (in R)
     * @param y the y-coordinate (in R)
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the crossproduct of vectors origin->p2 and origin->this.
     *
     * @param origin The point in which both vectors originate
     * @param p2 The point that determines the second vector.
     * @return 0 if both points are collinear, a value > 0 if this point lies
     * left of vector origin->p2 (when standing in origin looking at p2), a
     * value < 0 if this point lies right of vector origin->p2.
     */
    private double calcCrossProduct(Point origin, Point p2) {
        return (p2.x - origin.x) * (this.y - origin.y)
                - (p2.y - origin.y) * (this.x - origin.x);
    }

    /**
     * A point is considered left of a line between points from and to if it is
     * on the lefthand side when looking along the line from point "from" to
     * point "to".
     *
     * The method uses the cross-product to determine if this point is left of
     * the line.
     *
     * @param from Point from which the line is drawn and from where we "look"
     * along the line in direction of point "to" to determine whether the point
     * is left or right of it.
     * @param to Point to which the line is drawn
     */
    public boolean isLeftOfLine(Point from, Point to) {
        return Double.compare(calcCrossProduct(from, to), 0) > 0;
    }

    /**
     * This point is considered collinear if it lies on a straight line between
     * the two given points.
     *
     * The method uses the cross-product to determine if that's the case.
     *
     * @param p2
     * @param reference
     * @return true if point is collinear, false otherwise
     */
    public boolean isCollinearTo(Point p2, Point reference) {
        return Math.abs(0.0 - calcCrossProduct(reference, p2)) < DOUBLE_DELTA;
    }

    /**
     * Calculates the distance to given point.
     *
     * @param p2 Point to calculate the distance to.
     * @return The distance of this point to given point.
     */
    public double getDistance(Point p2) {
        return Math.sqrt((this.x - p2.x) * (this.x - p2.x)
                + (this.y - p2.y) * (this.y - p2.y));
    }

    /**
     *
     * @return x value of point.
     */
    public double getX() {
        return x;
    }

    /**
     *
     * @return y value of point.
     */
    public double getY() {
        return y;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Point other = (Point) o;
        return Math.abs(this.x - other.x) < DOUBLE_DELTA
                && Math.abs(this.y - other.y) < DOUBLE_DELTA;
    }

    /**
     * Calculates the distance of this point to the line which is formed by points a and b.
     * @param a
     * @param b
     * @return The distance to the line.
     */
    public double getDistanceToLine(Point a, Point b) {
        return Math.abs((b.getX() - a.getX()) * (a.getY() - this.y) - (a.getX() - this.x) * (b.getY() - a.getY()))
                / Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
}