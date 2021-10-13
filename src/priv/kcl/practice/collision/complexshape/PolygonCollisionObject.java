package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

public class PolygonCollisionObject extends CollisionObject
        implements CollisionDetector, Movable, Rotatable, AABBConverter {

    protected static final int MIN_PROJECTION_INDEX = 0;
    protected static final int MAX_PROJECTION_INDEX = 1;

    protected ArrayList<MathPoint> referenceVertices;
    protected ArrayList<MathPoint> rotatedVertices;

    public PolygonCollisionObject() {
        super(ObjectType.TYPE_POLYGON);
    }

    public PolygonCollisionObject(PolygonCollisionObject polygonCollisionObject) {
        super(ObjectType.TYPE_POLYGON, polygonCollisionObject.referenceLocation);

        this.referenceVertices = new ArrayList<>(polygonCollisionObject.referenceVertices.size());
        polygonCollisionObject.referenceVertices.forEach(vertex ->
                this.referenceVertices.add(new MathPoint(vertex)));

        resetRotatedVertices();
    }

    public PolygonCollisionObject(MathPoint referencePoint, ArrayList<MathPoint> referenceVertices) {
        super(ObjectType.TYPE_POLYGON, referencePoint);

        this.referenceVertices = referenceVertices;

        resetRotatedVertices();
    }

    @Override
    protected void initInterface() {
        this.collisionDetector = this;
        this.movable = this;
        this.rotatable = this;
        this.aabbConverter = this;
    }
//    public boolean isCollidedTo(PolygonCollisionObject hostileObject) {
//        return !isSeparatedTo(hostileObject);
//    }
//
//    public boolean isSeparatedTo(PolygonCollisionObject hostileObject) {
//        ArrayList<MathVector> separateAxes = getRightNormals();
//        for (MathVector separateAxis: separateAxes) {
//            double[] myMinMax = getMinMax(separateAxis);
//            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);
//
//            if (myMinMax[0] > hostileMinMax[1] || hostileMinMax[0] > myMinMax[1])
//                return true;
//        }
//
//        ArrayList<MathVector> hostileSeparateAxes = hostileObject.getRightNormals();
//        for (MathVector separateAxis: hostileSeparateAxes) {
//            double[] myMinMax = getMinMax(separateAxis);
//            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);
//
//            if (myMinMax[0] > hostileMinMax[1] || hostileMinMax[0] > myMinMax[1])
//                return true;
//        }
//
//        return false;
//

//    }
    protected void resetRotatedVertices() {
        rotatedVertices = new ArrayList<>(referenceVertices.size());
        referenceVertices.forEach(vertex -> rotatedVertices.add(new MathPoint(vertex)));
    }

    public void addVertex(MathPoint vertex) {
        referenceVertices.add(new MathPoint(vertex));
        resetRotatedVertices();
    }
    public void addVertices(ArrayList<MathPoint> vertices) {
        for (MathPoint vertex : vertices)
            referenceVertices.add(new MathPoint(vertex));
        resetRotatedVertices();
    }

    public void addVertices(MathPoint ... vertices) {
        for (MathPoint vertex : vertices)
            referenceVertices.add(new MathPoint(vertex));
        resetRotatedVertices();
    }


    public ArrayList<MathPoint> getReferenceVertices() {
        return referenceVertices;
    }

    public ArrayList<MathPoint> getRotatedVertices() {
        return rotatedVertices;
    }


    @Override
    public boolean isSeparatedFrom(AABBCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException("Unable detect collision between AABBCollisionObject and PolygonCollisionObject");
    }

    @Override
    public boolean isSeparatedFrom(PolygonCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        ArrayList<MathVector> separateAxes = getRightNormals();
        for (MathVector separateAxis: separateAxes) {
            double[] myMinMax = getMinMax(separateAxis);
            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);

            if (myMinMax[MIN_PROJECTION_INDEX] > hostileMinMax[MAX_PROJECTION_INDEX] ||
                    hostileMinMax[MIN_PROJECTION_INDEX] > myMinMax[MAX_PROJECTION_INDEX])
                return true;
        }

        ArrayList<MathVector> hostileSeparateAxes = hostileObject.getRightNormals();
        for (MathVector separateAxis: hostileSeparateAxes) {
            double[] myMinMax = getMinMax(separateAxis);
            double[] hostileMinMax = hostileObject.getMinMax(separateAxis);

            if (myMinMax[MIN_PROJECTION_INDEX] > hostileMinMax[MAX_PROJECTION_INDEX] ||
                    hostileMinMax[MIN_PROJECTION_INDEX] > myMinMax[MAX_PROJECTION_INDEX])
                return true;
        }

        return false;
    }

    protected double[] getMinMax(MathVector axis) {
        double[] result = new double[2];

        result[MIN_PROJECTION_INDEX] = new MathVector(rotatedVertices.get(0)).getProjectLengthOnto(axis);
        result[MAX_PROJECTION_INDEX] = result[MIN_PROJECTION_INDEX];

        for (MathPoint point : rotatedVertices) {
            MathVector pointInVector = new MathVector(point);
            result[MIN_PROJECTION_INDEX] = Math.min(result[MIN_PROJECTION_INDEX], pointInVector.getProjectLengthOnto(axis));
            result[MAX_PROJECTION_INDEX] = Math.max(result[MAX_PROJECTION_INDEX], pointInVector.getProjectLengthOnto(axis));
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

    @Override
    public boolean isSeparatedFrom(CircleCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        ArrayList<MathVector> separateAxes = getLeftNormals();

        MathPoint circleOrigin = hostileObject.getOrigin();
        double circleRadius = hostileObject.getRadius();

        for (MathVector separateAxis: separateAxes) {
            double[] minmax = getMinMax(separateAxis);
            double minPoly = minmax[0];
            double maxPoly = minmax[1];

            double originProjLenOntoAxis = new MathVector(circleOrigin).getProjectLengthOnto(separateAxis);
            double minCirc = originProjLenOntoAxis - circleRadius;
            double maxCirc = originProjLenOntoAxis + circleRadius;

            if (minPoly > maxCirc || minCirc > maxPoly)
                return true;
        }
        return false;
    }

    @Override
    public boolean isSeparatedFrom(ComplexCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        return hostileObject.isSeparatedFrom(this);
    }

    @Override
    public void moveObject(MathVector movement) throws UnsupportedMovementException {
        referenceLocation.move(movement);
        referenceVertices.forEach(vertex -> vertex.move(movement));
        rotatedVertices.forEach(vertex -> vertex.move(movement));
    }

    @Override
    public void rotateObject(double angle) throws UnsupportedRotationException {
//        MathPoint originRefLocation = new MathPoint(referenceLocation);
//        move(MathPoint.ORIGIN_POINT);
//        referenceVertices.forEach(vertex -> {
//            double x1 = vertex.x;
//            double y1 = vertex.y;
//            double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
//            double y2 = x1 * Math.sin(angle) + y1 * Math.sin(angle);
//
//            vertex.setPoint(x2, y2);
//        });
//        move(originRefLocation);
        MathPoint tempRefPoint = new MathPoint(referenceLocation);
        move(MathPoint.ORIGIN_POINT);
        for (int currentVertexIndex = 0; currentVertexIndex < referenceVertices.size(); currentVertexIndex++) {
            double x1 = referenceVertices.get(currentVertexIndex).x;
            double y1 = referenceVertices.get(currentVertexIndex).y;
            double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
            double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

            rotatedVertices.get(currentVertexIndex).setPoint(x2, y2);
        }
        move(tempRefPoint);
    }

    @Override
    public AABBCollisionObject convert() {
        double minX = rotatedVertices.get(0).x;
        double maxX = minX;
        double minY = rotatedVertices.get(0).y;
        double maxY = minY;

        for (MathPoint vertex : rotatedVertices) {
            minX = Math.min(minX, vertex.x);
            maxX = Math.max(maxX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxY = Math.max(maxY, vertex.y);
        }

        return new AABBCollisionObject(referenceLocation, new MathPoint(minX, minY), new MathPoint(maxX, maxY));
    }
}
