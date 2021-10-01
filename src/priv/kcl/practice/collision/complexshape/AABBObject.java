package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

public class AABBObject extends CollisionObject {

    public AABBObject() {
        super(ObjectType.TYPE_AABB, new MathPoint(0, 0));
    }
    public AABBObject(AABBObject aabbObject) {
        super(aabbObject);
    }
    public AABBObject(MathPoint referenceLocation, MathPoint topLeftVertex, MathPoint bottomRightVertex) {
        super(ObjectType.TYPE_AABB, referenceLocation);

        getVerticesByDiagonalVertices(topLeftVertex, bottomRightVertex);
    }
    public AABBObject(MathPoint referenceLocation, int width, int height) {
        super(ObjectType.TYPE_AABB, referenceLocation);

        double lx = referenceLocation.x - width / 2d;
        double rx = referenceLocation.x + width / 2d;
        double ly = referenceLocation.y - height / 2d;
        double ry = referenceLocation.y + height / 2d;
        getVerticesByDiagonalVertices(new MathPoint(lx, ly), new MathPoint(rx, ry));
    }

//    private void initializeVertices() {
//        topLeftVertex = new MathPoint(0, 0);
//        topRightVertex = new MathPoint(0, 0);
//        bottomLeftVertex = new MathPoint(0, 0);
//        bottomRightVertex = new MathPoint(0, 0);
//    }

    private void getVerticesByDiagonalVertices(MathPoint v1, MathPoint v2) {
        double x1 = Math.min(v1.x, v2.x);   // top-left vertex
        double y1 = Math.min(v1.y, v2.y);
        double x2 = Math.max(v1.x, v2.x);   // bottom-right vertex
        double y2 = Math.max(v1.y, v2.y);

        referenceVertices.add(new MathPoint(x1, y1));   // top-left vertex
        referenceVertices.add(new MathPoint(x2, y2));   // bottom-right vertex

//        topLeftVertex = new MathPoint(Math.min(x1, x2), Math.min(y1, y2));
//        topRightVertex = new MathPoint(Math.max(x1, x2), Math.min(y1, y2));
//        bottomLeftVertex = new MathPoint(Math.min(x1, x2), Math.max(y1, y2));
//        bottomRightVertex = new MathPoint(Math.max(x1, x2), Math.max(y1, y2));
    }

    public boolean isCollidedTo(AABBObject hostileObject) {
        // A = this
        // B = hostileObject
//        int aMinX = this.topLeftVertex.x;
//        int aMaxX = this.bottomRightVertex.x;
//        int aMinY = this.topLeftVertex.y;
//        int aMaxY = this.bottomRightVertex.y;
//
//        int bMinX = hostileObject.topLeftVertex.x;
//        int bMaxX = hostileObject.bottomRightVertex.x;
//        int bMinY = hostileObject.topLeftVertex.y;
//        int bMaxY = hostileObject.bottomRightVertex.y;
        double aMinX = this.getTopLeftVertex().x;
        double aMaxX = this.getBottomRightVertex().x;
        double aMinY = this.getTopLeftVertex().y;
        double aMaxY = this.getBottomRightVertex().y;

        double bMinX = hostileObject.getTopLeftVertex().x;
        double bMaxX = hostileObject.getBottomRightVertex().x;
        double bMinY = hostileObject.getTopLeftVertex().y;
        double bMaxY = hostileObject.getBottomRightVertex().y;

        return aMaxX > bMinX && bMaxX > aMinX &&
                aMaxY > bMinY && bMaxY > aMinY;
    }

    public boolean isSeparatedTo(AABBObject hostileObject) {
        return !isCollidedTo(hostileObject);
    }

    public ArrayList<MathPoint> getRotatedVertices() {
        ArrayList<MathPoint> result = new ArrayList<>(4);
        double minX = getTopLeftVertex().x;
        double maxX = getBottomRightVertex().x;
        double minY = getTopLeftVertex().y;
        double maxY = getBottomRightVertex().y;

        result.add(getTopLeftVertex());
        result.add(new MathPoint(maxX, minY));
        result.add(new MathPoint(minX, maxY));
        result.add(getBottomRightVertex());
        return result;
    }

    public ArrayList<MathPoint> getDiagonalVertices() {
        return new ArrayList<>(referenceVertices);
    }

    public MathPoint getTopLeftVertex() {
        if (referenceVertices.isEmpty())
            return null;
        return new MathPoint(referenceVertices.get(0));
    }
//    public MathPoint getTopRightVertex() {
//        return topRightVertex;
//    }
//    public MathPoint getBottomLeftVertex() {
//        return bottomLeftVertex;
//    }
    public MathPoint getBottomRightVertex() {
        if (referenceVertices.isEmpty())
            return null;
        return new MathPoint(referenceVertices.get(1));
    }

    public double getWidth() {
        if (referenceVertices.isEmpty())
            return 0d;
        return getBottomRightVertex().x - getTopLeftVertex().x;
    }
    public double getHeight() {
        if (referenceVertices.isEmpty())
            return 0d;
        return getBottomRightVertex().y - getTopLeftVertex().x;
    }
}
