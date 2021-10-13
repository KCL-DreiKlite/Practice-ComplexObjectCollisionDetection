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

public abstract class CollisionObject {

    public static boolean checkCollision(CollisionObject objectA, CollisionObject objectB) {
        return !checkSeparation(objectA, objectB);
    }

    public static boolean checkSeparation(CollisionObject objectA, CollisionObject objectB) {
        CheckType checkType = getCheckType(objectA, objectB);

        if (checkType == CheckType.AABB_TO_AABB) {
            return ((AABBCollisionObject) objectA).isSeparatedFrom((AABBCollisionObject) objectB);
        }
        else if (checkType == CheckType.CIRC_TO_CIRC) {

        }
        else if (checkType == CheckType.POLY_TO_POLY) {
            return ((PolygonCollisionObject) objectA).isSeparatedFrom((PolygonCollisionObject) objectB);
        }
        else if (checkType == CheckType.COMP_TO_COMP) {

        }
        else if (checkType == CheckType.POLY_TO_CIRC) {
            if (objectA.objectType == ObjectType.TYPE_POLYGON)
                return CHECK_SEPARATE_POLY_TO_CIRC((PolygonCollisionObject) objectA, (CircleCollisionObject) objectB);
            else
                return CHECK_SEPARATE_POLY_TO_CIRC((PolygonCollisionObject) objectB, (CircleCollisionObject) objectA);
        }
        else if (checkType == CheckType.POLY_TO_COMP) {

        }

        if (objectA.objectType == ObjectType.TYPE_CIRCLE)
            return CHECK_SEPARATE_CIRC_TO_COMP((CircleCollisionObject) objectA, (ComplexCollisionObject) objectB);
        return CHECK_SEPARATE_CIRC_TO_COMP((CircleCollisionObject) objectB, (ComplexCollisionObject) objectA);
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

    protected static boolean CHECK_SEPARATE_POLY_TO_CIRC(PolygonCollisionObject polygonCollisionObject, CircleCollisionObject circleCollisionObject) {
        ArrayList<MathVector> separateAxes = polygonCollisionObject.getLeftNormals();

        MathPoint circleOrigin = circleCollisionObject.getOrigin();
        double circleRadius = circleCollisionObject.getRadius();

        for (MathVector separateAxis: separateAxes) {
            double[] minmax = polygonCollisionObject.getMinMax(separateAxis);
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
    protected static boolean CHECK_SEPARATE_POLY_TO_COMP(PolygonCollisionObject polygonCollisionObject, ComplexCollisionObject complexCollisionObject) {
        return false;
    }
    protected static boolean CHECK_SEPARATE_CIRC_TO_COMP(CircleCollisionObject circleCollisionObject, ComplexCollisionObject complexCollisionObject) {
        return false;
    }

    protected final ObjectType objectType;

    protected MathPoint referenceLocation;

    protected CollisionDetector collisionDetector;

    protected Movable movable;
    protected Rotatable rotatable;

    protected AABBConverter aabbConverter;

    protected CollisionObject(CollisionObject collisionObject) {
        this.objectType = collisionObject.objectType;

        this.referenceLocation = new MathPoint(collisionObject.referenceLocation);

        initInterface();
    }
    protected CollisionObject(ObjectType objectType) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint();

        initInterface();
    }
    protected CollisionObject(ObjectType objectType, MathPoint referenceLocation) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint(referenceLocation);

        initInterface();
    }
    protected CollisionObject(
            ObjectType objectType,
            MathPoint referenceLocation,
            CollisionDetector collisionDetector,
            Movable movable,
            Rotatable rotatable,
            AABBConverter aabbConverter) {
        this.objectType = objectType;

        this.referenceLocation = new MathPoint(referenceLocation);

        this.collisionDetector = collisionDetector;
        this.movable = movable;
        this.rotatable = rotatable;
        this.aabbConverter = aabbConverter;
    }

    protected abstract void initInterface();

    public void move(MathVector movement) throws UnsupportedMovementException {
        if (movable == null)
            throw new UnsupportedMovementException("Movable is not assigned");
        movable.moveObject(movement);
    }
    public void move(MathPoint destination) throws UnsupportedMovementException {
        move(new MathVector(referenceLocation, destination));
    }

    public void rotate(double angle) throws UnsupportedRotationException {
        if (rotatable == null)
            throw new UnsupportedRotationException("Rotatable is not assigned");

        rotatable.rotateObject(angle);

    }

    public boolean isCollidedWith(CollisionObject hostileObject)
            throws UnsupportedCollisionDetectionException, IllegalArgumentException {
        return !isSeparatedFrom(hostileObject);
    }

    public boolean isSeparatedFrom(CollisionObject hostileObject)
            throws UnsupportedCollisionDetectionException, IllegalArgumentException {
        if (hostileObject == null)
            throw new IllegalArgumentException("Hostile object cannot be null");
        if (collisionDetector == null)
            throw new UnsupportedCollisionDetectionException("CollisionDetector is not assigned");

        return switch (hostileObject.getObjectType()) {
            case TYPE_AABB -> collisionDetector.isSeparatedFrom((AABBCollisionObject) hostileObject);
            case TYPE_POLYGON -> collisionDetector.isSeparatedFrom((PolygonCollisionObject) hostileObject);
            case TYPE_CIRCLE -> collisionDetector.isSeparatedFrom((CircleCollisionObject) hostileObject);
            case TYPE_COMPLEX -> collisionDetector.isSeparatedFrom((ComplexCollisionObject) hostileObject);
        };
    }

    public void setReferenceLocation(MathPoint referenceLocation) {
        move(referenceLocation);
    }

    public MathPoint getReferenceLocation() {
        return referenceLocation;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setCollisionDetector(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public void setMovable(Movable movable) {
        this.movable = movable;
    }

    public void setRotatable(Rotatable rotatable) {
        this.rotatable = rotatable;
    }

    public void setAABBConverter(AABBConverter aabbConverter) {
        this.aabbConverter = aabbConverter;
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public Movable getMovable() {
        return movable;
    }

    public Rotatable getRotatable() {
        return rotatable;
    }

    public AABBConverter getAABBConverter() {
        return aabbConverter;
    }

    public AABBCollisionObject toAABBCollisionObject() {
        return aabbConverter.convert();
    }

}
