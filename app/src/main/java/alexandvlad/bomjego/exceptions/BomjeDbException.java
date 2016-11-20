package alexandvlad.bomjego.exceptions;

public class BomjeDbException extends Exception {

    public BomjeDbException() {}

    public BomjeDbException(String message) {
        super(message);
    }

    public BomjeDbException(String message, Throwable throwable) {
        super(message, throwable);
    }

    private static final String TAG = "BomjeDbException";
}
