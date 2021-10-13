package priv.kcl.practice.collision.complexshape;

public class UnsupportedCollisionDetectionException extends RuntimeException {
    public UnsupportedCollisionDetectionException() {
    }

    public UnsupportedCollisionDetectionException(String message) {
        super(message);
    }

    public UnsupportedCollisionDetectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedCollisionDetectionException(Throwable cause) {
        super(cause);
    }

    public UnsupportedCollisionDetectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
