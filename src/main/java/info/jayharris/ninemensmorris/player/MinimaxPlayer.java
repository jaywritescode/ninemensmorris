package info.jayharris.ninemensmorris.player;

import info.jayharris.minimax.search.Search;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.Turn;
import info.jayharris.ninemensmorris.minimax.MinimaxAction;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.util.function.Supplier;

public class MinimaxPlayer extends BasePlayer {

    private Supplier<? extends Search<MinimaxState, MinimaxAction>> searchSupplier;

    public MinimaxPlayer(Piece piece, Supplier<? extends Search<MinimaxState, MinimaxAction>> searchSupplier) {
        super(piece);
        this.searchSupplier = searchSupplier;
    }

    @Override
    public Turn takeTurn(Board board) {
        MinimaxAction action = searchSupplier.get().perform(MinimaxState.create(board, this));

        Turn turn = Turn.initialize(this, board);

        if (action.isPlacePiece()) {
            turn.doInitialMove(PlacePiece.createLegal(piece, board.getPoint(action.getPlacePiece())));
            --startingPieces;
        }
        else {
            turn.doInitialMove(MovePiece.createLegal(
                    piece,
                    board.getPoint(action.getMovePieceFrom()),
                    board.getPoint(action.getMovePieceTo()),
                    board.getOccupiedPoints(piece).size() == 3));
        }

        if (action.isCapturePiece()) {
            turn.doCaptureMove(CapturePiece.createLegal(piece, board.getPoint(action.getCapturePiece())));
        }

        return turn;
    }
}
