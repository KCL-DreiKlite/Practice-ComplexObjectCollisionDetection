package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

enum CheckType {
    AABB_TO_AABB,
    // AABB object can only be collided by AABB object!
//    AABB_TO_POLY,
//    AABB_TO_CIRC,
//    AABB_TO_COMP,

    POLY_TO_POLY,
    POLY_TO_CIRC,
    POLY_TO_COMP,

    CIRC_TO_CIRC,
    CIRC_TO_COMP,

    COMP_TO_COMP
}

public class CollisionObject {

    public static boolean checkCollision(CollisionObject objectA, CollisionObject objectB) {
        return !checkSeparation(objectA, objectB);
    }

    public static boolean checkSeparation(CollisionObject objectA, CollisionObject objectB) {
        CheckType checkType = getCheckType(objectA, objectB);

        if (checkType == CheckType.AABB_TO_AABB) {
            return ((AABBObject) objectA).isSeparatedTo((AABBObject) objectB);
        }
        else if (checkType == CheckType.CIRC_TO_CIRC) {

        }
        else if (checkType == CheckType.POLY_TO_POLY) {
            return ((PolygonObject) objectA).isSeparatedTo((PolygonObject) objectB);
        }
        else if (checkType == CheckType.COMP_TO_COMP) {

        }
        else if (checkType == CheckType.POLY_TO_CIRC) {
            if (objectA.objectType == ObjectType.TYPE_POLYGON)
                return CHECK_SEPARATE_POLY_TO_CIRC((PolygonObject) objectA, (CircleObject) objectB);
            else
                return CHECK_SEPARATE_POLY_TO_CIRC((PolygonObject) objectB, (CircleObject) objectA);
        }
        else if (checkType == CheckType.POLY_TO_COMP) {

        }

        if (objectA.objectType == ObjectType.TYPE_CIRCLE)
            return CHECK_SEPARATE_CIRC_TO_COMP((CircleObject) objectA, (ComplexObject) objectB);
        return CHECK_SEPARATE_CIRC_TO_COMP((CircleObject) objectB, (ComplexObject) objectA);
    }

    protected static CheckType getCheckType(CollisionObject objectA, CollisionObject objectB) {
        ObjectType typeA = objectA.getObjectType();
        ObjectType typeB = objectB.getObjectType();
        if (typeA == typeB) {
            if (typeA == ObjectType.TYPE_AABB)
                return CheckType.AABB_TO_AABB;
            else if (typeA == ObjectType.TYPE_POLYGON)
                return CheckType.POLY_TO_POLY;
            else if (typeA == ObjectType.TYPE_CIRCLE)
                return CheckType.CIRC_TO_CIRC;
            else
                return CheckType.COMP_TO_COMP;
        }

//        if (typeA == ObjectType.TYPE_AABB && typeB == ObjectType.TYPE_POLYGON ||
//            typeA == ObjectType.TYPE_POLYGON && typeB == ObjectType.TYPE_AABB)
//            return CheckType.AABB_TO_POLY;
//        else if (typeA == ObjectType.TYPE_AABB && typeB == ObjectType.TYPE_CIRCLE ||
//            typeA == ObjectType.TYPE_CIRCLE && typeB == ObjectType.TYPE_AABB) {
//            return CheckType.AABB_TO_CIRC;
//        }
//        else if (typeA == ObjectType.TYPE_AABB && typeB == ObjectType.TYPE_COMPLEX ||
//            typeA == ObjectType.TYPE_COMPLEX && typeB == ObjectType.TYPE_AABB) {
//            return CheckType.AABB_TO_COMP;
//        }
        else if (typeA == ObjectType.TYPE_POLYGON && typeB == ObjectType.TYPE_CIRCLE ||
            typeA == ObjectType.TYPE_CIRCLE && typeB == ObjectType.TYPE_POLYGON) {
            return CheckType.POLY_TO_CIRC;
        }
        else if (typeA == ObjectType.TYPE_POLYGON && typeB == ObjectType.TYPE_COMPLEX ||
            typeA == ObjectType.TYPE_COMPLEX && typeB == ObjectType.TYPE_POLYGON)
            return CheckType.POLY_TO_COMP;
//        else if (typeA == ObjectType.TYPE_CIRCLE && typeB == ObjectType.TYPE_COMPLEX ||
//            typeA == ObjectType.TYPE_COMPLEX && typeB == ObjectType.TYPE_CIRCLE)
        return CheckType.CIRC_TO_COMP;

    }

    protected static boolean CHECK_SEPARATE_POLY_TO_CIRC(PolygonObject polygonObject, CircleObject circleObject) {
        ArrayList<MathVector> separateAxes = polygonObject.getLeftNormals();

        MathPoint circleOrigin = circleObject.getOrigin();
        double circleRadius = circleObject.getRadius();

        for (MathVector separateAxis: separateAxes) {
            double[] minmax = polygonObject.getMinMax(separateAxis);
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
    protected static boolean CHECK_SEPARATE_POLY_TO_COMP(PolygonObject polygonObject, ComplexObject complexObject) {
        return false;
    }
    protected static boolean CHECK_SEPARATE_CIRC_TO_COMP(CircleObject circleObject, ComplexObject complexObject) {
        return false;
    }

    protected final ObjectType objectType;

    protected MathPoint referenceLocation;
    protected ArrayList<MathPoint> referenceVertices;
    protected ArrayList<MathPoint> rotatedVertices;

    protected CollisionObject(CollisionObject collisionObject) {
        this.objectType = collisionObject.objectType;

        this.referenceLocation = new MathPoint(collisionObject.referenceLocation);
        this.referenceVertices = new ArrayList<>(collisionObject.referenceVertices);
        resetRotatedVertices();
    }
    protected CollisionObject(ObjectType objectType) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint();
        this.referenceVertices = new ArrayList<>();
        resetRotatedVertices();
    }
    protected CollisionObject(ObjectType objectType, MathPoint referenceLocation) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint(referenceLocation);
        this.referenceVertices = new ArrayList<>();
        resetRotatedVertices();
    }
    protected CollisionObject(ObjectType objectType, MathPoint referenceLocation, ArrayList<MathPoint> referenceVertices) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint(referenceLocation);
        this.referenceVertices = new ArrayList<>(referenceVertices);
        resetRotatedVertices();
    }


    protected void resetRotatedVertices() {
        rotatedVertices = new ArrayList<>(referenceVertices.size());
        referenceVertices.forEach(refVertex -> rotatedVertices.add(new MathPoint(refVertex)));
    }

    public void move(MathVector movement) {
        referenceLocation.move(movement);

        if (referenceVertices.isEmpty())
            return;

        referenceVertices.forEach(vertex -> vertex.move(movement));
        rotatedVertices.forEach(vertex -> vertex.move(movement));
    }
    public void move(MathPoint destination) {
        move(new MathVector(referenceLocation, destination));
    }

    public void rotate(double angle) {
        // AABB object does not allow rotating.
        if (objectType == ObjectType.TYPE_AABB)
            return;

        // You cannot rotate a shape that does not exist right?
        // Right?
        if (rotatedVertices.isEmpty())
            return;

        MathPoint originRefLocation = new MathPoint(referenceLocation);
        move(MathPoint.POINT_ORIGIN);
        for (int i = 0; i < referenceVertices.size(); i++) {
            double x1 = referenceVertices.get(i).x;
            double y1 = referenceVertices.get(i).y;
            double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
            double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

            rotatedVertices.get(i).setPoint(x2, y2);
        }
        move(originRefLocation);
    }

    public boolean isCollidedTo(CollisionObject hostileObject) {
        return false;
    }

    public boolean isSeparatedTo(CollisionObject hostileObject) {
        return true;
    }

    public void setReferenceLocation(MathPoint referenceLocation) {
        move(referenceLocation);
    }

    public MathPoint getReferenceLocation() {
        return referenceLocation;
    }
    public ArrayList<MathPoint> getReferenceVertices() {
        return referenceVertices;
    }
    public ArrayList<MathPoint> getRotatedVertices() {
        return rotatedVertices;
    }
    public ObjectType getObjectType() {
        return objectType;
    }

    public AABBObject toAABBObject() {
        if (rotatedVertices.isEmpty())
            return null;

        if (objectType == ObjectType.TYPE_AABB)
            return new AABBObject((AABBObject) this);

        double minX = rotatedVertices.get(0).x;
        double maxX = minX;
        double minY = rotatedVertices.get(0).y;
        double maxY = minY;

        for (MathPoint vertex: rotatedVertices) {
            minX = Math.min(minX, vertex.x);
            maxX = Math.max(maxX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxY = Math.max(maxY, vertex.y);
        }

        return new AABBObject(referenceLocation, new MathPoint(minX, minY), new MathPoint(maxX, maxY));
    }

}
