package priv.kcl.practice.collision.complexshape;

import java.util.ArrayList;

public class ComplexObject extends CollisionObject {

    protected ArrayList<CollisionObject> collisionObjects;
//    protected ArrayList<MathPoint> checkPoints;
    protected ArrayList<AABBObject> checkObjects;


    public ComplexObject() {
        super(ObjectType.TYPE_COMPLEX);

        this.collisionObjects = new ArrayList<>();
    }
    public ComplexObject(ComplexObject complexObject) {
        super(ObjectType.TYPE_COMPLEX, complexObject.referenceLocation);

        resetReferenceVertices();
    }
    public ComplexObject(MathPoint referenceLocation) {
        super(ObjectType.TYPE_COMPLEX, referenceLocation);

        this.collisionObjects = new ArrayList<>();

        resetReferenceVertices();
    }
    public ComplexObject(MathPoint referenceLocation, ArrayList<CollisionObject> collisionParts) throws IllegalArgumentException {
        super(ObjectType.TYPE_COMPLEX, referenceLocation);

        if (collisionParts.stream().anyMatch(part -> part.objectType == ObjectType.TYPE_AABB))
            throw new IllegalArgumentException("Complex Collision Object does not accept any AABB Object");

        this.collisionObjects = new ArrayList<>(collisionParts);

        resetReferenceVertices();
    }

    protected void resetReferenceVertices() {
        checkObjects = new ArrayList<>(collisionObjects.size());

        for (CollisionObject element : collisionObjects)
            checkObjects.add(element.toAABBObject());
    }

    public void addPart(CollisionObject element) throws IllegalArgumentException {
        if (element.getObjectType() == ObjectType.TYPE_AABB)
            throw new IllegalArgumentException("Complex Collision Object does not accept AABB object");

        collisionObjects.add(element);
    }

    public boolean removePart(CollisionObject element) {
        return collisionObjects.remove(element);
    }

    public void clearParts() {
        collisionObjects = new ArrayList<>();

    }

    public boolean isCollidedTo(ComplexObject hostileObject) {
        return !isSeparatedTo(hostileObject);
    }

    public boolean isSeparatedTo(ComplexObject hostileObject) {
        return true;
    }

    @Override
    public void move(MathVector movement) {
        super.move(movement);
        
        collisionObjects.forEach(element -> element.move(movement));
    }

    @Override
    public void move(MathPoint destination) {
        super.move(destination);
    }

    @Override
    public void rotate(double angle) {
        super.rotate(angle);
    }

    @Override
    public AABBObject toAABBObject() {
        return super.toAABBObject();
    }
}
