package priv.kcl.practice.collision.complexshape;

public interface CollisionDetector {
    boolean isSeparatedFrom(AABBCollisionObject hostileObject) throws UnsupportedCollisionDetectionException;
    boolean isSeparatedFrom(PolygonCollisionObject hostileObject) throws UnsupportedCollisionDetectionException;
    boolean isSeparatedFrom(CircleCollisionObject hostileObject) throws UnsupportedCollisionDetectionException;
    boolean isSeparatedFrom(ComplexCollisionObject hostileObject) throws UnsupportedCollisionDetectionException;
}
