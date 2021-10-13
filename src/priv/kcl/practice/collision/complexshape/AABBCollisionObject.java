package priv.kcl.practice.collision.complexshape;

public class AABBCollisionObject extends CollisionObject
        implements CollisionDetector, Movable, Rotatable, AABBConverter {

    protected MathPoint topLeftVertex;
    protected MathPoint bottomRightVertex;

    public AABBCollisionObject() {
        super(ObjectType.TYPE_AABB, new MathPoint(0, 0));
    }
    public AABBCollisionObject(AABBCollisionObject aabbObject) {
        super(aabbObject);

        this.topLeftVertex = new MathPoint(aabbObject.topLeftVertex);
        this.bottomRightVertex = new MathPoint(aabbObject.bottomRightVertex);
    }
    public AABBCollisionObject(MathPoint referenceLocation, MathPoint topLeftVertex, MathPoint bottomRightVertex) {
        super(ObjectType.TYPE_AABB, referenceLocation);

        getVerticesByDiagonalVertices(topLeftVertex, bottomRightVertex);
    }
    public AABBCollisionObject(MathPoint referenceLocation, int width, int height) {
        super(ObjectType.TYPE_AABB, referenceLocation);

        double lx = referenceLocation.x - width / 2d;
        double rx = referenceLocation.x + width / 2d;
        double ly = referenceLocation.y - height / 2d;
        double ry = referenceLocation.y + height / 2d;
        getVerticesByDiagonalVertices(new MathPoint(lx, ly), new MathPoint(rx, ry));
    }

    private void getVerticesByDiagonalVertices(MathPoint v1, MathPoint v2) {
        double x1 = Math.min(v1.x, v2.x);   // top-left vertex
        double y1 = Math.min(v1.y, v2.y);
        double x2 = Math.max(v1.x, v2.x);   // bottom-right vertex
        double y2 = Math.max(v1.y, v2.y);

        topLeftVertex = new MathPoint(x1, y1);
        bottomRightVertex = new MathPoint(x2, y2);
    }

    @Override
    protected void initInterface() {
        this.collisionDetector = this;
        this.movable = this;
        this.rotatable = this;
        this.aabbConverter = this;
    }

    public void setTopLeftVertex(MathPoint topLeftVertex) {
        this.topLeftVertex = topLeftVertex;
    }

    public void setBottomRightVertex(MathPoint bottomRightVertex) {
        this.bottomRightVertex = bottomRightVertex;
    }

    public MathPoint getTopLeftVertex() {
        return topLeftVertex;
    }

    public MathPoint getBottomRightVertex() {
        return bottomRightVertex;
    }

    @Override
    public boolean isSeparatedFrom(AABBCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        double aMinX = this.getTopLeftVertex().x;
        double aMaxX = this.getBottomRightVertex().x;
        double aMinY = this.getTopLeftVertex().y;
        double aMaxY = this.getBottomRightVertex().y;

        double bMinX = hostileObject.getTopLeftVertex().x;
        double bMaxX = hostileObject.getBottomRightVertex().x;
        double bMinY = hostileObject.getTopLeftVertex().y;
        double bMaxY = hostileObject.getBottomRightVertex().y;

        return !(aMaxX > bMinX &&
                bMaxX > aMinX &&
                aMaxY > bMinY &&
                bMaxY > aMinY);
    }

    @Override
    public boolean isSeparatedFrom(PolygonCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException("Unable detect collision between AABBCollisionObject and PolygonCollisionObject");
    }

    @Override
    public boolean isSeparatedFrom(CircleCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException("Unable detect collision between AABBCollisionObject and CircleCollisionObject");
    }

    @Override
    public boolean isSeparatedFrom(ComplexCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException("Unable detect collision between AABBCollisionObject and ComplexCollisionObject");
    }

    @Override
    public void moveObject(MathVector movement) throws UnsupportedMovementException {
        topLeftVertex.move(movement);
        bottomRightVertex.move(movement);
    }

    @Override
    public void rotateObject(double angle) throws UnsupportedRotationException {
        throw new UnsupportedRotationException("Cannot rotate AABBCollisionObject");
    }

    @Override
    public AABBCollisionObject convert() {
        return new AABBCollisionObject(this);
    }
}
