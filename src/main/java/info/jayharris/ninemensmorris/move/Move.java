package info.jayharris.ninemensmorris.move;

public interface Move {

    /**
     * Validates and performs the move.
     *
     * @throws IllegalMoveException if the move is illegal
     */
    void perform() throws IllegalMoveException;

    String pretty();
}
