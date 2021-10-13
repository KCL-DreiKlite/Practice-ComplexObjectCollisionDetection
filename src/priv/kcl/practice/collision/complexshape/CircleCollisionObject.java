package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

public class CircleCollisionObject extends CollisionObject
        implements CollisionDetector, Movable, Rotatable, AABBConverter {

    protected double radius;

    public CircleCollisionObject() {
        super(ObjectType.TYPE_CIRCLE, new MathPoint(0, 0));

        this.radius = 0;
    }
    public CircleCollisionObject(CircleCollisionObject circleCollisionObject) {
        super(ObjectType.TYPE_CIRCLE, circleCollisionObject.referenceLocation);

        this.radius = circleCollisionObject.radius;
    }
    public CircleCollisionObject(MathPoint origin, double radius) {
        super(ObjectType.TYPE_CIRCLE, origin);

        this.radius = radius;
    }

    @Override
    protected void initInterface() {
        this.collisionDetector = this;
        this.movable = this;
        this.rotatable = this;
        this.aabbConverter = this;
    }

//    public boolean isCollidedTo(CircleCollisionObject hostileObject) {
//        return !isSeparatedTo(hostileObject);
//    }
//    public boolean isSeparatedTo(CircleCollisionObject hostileObject) {
//        double distanceBetweenTwoObject = new MathVector(referenceLocation, hostileObject.referenceLocation).length();
//
//        return distanceBetweenTwoObject > radius * 2;
//    }

    public MathPoint getOrigin() {
        return getReferenceLocation();
    }
    public double getRadius() {
        return radius;
    }


    @Override
    public boolean isSeparatedFrom(AABBCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException("Unable detect collision between AABBCollisionObject and CircleCollisionObject");
    }

    @Override
    public boolean isSeparatedFrom(PolygonCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        ArrayList<MathVector> separateAxes = hostileObject.getLeftNormals();

        MathPoint circleOrigin = getOrigin();
        double circleRadius = getRadius();

        for (MathVector separateAxis: separateAxes) {
            double[] minmax = hostileObject.getMinMax(separateAxis);
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
    public boolean isSeparatedFrom(CircleCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        double distanceBetweenTwoObject = new MathVector(referenceLocation, hostileObject.referenceLocation).length();
        return distanceBetweenTwoObject > this.radius + hostileObject.radius;
    }

    @Override
    public boolean isSeparatedFrom(ComplexCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        return hostileObject.isSeparatedFrom(this);
    }


    @Override
    public void moveObject(MathVector movement) throws UnsupportedMovementException {
        referenceLocation.move(movement);
    }

    @Override
    public void rotateObject(double angle) throws UnsupportedRotationException {
        throw new UnsupportedRotationException("Cannot rotate CircleCollisionObject");
    }

    @Override
    public AABBCollisionObject convert() {
        MathPoint topLeftVertex = new MathPoint(referenceLocation.x - radius, referenceLocation.y - radius);
        MathPoint bottomRightVertex = new MathPoint(referenceLocation.x + radius, referenceLocation.y + radius);

        return new AABBCollisionObject(referenceLocation, topLeftVertex, bottomRightVertex);
    }
}
