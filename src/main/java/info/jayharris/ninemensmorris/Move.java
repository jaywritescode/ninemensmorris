package info.jayharris.ninemensmorris;

public interface Move {

    /**
     * Validates and performs the move.
     *
     * @throws IllegalStateException if the move is illegal
     */
    void perform() throws IllegalStateException;
}
