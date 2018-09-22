package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

public class RemovePiece extends Action {

    @Override
    void perform(Point point) throws IllegalArgumentException {
        if (!point.isOccupied()) {
            throw new IllegalArgumentException();
        }

        point.setPiece(null);
    }

    /**
     * Creates an action remove a piece from the board.
     *
     * @return the Action
     */
    public static RemovePiece create() {
        return new RemovePiece();
    }
}
