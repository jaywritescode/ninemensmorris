package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

public class AddPiece extends Action {

    Piece piece;

    AddPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    void perform(Point point) throws IllegalArgumentException {
        if (point.isOccupied()) {
            throw new IllegalArgumentException();
        }

        point.setPiece(piece);
    }

    /**
     * Creates an action to place {@code piece} on the board.
     *
     * @param piece the piece
     * @return the Action
     */
    public static AddPiece create(Piece piece) {
        return new AddPiece(piece);
    }
}
