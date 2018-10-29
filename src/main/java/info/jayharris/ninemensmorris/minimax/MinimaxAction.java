package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.Action;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Coordinate;

import java.util.Objects;

public class MinimaxAction implements Action<MinimaxState, MinimaxAction> {

    private Coordinate placePiece;
    private Coordinate movePieceFrom;
    private Coordinate movePieceTo;
    private Coordinate capturePiece;

    private MinimaxAction(Coordinate placePiece) {
        this.placePiece = placePiece;
    }

    private MinimaxAction(Coordinate movePieceFrom, Coordinate movePieceTo) {
        this.movePieceFrom = movePieceFrom;
        this.movePieceTo = movePieceTo;
    }

    @Override
    public MinimaxState apply(MinimaxState initialState) {
        Board copy = initialState.copyBoard();

        return null;
    }

    MinimaxAction withCapture(Coordinate capturePiece) {
        this.capturePiece = capturePiece;
        return this;
    }

    static MinimaxAction fromPlacePiece(Coordinate placePiece) {
        return new MinimaxAction(placePiece);
    }

    static MinimaxAction fromMovePiece(Coordinate movePieceFrom, Coordinate movePieceTo) {
        return new MinimaxAction(movePieceFrom, movePieceTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimaxAction that = (MinimaxAction) o;
        return Objects.equals(placePiece, that.placePiece) &&
               Objects.equals(movePieceFrom, that.movePieceFrom) &&
               Objects.equals(movePieceTo, that.movePieceTo) &&
               Objects.equals(capturePiece, that.capturePiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placePiece, movePieceFrom, movePieceTo, capturePiece);
    }
}
