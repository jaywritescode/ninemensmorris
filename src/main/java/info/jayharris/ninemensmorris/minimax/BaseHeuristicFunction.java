package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;

public class BaseHeuristicFunction {

    protected final Piece piece;

    public BaseHeuristicFunction(Piece piece) {
        this.piece = piece;
    }

    public double apply(MinimaxState state) {
        Board board = state.getBoard();

        if (state.getPlayerPieces() == 0) {
            if (BoardUtils.isWinner(board, piece)) {
                return Double.POSITIVE_INFINITY;
            }
            if (BoardUtils.isWinner(board, piece.opposite())) {
                return Double.NEGATIVE_INFINITY;
            }
        }

        return 0;
    }
}
