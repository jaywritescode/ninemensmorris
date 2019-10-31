package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.feature.MobilityFeature;

/**
 * A heuristic function that prefers moves that leave the player with greater
 * mobility and also that leave the opponent with less mobility.
 */
public class ComparativeMobilityHeuristicFunction extends AbstractHeuristicFunction {

    public ComparativeMobilityHeuristicFunction(Piece myPiece) {
        super(myPiece);
    }

    @Override
    public double applyAsDouble(MinimaxState state) {
        Board board = state.getBoard();

        if (BoardUtils.isEmpty(board)) {
            return 4;
        }

        double myMobility = new MobilityFeature(piece).apply(state);
        double theirMobility = new MobilityFeature(piece.opposite()).apply(state);

        return myMobility / theirMobility;
    }
}
