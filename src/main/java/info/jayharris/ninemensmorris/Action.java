package info.jayharris.ninemensmorris;

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
     * @throws IllegalArgumentException if the action cannot be performed
     */
    abstract void perform(Point point) throws IllegalArgumentException;
}
