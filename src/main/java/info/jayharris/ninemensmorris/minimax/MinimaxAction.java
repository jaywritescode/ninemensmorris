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

        if (placePiece != null) {
            copy.getPoint(placePiece).setPiece(initialState.getToMove());
        }

        if (capturePiece != null) {
            copy.getPoint(capturePiece).setPiece(null);
        }

        return new MinimaxState(copy, initialState.getToMove().opposite(), initialState.getPlayerPieces() - 1);
    }

    MinimaxAction withCapture(Coordinate capturePiece) {
        this.capturePiece = capturePiece;
        return this;
    }

    public String pretty() {
        StringBuilder sb = new StringBuilder();

        if (placePiece != null) {
            sb.append(placePiece.pretty());
        }
        else if (movePieceFrom != null && movePieceTo != null) {
            sb.append(movePieceFrom.pretty()).append("-").append(movePieceTo.pretty());
        }
        else {
            return "invalid";
        }

        if (capturePiece != null) {
            sb.append("x").append(capturePiece.pretty());
        }
        return sb.toString();
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

    static MinimaxAction fromPlacePiece(Coordinate placePiece) {
        return new MinimaxAction(placePiece);
    }

    static MinimaxAction fromMovePiece(Coordinate movePieceFrom, Coordinate movePieceTo) {
        return new MinimaxAction(movePieceFrom, movePieceTo);
    }

}
