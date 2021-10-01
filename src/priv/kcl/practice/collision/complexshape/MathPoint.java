package priv.kcl.practice.collision.complexshape;

import java.awt.*;
import java.util.ArrayList;

public class MathPoint {

    public static final MathPoint POINT_ORIGIN = new MathPoint(0d, 0d);

    public static MathVector getVectorByPoints(MathPoint p1, MathPoint p2) {
        return new MathVector(p1, p2);
    }

    public static MathPoint getCentroid(ArrayList<MathPoint> points) {
        double sumX = 0;
        double sumY = 0;
        int totalPoints = 0;
        for (MathPoint point: points) {
            sumX += point.x;
            sumY += point.y;
            totalPoints++;
        }
        return new MathPoint(sumX / totalPoints, sumY / totalPoints);
    }
    public static MathPoint getCentroid(MathPoint ... points) {
        double sumX = 0;
        double sumY = 0;
        int totalPoints = 0;
        for (MathPoint point: points) {
            sumX += point.x;
            sumY += point.y;
            totalPoints++;
        }
        return new MathPoint(sumX / totalPoints, sumY / totalPoints);
    }

    public static MathVector getSlopeAsUnitVector(MathPoint pointA, MathPoint pointB) {
        return new MathVector(pointA, pointB).getUnitVector();
    }

    /**
     * The abscissa coordinate.
     */
    public double x;
    /**
     * The ordinate coordinate.
     */
    public double y;


    public MathPoint() {
        this.x = 0;
        this.y = 0;
    }
    public MathPoint(MathPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    public MathPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public MathPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public MathPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public void move(MathVector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
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

    public void setPoint(MathPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    public void setPoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public int getAWTX() {
        return (int) x;
    }
    public int getAWTY() {
        return (int) y;
    }

    public MathVector toVector() {
        return new MathVector(x, y);
    }
    public Point toAWTPoint() {
        return new Point((int) x, (int) y);
    }

}
