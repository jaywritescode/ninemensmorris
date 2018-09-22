package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

public class RemovePieceAction extends Action {

    /**
     * Removes the piece at the given point.
     *
     * For purposes of symmetry, this method expects {@code point} to be non-empty.
     *
     * @param point the point
     */
    @Override
    void perform(Point point) {
        point.setPiece(null);
    }

    /**
     * Creates an action remove a piece from the board.
     *
     * @return the Action
     */
    public static RemovePieceAction create() {
        return new RemovePieceAction();
    }
}
