package priv.kcl.practice.collision.complexshape;

import java.awt.*;

public class MathVector {

    /**
     * The abscissa component of this vector.
     */
    private double x;
    /**
     * The ordinate component of this vector.
     */
    private double y;

    /**
     * Create a zero vector.
     */
    public MathVector() {
        this.x = 0;
        this.y = 0;
    }
    /**
     * Clone a new vector.
     * @param vector
     */
    public MathVector(MathVector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

//    public MathVector(Point point) {
//        this.x = point.x;
//        this.y = point.y;
//    }

//    public MathVector(Point pointA, Point pointB) {
//        this.x = pointB.x - pointA.x;
//        this.y = pointB.y - pointA.y;
//    }
    /**
     * Create a new vector from ORIGIN to given point.
     * <br>
     * Vector = [ORIGIN -------> point]
     * @param point the destination of vector
     */
    public MathVector(MathPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    /**
     * Create a new vector pointing from A to B.
     * <br>
     * Vector = [pointA -------> pointB]
     * @param pointA the A point
     * @param pointB the B point
     */
    public MathVector(MathPoint pointA, MathPoint pointB) {
        this.x = pointB.x - pointA.x;
        this.y = pointB.y - pointA.y;
    }

    /**
     * Create a new vector.
     * @param x the abscissa component
     * @param y the ordinate component
     */
    public MathVector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Create a new vector.
     * @param x the abscissa component
     * @param y the ordinate component
     */
    public MathVector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Create a new vector.
     * @param x the abscissa component
     * @param y the ordinate component
     */
    public MathVector(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the length of this vector.
     * @return the absolute value of this vector
     */
    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Get the angle of this vector in radian.
     * @return the angle of vector
     */
    public double angleInRadian() {
        return Math.atan2(x, y);
    }

    /**
     * Get the dot product with another vector.
     * @param vector the other vector
     * @return the dot product of two vectors
     */
    public double dotProduct(MathVector vector) {
        double x1 = x, y1 = y;
        double x2 = vector.x, y2 = vector.y;
        return x1*x2 + y1*y2;
    }

    /**
     * Get the added vector by another vector without changing this
     * vector's value.
     * @param vector the other vector
     * @return the added vector
     */
    public MathVector getAddition(MathVector vector) {
        MathVector result = new MathVector(this);
        result.operateAddition(vector);
        return result;
    }
    /**
     * Do vector addition here from another vector.
     * @param vector the other vector
     */
    public void operateAddition(MathVector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    /**
     * Get the subtracted vector by another vector without changing
     * this vector's value.
     * @param vector the other vector
     * @return the subtracted vector
     */
    public MathVector getSubtract(MathVector vector) {
        MathVector result = new MathVector(this);
        result.operateSubtract(vector);
        return result;
    }
    /**
     * Do vector subtraction here from another vector.
     * @param vector the other vector
     */
    public void operateSubtract(MathVector vector) {
        operateAddition(vector.getReverse());
    }

    /**
     * Get the multiplied vector without changing this vector's value.
     * @param multiplier the multiplier
     * @return the multiplied vector
     */
    public MathVector getMultiply(double multiplier) {
        MathVector result = new MathVector(this);
        result.operateMultiply(multiplier);
        return result;
    }
    /**
     * Do scalar multiplication here.
     * @param multiplier the multiplier
     */
    public void operateMultiply(double multiplier) {
        x *= multiplier;
        y *= multiplier;
    }

    /**
     * Get the reversed vector without changing this vector's value.
     * @return the reversed vector
     */
    public MathVector getReverse() {
        return new MathVector(-x, -y);
    }
    /**
     * Do vector reverse here.
     */
    public void operateReverse() {
        x = -x;
        y = -y;
    }

    /**
     * Move the point base on this vector.
     * @param p the AWT point need to be moved
     */
    public void movePoint(Point p) {
        p.x += x;
        p.y += y;
    }
    /**
     * Move the point base on this vector.
     * @param point the point need to be moved
     */
    public void movePoint(MathPoint point) {
        point.x += x;
        point.y += y;
    }

    /**
     * Get left normal vector base on this vector.
     * @return the left normal vector
     */
    public MathVector getLeftNormal() {
        return new MathVector(-y, x);
    }
    /**
     * Get right normal vector base on this vector.
     * @return the right normal vector
     */
    public MathVector getRightNormal() {
        return new MathVector(y, -x);
    }

    public double getProjectLengthOnto(MathVector vector) {
        return dotProduct(vector) / vector.length();
    }
    public MathVector getProjectVectorOnto(MathVector vector) {
        MathVector numerator = vector.getMultiply(dotProduct(vector));
        double denominator = vector.length() * vector.length();
        return numerator.getMultiply(1 / denominator);
    }

    public MathVector getUnitVector() {
        return new MathVector(x / length(), y / length());
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setX(long x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setY(long y) {
        this.y = y;
    }
    public void setVector(MathVector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
    public void setVector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setVector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setVector(long x, long y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public String toString() {
        return "[MathVector@x="+x+",y="+y+"]";
    }
}
