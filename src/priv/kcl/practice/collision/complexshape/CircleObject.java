package priv.kcl.practice.collision.complexshape;

public class CircleObject extends CollisionObject {

    protected double radius;

    public CircleObject() {
        super(ObjectType.TYPE_CIRCLE, new MathPoint(0, 0));

        this.radius = 0;
    }
    public CircleObject(CircleObject circleObject) {
        super(ObjectType.TYPE_CIRCLE, circleObject.referenceLocation);

        this.radius = circleObject.radius;
    }
    public CircleObject(MathPoint origin, double radius) {
        super(ObjectType.TYPE_CIRCLE, origin);

        this.radius = radius;
    }


    public boolean isCollidedTo(CircleObject hostileObject) {
        return !isSeparatedTo(hostileObject);
    }
    public boolean isSeparatedTo(CircleObject hostileObject) {
        double distanceBetweenTwoObject = new MathVector(referenceLocation, hostileObject.referenceLocation).length();

        return distanceBetweenTwoObject > radius * 2;
    }

    public MathPoint getOrigin() {
        return getReferenceLocation();
    }
    public double getRadius() {
        return radius;
    }

    @Override
    public AABBObject toAABBObject() {
        MathPoint topLeftVertex = new MathPoint(referenceLocation.x - radius, referenceLocation.y - radius);
        MathPoint bottomRightVertex = new MathPoint(referenceLocation.x + radius, referenceLocation.y + radius);

        return new AABBObject(referenceLocation, topLeftVertex, bottomRightVertex);
    }
}
