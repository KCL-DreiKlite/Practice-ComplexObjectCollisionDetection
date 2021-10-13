package priv.kcl.practice.collision.complexshape;

public class UnsupportedCollisionPartException extends RuntimeException {
    public UnsupportedCollisionPartException() {
    }

    public UnsupportedCollisionPartException(String message) {
        super(message);
    }

    public UnsupportedCollisionPartException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedCollisionPartException(Throwable cause) {
        super(cause);
    }

    public UnsupportedCollisionPartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
