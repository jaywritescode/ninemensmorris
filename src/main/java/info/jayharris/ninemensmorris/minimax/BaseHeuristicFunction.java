package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.HeuristicEvaluationFunction;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;

public abstract class BaseHeuristicFunction implements HeuristicEvaluationFunction<MinimaxState> {

    protected final Piece piece;

    public BaseHeuristicFunction(Piece piece) {
        this.piece = piece;
    }

    @Override
    public double apply(MinimaxState state) {
        Board board = state.copyBoard();

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
