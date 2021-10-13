package priv.kcl.practice.collision.complexshape;

public class UnsupportedMovementException extends RuntimeException {
    public UnsupportedMovementException() {
    }

    public UnsupportedMovementException(String message) {
        super(message);
    }

    public UnsupportedMovementException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedMovementException(Throwable cause) {
        super(cause);
    }

    public UnsupportedMovementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
