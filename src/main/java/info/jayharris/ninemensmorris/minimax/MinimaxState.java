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
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinimaxState extends BaseState<MinimaxState, MinimaxAction> {

    Board board;
    Piece toMove;
    int playerPieces;

    private MinimaxState(Board board, Piece toMove, int playerPieces) {
        this.board = board;
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

    private Collection<MinimaxAction> getPlacePieceActions() {
        Board scratchPad = Board.copy(board);

        return scratchPad.getUnoccupiedPoints().stream()
                .flatMap(tryPlacePiece(scratchPad))
                .collect(Collectors.toSet());
    }

    private Collection<MinimaxAction> getMovePieceActions() {
        Board scratchPad = Board.copy(board);
        Set<Point> initialPoints = board.getOccupiedPoints(toMove);

        Function<Point, Stream<MinimaxAction>> moveFunction = initialPoints.size() == 3
                ? point -> tryMovePieceAnywhere(point, scratchPad)
                : point -> tryMovePieceToNeighbor(point, scratchPad);

        return initialPoints.stream()
                .flatMap(moveFunction)
                .collect(Collectors.toSet());
    }

    private Function<Point, Stream<MinimaxAction>> tryPlacePiece(Board board) {
        return point -> {
            InitialMove move = PlacePiece.create(toMove, point);
            move.perform();

            MinimaxAction.Builder builder = MinimaxAction.createWithInitialMove(move);

            Stream<MinimaxAction> stream;
            if (board.isCompleteMill(move.getUpdatedPoint())) {
                stream = board.getOccupiedPoints(toMove.opposite()).stream()
                        .map(capturePoint -> builder.withCapture(CapturePiece.create(toMove, capturePoint)));
            }
            else {
                stream = Stream.of(builder.withNoCapture());
            }

            // clean up
            point.setPiece(null);

            return stream;
        };
    }

//    private Stream<MinimaxAction> tryPlacePiece(Point point) {
//        InitialMove move = PlacePiece.create(toMove, point);
//        move.perform();
//
//        MinimaxAction.Builder builder = MinimaxAction.createWithInitialMove(move);
//
//        Stream<MinimaxAction> stream;
//        if (move.getUpdatedPoint().getBoard().isCompleteMill()) {
//            stream = board.getOccupiedPoints(toMove.opposite()).stream()
//                    .map(capturePoint -> builder.withCapture(CapturePiece.create(toMove, capturePoint)));
//        }
//        else {
//            stream = Stream.of(builder.withNoCapture());
//        }
//
//        // clean up
//        point.setPiece(null);
//
//        return stream;
//    }

    private Stream<MinimaxAction> tryMovePieceAnywhere(Point initial, Board scratchPad) {
        return scratchPad.getUnoccupiedPoints().stream()
                .map(destination -> MovePiece.create(toMove, scratchPad, initial, destination))
                .flatMap(move -> tryMoveAndCapture(initial, move, scratchPad));
    }

    private Stream<MinimaxAction> tryMovePieceToNeighbor(Point initial, Board scratchPad) {
        return initial.getNeighbors().stream()
                .map(destination -> MovePiece.create(toMove, scratchPad, initial, destination))
                .flatMap(move -> tryMoveAndCapture(initial, move, scratchPad));
    }

    private Stream<MinimaxAction> tryMoveAndCapture(Point initial, InitialMove move, Board scratchPad) {
        move.perform();

        MinimaxAction.Builder builder = MinimaxAction.createWithInitialMove(move);

        Stream<MinimaxAction> stream;
        if (board.isCompleteMill(move.getUpdatedPoint())) {
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

    private PlacePiece createPlacePiece(Point point) {
        return PlacePiece.create(toMove, point);
    }

    private CapturePiece createCapturePiece(Point point) {
        return CapturePiece.create(toMove, point);
    }

    @Override
    public OptionalLong utility() {
        return null;
    }

    public static MinimaxState initialState(Piece toMove) {
        return new MinimaxState(new Board(), toMove, 9);
    }
}
