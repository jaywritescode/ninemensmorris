package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.Action;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.Move;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

// TODO: Can this class be consolidated into MinimaxPlayer?
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
    public MinimaxState apply(MinimaxState predecessor) {
        return MinimaxState.create(predecessor, this);
    }

    public List<Function<Board, Move>> makeChain(Piece toMove) {
        List<Function<Board, Move>> moves = new LinkedList<>();

        if (placePiece != null) {
            moves.add(board -> PlacePiece.createLegal(toMove, board.getPoint(placePiece)));
        }
        if (movePieceFrom != null && movePieceTo != null) {
            moves.add(board -> MovePiece.createLegal(toMove, board.getPoint(movePieceFrom), board.getPoint(movePieceTo),
                    board.getOccupiedPoints(toMove).size() == 3));
        }
        if (capturePiece != null) {
            moves.add(board -> CapturePiece.createLegal(toMove, board.getPoint(capturePiece)));
        }

        return moves;
    }

    MinimaxAction withCapture(Coordinate capturePiece) {
        this.capturePiece = capturePiece;
        return this;
    }

    public Coordinate getPlacePiece() {
        return placePiece;
    }

    public Coordinate getMovePieceFrom() {
        return movePieceFrom;
    }

    public Coordinate getMovePieceTo() {
        return movePieceTo;
    }

    public Coordinate getCapturePiece() {
        return capturePiece;
    }

    public boolean isPlacePiece() { return placePiece != null; }

    public boolean isMovePiece() { return movePieceFrom != null && movePieceTo != null; }

    public boolean isCapturePiece() { return capturePiece != null; }

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

    @Override public String toString() {
        return pretty();
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
