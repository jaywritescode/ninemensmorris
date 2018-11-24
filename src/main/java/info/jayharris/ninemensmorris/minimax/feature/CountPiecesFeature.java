package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

/**
 * This feature counts the number of one player's pieces on the board.
 */
public class CountPiecesFeature extends Feature {

    public CountPiecesFeature(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        return BoardUtils.countPieces(state.copyBoard(), piece);
    }
}
