package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

/**
 * This feature counts the number of empty squares that a player can move to.
 */
public class MobilityFeature extends Feature {

    public MobilityFeature(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        Board board = state.copyBoard();
        int myPieces = BoardUtils.countPieces(board, piece);

        if (myPieces == 3) {
            return BoardUtils.NUM_POINTS - myPieces - BoardUtils.countPieces(board, piece.opposite());
        }

        return board.getOccupiedPoints(piece)
                .stream()
                .mapToLong(point -> point.getNeighbors().stream().filter(Point::isUnoccupied).count())
                .sum();
    }
}
