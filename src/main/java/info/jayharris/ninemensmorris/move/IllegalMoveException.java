package info.jayharris.ninemensmorris.move;

public class IllegalMoveException extends RuntimeException {

    IllegalMoveException(String message) {
        super(message);
    }

    public static IllegalMoveException create(String message) {
        return new IllegalMoveException(message);
    }

    public static IllegalMoveException create(String template, String... args) {
        return IllegalMoveException.create(String.format(template, (Object) args));
    }
}
