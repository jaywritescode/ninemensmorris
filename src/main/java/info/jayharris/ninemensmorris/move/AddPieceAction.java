package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;

import static com.google.common.base.Preconditions.*;

public class AddPieceAction extends Action {

    Piece piece;

    AddPieceAction(Piece piece) {
        this.piece = piece;
    }

    /**
     * Adds {@code piece} to the given point.
     *
     * This method expects that {@code point.piece} will be null.
     *
     * @param point the point
     */
    @Override
    void perform(Point point) {
        checkArgument(!point.isOccupied(), "Expected point %s to be empty.", point.getId());
        point.setPiece(piece);
    }

    /**
     * Creates an action to place {@code piece} on the board.
     *
     * @param piece the piece
     * @return the Action
     */
    public static AddPieceAction create(Piece piece) {
        return new AddPieceAction(piece);
    }
}
