package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.BaseState;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinimaxState extends BaseState<MinimaxState, MinimaxAction> {

    Board scratchPad;
    Piece toMove;
    int playerPieces;

    private MinimaxState(Board scratchPad, Piece toMove, int playerPieces) {
        this.scratchPad = scratchPad;
        this.toMove = toMove;
        this.playerPieces = playerPieces;
    }

    @Override
    public Collection<MinimaxAction> actions() {
        if (playerPieces > 0) {
            return getPlacePieceActions();
        }
        return getMovePieceActions();
    }

    @Override
    public double eval() {
        return 0;
    }

    private Collection<MinimaxAction> getPlacePieceActions() {
        return scratchPad.getUnoccupiedPoints().stream()
                .flatMap(this::tryPlacePiece)
                .collect(Collectors.toSet());
    }

    private Collection<MinimaxAction> getMovePieceActions() {
        Set<Point> initialPoints = scratchPad.getOccupiedPoints(toMove);

        return initialPoints.stream()
                .flatMap(initialPoints.size() == 3 ? this::tryMovePieceAnywhere : this::tryMovePieceToNeighbor)
                .collect(Collectors.toSet());
    }

    private Stream<MinimaxAction> tryPlacePiece(Point point) {
        InitialMove move = PlacePiece.create(toMove, point);
        move.perform();

        MinimaxAction.Builder builder = MinimaxAction.createWithInitialMove(move);

        Stream<MinimaxAction> stream;
        if (scratchPad.isCompleteMill(move.getUpdatedPoint())) {
            stream = scratchPad.getOccupiedPoints(toMove.opposite()).stream()
                    .map(capturePoint -> builder.withCapture(CapturePiece.create(toMove, capturePoint)));
        }
        else {
            stream = Stream.of(builder.withNoCapture());
        }

        // clean up
        point.setPiece(null);

        return stream;
    }

    private Stream<MinimaxAction> tryMovePieceAnywhere(Point initial) {
        return scratchPad.getUnoccupiedPoints().stream()
                .map(destination -> MovePiece.create(toMove, scratchPad, initial, destination))
                .flatMap(move -> tryMoveAndCapture(initial, move));
    }

    private Stream<MinimaxAction> tryMovePieceToNeighbor(Point initial) {
        return initial.getNeighbors().stream()
                .filter(Point::isUnoccupied)
                .map(destination -> MovePiece.create(toMove, scratchPad, initial, destination))
                .flatMap(move -> tryMoveAndCapture(initial, move));
    }

    private Stream<MinimaxAction> tryMoveAndCapture(Point initial, InitialMove move) {
        move.perform();

        MinimaxAction.Builder builder = MinimaxAction.createWithInitialMove(move);

        Stream<MinimaxAction> stream;
        if (scratchPad.isCompleteMill(move.getUpdatedPoint())) {
            stream = scratchPad.getOccupiedPoints(toMove.opposite()).stream()
                    .map(capturePoint -> builder.withCapture(CapturePiece.create(toMove, capturePoint)));
        }
        else {
            stream = Stream.of(builder.withNoCapture());
        }

        // clean up
        move.getUpdatedPoint().setPiece(null);
        initial.setPiece(toMove);

        return stream;
    }

    public static MinimaxState initialState(Piece toMove) {
        return new MinimaxState(new Board(), toMove, 9);
    }
}
