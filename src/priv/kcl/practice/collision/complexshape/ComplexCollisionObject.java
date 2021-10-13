package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

public class ComplexCollisionObject extends CollisionObject
        implements CollisionDetector, Movable, Rotatable, AABBConverter {

    protected static final int FRIENDLY_PART = 0;
    protected static final int HOSTILE_PART = 1;

    protected ArrayList<CollisionObject> referenceCollisionParts;
    protected ArrayList<CollisionObject> rotatedCollisionParts;

    protected ArrayList<AABBCollisionObject> checkObjects;


    public ComplexCollisionObject() {
        super(ObjectType.TYPE_COMPLEX);

        this.referenceCollisionParts = new ArrayList<>();
    }
    public ComplexCollisionObject(ComplexCollisionObject complexCollisionObject) {
        super(ObjectType.TYPE_COMPLEX, complexCollisionObject.referenceLocation);

        referenceCollisionParts = new ArrayList<>(complexCollisionObject.referenceCollisionParts.size());
        for (CollisionObject element : complexCollisionObject.referenceCollisionParts) {
            switch (element.getObjectType()) {
                case TYPE_POLYGON ->
                        referenceCollisionParts.add(new PolygonCollisionObject((PolygonCollisionObject) element));
                case TYPE_CIRCLE ->
                        referenceCollisionParts.add(new CircleCollisionObject((CircleCollisionObject) element));
                default -> {}
            }
        }

        resetRotatedCollisionParts();
    }
    public ComplexCollisionObject(MathPoint referenceLocation) {
        super(ObjectType.TYPE_COMPLEX, referenceLocation);

        this.referenceCollisionParts = new ArrayList<>();

        resetRotatedCollisionParts();
    }
    public ComplexCollisionObject(MathPoint referenceLocation, ArrayList<CollisionObject> collisionParts)
            throws IllegalArgumentException {
        super(ObjectType.TYPE_COMPLEX, referenceLocation);

        if (collisionParts.stream().anyMatch(part ->
                part.objectType == ObjectType.TYPE_AABB || part.objectType == ObjectType.TYPE_COMPLEX))
            throw new IllegalArgumentException(
                    "ComplexCollisionObject does not accept AABBCollisionObject or ComplexCollisionObject as part");

        this.referenceCollisionParts = collisionParts;

        resetRotatedCollisionParts();
    }

    protected ComplexCollisionObject(MathPoint referenceLocation, CollisionObject singleCollisionPart) {
        super(ObjectType.TYPE_COMPLEX, referenceLocation);

        this.referenceCollisionParts = new ArrayList<>(1);
        this.referenceCollisionParts.add(singleCollisionPart);
        this.rotatedCollisionParts = new ArrayList<>(referenceCollisionParts);
    }

    @Override
    protected void initInterface() {
        this.collisionDetector = this;
        this.movable = this;
        this.rotatable = this;
        this.aabbConverter = this;
    }

    protected void resetRotatedCollisionParts() {
        rotatedCollisionParts = new ArrayList<>(referenceCollisionParts.size());

        for (CollisionObject part : referenceCollisionParts) {
            switch (part.getObjectType()) {
                case TYPE_POLYGON -> rotatedCollisionParts.add(new PolygonCollisionObject((PolygonCollisionObject) part));
                case TYPE_CIRCLE -> rotatedCollisionParts.add(new CircleCollisionObject((CircleCollisionObject) part));
                default -> throw new UnsupportedCollisionPartException(
                        "ComplexCollisionObject does not accept AABBCollision or ComplexCollisionObject as part");
            }
        }

//        checkObjects = new ArrayList<>(referenceCollisionParts.size());

//        for (CollisionObject element : referenceCollisionParts)
//            checkObjects.add(element.toAABBCollisionObject());

    }


    public void addPart(CollisionObject element) throws IllegalArgumentException {
        if (element.getObjectType() == ObjectType.TYPE_AABB)
            throw new IllegalArgumentException("ComplexCollisionObject does not accept AABBCollisionObject as part");
        else if (element.getObjectType() == ObjectType.TYPE_COMPLEX)
            throw new IllegalArgumentException("ComplexCollisionObject does not accept ComplexCollisionObject as part");

        referenceCollisionParts.add(element);
    }

    public boolean removePart(CollisionObject element) {
        return referenceCollisionParts.remove(element);
    }

    public void clearParts() {
        referenceCollisionParts = new ArrayList<>();
        resetRotatedCollisionParts();
    }

    public ArrayList<CollisionObject> getReferenceCollisionParts() {
        return referenceCollisionParts;
    }

    public ArrayList<CollisionObject> getRotatedCollisionParts() {
        return rotatedCollisionParts;
    }

    @Override
    public AABBCollisionObject convert() {
        double minX = rotatedCollisionParts.get(0).getReferenceLocation().x;
        double maxX = minX;
        double minY = rotatedCollisionParts.get(0).getReferenceLocation().y;
        double maxY = minY;

        for (CollisionObject element : rotatedCollisionParts) {
            switch (element.getObjectType()) {
                case TYPE_POLYGON -> {
                    PolygonCollisionObject polygonElement = (PolygonCollisionObject) element;
                    ArrayList<MathPoint> rotatedVertices = polygonElement.getRotatedVertices();
                    for (MathPoint vertex : rotatedVertices) {
                        minX = Math.min(minX, vertex.x);
                        maxX = Math.max(maxX, vertex.x);
                        minY = Math.min(minY, vertex.y);
                        maxY = Math.max(maxY, vertex.y);
                    }
                }
                case TYPE_CIRCLE -> {
                    CircleCollisionObject circleElement = (CircleCollisionObject) element;
                    MathPoint referenceLocation = circleElement.getReferenceLocation();
                    double radius = circleElement.getRadius();

                    minX = Math.min(minX, referenceLocation.x - radius);
                    maxX = Math.max(maxX, referenceLocation.x + radius);
                    minY = Math.min(minY, referenceLocation.y - radius);
                    maxY = Math.max(maxY, referenceLocation.y + radius);
                }
                default -> {}
            }
        }
        return null;
    }

    @Override
    public boolean isSeparatedFrom(AABBCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        throw new UnsupportedCollisionDetectionException(
                "Unable detect collision between AABBCollisionObject and ComplexCollisionObject");
    }

    @Override
    public boolean isSeparatedFrom(PolygonCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        return isSeparatedFrom(convertSingleObjectToComplexCollisionObject(hostileObject));
    }

    @Override
    public boolean isSeparatedFrom(CircleCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        return isSeparatedFrom(convertSingleObjectToComplexCollisionObject(hostileObject));
    }

    protected ComplexCollisionObject convertSingleObjectToComplexCollisionObject(CollisionObject hostileObject) {
        MathPoint hostileObjectRefLocation = hostileObject.getReferenceLocation();
//        ArrayList<CollisionObject> hostileObjectRefParts = new ArrayList<>(1);
//        hostileObjectRefParts.add(hostileObject);
//        return new ComplexCollisionObject(hostileObjectRefLocation, hostileObjectRefParts);
        return new ComplexCollisionObject(hostileObjectRefLocation, hostileObject);
    }

    @Override
    public boolean isSeparatedFrom(ComplexCollisionObject hostileObject) throws UnsupportedCollisionDetectionException {
        ArrayList<CollisionObject[]> detectionPairs =
                getDetectionPairs(rotatedCollisionParts, hostileObject.rotatedCollisionParts);

        if (detectionPairs.isEmpty())
            return true;

        for (CollisionObject[] eachPair : detectionPairs) {
            CollisionObject friendlyPart = eachPair[FRIENDLY_PART];
            CollisionObject hostilePart = eachPair[HOSTILE_PART];

            if (friendlyPart.isCollidedWith(hostilePart))
                return false;
        }

        return true;
    }

    protected ArrayList<CollisionObject[]> getDetectionPairs(
            ArrayList<CollisionObject> friendlyObjects,
            ArrayList<CollisionObject> hostileObjects) {
        ArrayList<CollisionObject[]> result = new ArrayList<>();

        for (CollisionObject friendlyPart : friendlyObjects) {
            for (CollisionObject hostilePart : hostileObjects) {
                AABBCollisionObject friendlyAABB = friendlyPart.toAABBCollisionObject();
                AABBCollisionObject hostileAABB = hostilePart.toAABBCollisionObject();

                if (friendlyAABB.isCollidedWith(hostileAABB))
                    result.add(new CollisionObject[]{friendlyPart, hostilePart});
            }
        }

        return result;
    }


    @Override
    public void moveObject(MathVector movement) throws UnsupportedMovementException {
        referenceLocation.move(movement);

        referenceCollisionParts.forEach(part -> part.move(movement));
        rotatedCollisionParts.forEach(part -> part.move(movement));
    }

    @Override
    public void rotateObject(double angle) throws UnsupportedRotationException {
        MathPoint originRefLocation = new MathPoint(referenceLocation);

        move(MathPoint.ORIGIN_POINT);
        resetRotatedCollisionParts();

        for (CollisionObject element : rotatedCollisionParts) {
            switch (element.getObjectType()) {
                case TYPE_POLYGON -> rotatePolygonPart((PolygonCollisionObject) element, angle);
                case TYPE_CIRCLE -> rotateCirclePart((CircleCollisionObject) element, angle);
                default -> throw new UnsupportedRotationException(
                        "ComplexCollisionObject does not accept AABBCollision or ComplexCollisionObject as part");
            }
        }

        move(originRefLocation);
    }

    protected void rotatePolygonPart(PolygonCollisionObject targetCollisionObject, double angle) {
        MathPoint referenceLocation = targetCollisionObject.getReferenceLocation();
        ArrayList<MathPoint> referenceVertices = targetCollisionObject.getReferenceVertices();
        ArrayList<MathPoint> rotatedVertices = targetCollisionObject.getRotatedVertices();

        for (int currentVertexIndex = 0; currentVertexIndex < referenceVertices.size(); currentVertexIndex++) {
            double x1 = referenceVertices.get(currentVertexIndex).x;
            double y1 = referenceVertices.get(currentVertexIndex).y;
            double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
            double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

            rotatedVertices.get(currentVertexIndex).setPoint(x2, y2);
        }
    }

    protected void rotateCirclePart(CircleCollisionObject targetCollisionObject, double angle) {
        MathPoint referenceLocation = targetCollisionObject.getReferenceLocation();

        double x1 = referenceLocation.x;
        double y1 = referenceLocation.y;
        double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
        double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

        referenceLocation.setPoint(x2, y2);
    }

//    public boolean isCollidedTo(ComplexCollisionObject hostileObject) {
//        return !isSeparatedTo(hostileObject);
//    }
//
//    public boolean isSeparatedTo(ComplexCollisionObject hostileObject) {
//        return true;
//    }
//
//    @Override
//    public void move(MathVector movement) {
//        referenceLocation.move(movement);
//        referenceCollisionParts.forEach(element -> element.move(movement));
//    }
//
//    @Override
//    public void move(MathPoint destination) {
//        move(new MathVector(referenceLocation, destination));
//    }
//
//    @Override
//    public void rotate(double angle) {
//
//    }
//
//    @Override
//    public AABBCollisionObject toAABBCollisionObject() {
//        return super.toAABBCollisionObject();
//    }
}
