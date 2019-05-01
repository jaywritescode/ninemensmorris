package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.Turn;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

public abstract class TwoStageTurnPlayer extends BasePlayer {

    TwoStageTurnPlayer(Piece piece) {
        super(piece);
    }

    public Turn takeTurn(Board board) {
        Turn turn = Turn.initialize(this, board);

        if (startingPieces > 0) {
            turn.doInitialMove(placePiece(board));
            --startingPieces;
        }
        else {
            turn.doInitialMove(movePiece(board));
        }

        if (board.isCompleteMill(turn.getUpdatedPoint())) {
            turn.doCaptureMove(capturePiece(board));
        }

        return turn;
    }

    protected abstract PlacePiece placePiece(Board board);

    protected abstract MovePiece movePiece(Board board);

    protected abstract CapturePiece capturePiece(Board board);
}
