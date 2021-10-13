package priv.kcl.practice.collision.complexshape;

public class UnsupportedRotationException extends RuntimeException {
    public UnsupportedRotationException() {
    }

    public UnsupportedRotationException(String message) {
        super(message);
    }

    public UnsupportedRotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedRotationException(Throwable cause) {
        super(cause);
    }

    public UnsupportedRotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
