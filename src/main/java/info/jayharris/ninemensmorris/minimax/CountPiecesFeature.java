package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;

public class CountPiecesFeature extends Feature {

    public CountPiecesFeature(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        Board board = state.copyBoard();
        return board.getOccupiedPoints(getPiece()).size();
    }
}
