package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board.Point;

/**
 * An action performed against a point on the board —— either putting a piece
 * on the board or removing a piece from the board.
 */
public abstract class Action {

    /**
     * Performs the action.
     *
     * @param point the point
     */
    abstract void perform(Point point);
}
