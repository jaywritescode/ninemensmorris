package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.BaseState;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.util.Collection;
import java.util.OptionalLong;
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
        return Board.copy(board).getUnoccupiedPoints().stream()
                .flatMap(this::tryPlacePiece)
                .collect(Collectors.toSet());
    }

    private Collection<MinimaxAction> getMovePieceActions() {
        return null;
    }

    private Stream<MinimaxAction> tryPlacePiece(Point point) {
        InitialMove move = PlacePiece.create(toMove, point);
        move.perform();

        MinimaxAction action = MinimaxAction.createWithInitialMove(move);

        Stream<MinimaxAction> stream;
        if (board.isCompleteMill(point)) {
            stream = board.getOccupiedPoints(toMove.opposite()).stream()
                    .map(capturePoint -> action.withCapture(CapturePiece.create(toMove, capturePoint)));
        }
        else {
            stream = Stream.of(action);
        }

        // clean up
        point.setPiece(null);

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
}
