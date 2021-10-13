package priv.kcl.practice.collision.complexshape;

public interface Movable {

    void moveObject(MathVector movement) throws UnsupportedMovementException;
//    void move(MathPoint destination);
}
