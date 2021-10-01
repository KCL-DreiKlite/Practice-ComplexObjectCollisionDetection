package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonObject extends CollisionObject {


    public PolygonObject() {
        super(ObjectType.TYPE_POLYGON);
    }
    public PolygonObject(MathPoint referencePoint, ArrayList<MathPoint> vertices) {
        super(ObjectType.TYPE_POLYGON, referencePoint, vertices);
    }

    public boolean isCollidedTo(PolygonObject hostileObject) {
        return !isSeparatedTo(hostileObject);
    }

    public boolean isSeparatedTo(PolygonObject hostileObject) {
        ArrayList<MathVector> separateAxes = getRightNormals();
        for (MathVector separateAxis: separateAxes) {
            double[] myMinMax = getMinMax(separateAxis);
            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);

            if (myMinMax[0] > hostileMinMax[1] || hostileMinMax[0] > myMinMax[1])
                return true;
        }

        ArrayList<MathVector> hostileSeparateAxes = hostileObject.getRightNormals();
        for (MathVector separateAxis: hostileSeparateAxes) {
            double[] myMinMax = getMinMax(separateAxis);
            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);

            if (myMinMax[0] > hostileMinMax[1] || hostileMinMax[0] > myMinMax[1])
                return true;
        }

        return false;

    }

    protected double[] getMinMax(MathVector axis) {
        double[] result = new double[2];

        result[0] = new MathVector(rotatedVertices.get(0)).getProjectLengthOnto(axis);
        result[1] = result[0];

        for (MathPoint point : rotatedVertices) {
            MathVector pointInVector = new MathVector(point);
            result[0] = Math.min(result[0], pointInVector.getProjectLengthOnto(axis));
            result[1] = Math.max(result[1], pointInVector.getProjectLengthOnto(axis));
        }

        return result;
    }

    protected ArrayList<MathVector> getLeftNormals() {
        ArrayList<MathVector> norms = new ArrayList<>(rotatedVertices.size());
        for (int i = 0; i < rotatedVertices.size(); i++) {
            MathPoint p1 = rotatedVertices.get(i);
            MathPoint p2 = rotatedVertices.get(i+1 >= rotatedVertices.size() ? 0 : i+1);
            MathVector edge = new MathVector(p1, p2);
            norms.add(edge.getLeftNormal());
        }
        return norms;
    }

    protected ArrayList<MathVector> getRightNormals() {
        ArrayList<MathVector> norms = new ArrayList<>(rotatedVertices.size());
        for (int i = 0; i < rotatedVertices.size(); i++) {
            MathPoint p1 = rotatedVertices.get(i);
            MathPoint p2 = rotatedVertices.get(i+1 >= rotatedVertices.size() ? 0 : i+1);
            MathVector edge = new MathVector(p2, p1);
            norms.add(edge.getLeftNormal());
        }
        return norms;
    }

    public void addVertex(MathPoint point) {
        referenceVertices.add(point);
        resetRotatedVertices();
    }

    public void addVertices(ArrayList<MathPoint> points) {
        referenceVertices.addAll(points);
        resetRotatedVertices();
    }
    public void addVertices(MathPoint ... points) {
        referenceVertices.addAll(Arrays.asList(points));
        resetRotatedVertices();
    }

//    public void move(MathVector vector) {
//        referenceLocation.move(vector);
//        referenceVertices.forEach(point -> point.move(vector));
//        rotatedVertices.forEach(point -> point.move(vector));
//    }
//    public void move(MathPoint destination) {
//        move(new MathVector(referenceLocation, destination));
//    }
//
//    public void rotate(double radians) {
//        MathPoint tempRefPoint = new MathPoint(referenceLocation);
//        setReferencePoint(MathPoint.POINT_ORIGIN);
//        for (int i = 0; i < referenceVertices.size(); i++) {
//            double x1 = referenceVertices.get(i).x;
//            double y1 = referenceVertices.get(i).y;
//            double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
//            double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);
//
//            rotatedVertices.get(i).setPoint(x2, y2);
//        }
//        setReferencePoint(tempRefPoint);
//    }

    public void setReferencePoint(MathPoint referenceLocation) {
        move(new MathPoint(referenceLocation));
    }

    public MathPoint getReferencePoint() {
        return referenceLocation;
    }
    public ArrayList<MathPoint> getOriginVertices() {
        return referenceVertices;
    }
    public ArrayList<MathPoint> getRotatedVertices() {
        return rotatedVertices;
    }


}
