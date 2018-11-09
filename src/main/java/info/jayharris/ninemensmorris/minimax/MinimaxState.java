package info.jayharris.ninemensmorris.minimax;

import com.google.common.base.Predicates;
import info.jayharris.minimax.BaseState;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Mill;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.MinimaxPlayer;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinimaxState extends BaseState<MinimaxState, MinimaxAction> {

    private final Board board;
    private final Piece toMove;
    private final int playerPieces;

    MinimaxState(Board board, Piece toMove, int playerPieces) {
        this.board = board;
        this.toMove = toMove;
        this.playerPieces = playerPieces;
    }

    @Override
    public Collection<MinimaxAction> actions() {
        if (playerPieces > 0) {
            return tryPlacePiece();
        }
        return tryMovePiece();
    }

    @Override
    public double eval() {
        return 0;
    }

    private Set<MinimaxAction> tryPlacePiece() {
        return board.getUnoccupiedPoints().stream()
                .flatMap(this::generateActionsFromPlacePiece)
                .collect(Collectors.toSet());
    }

    private Set<MinimaxAction> tryMovePiece() {
        Set<Point> initialPoints = board.getOccupiedPoints(toMove);

        return initialPoints.stream()
                .flatMap(initialPoints.size() == 3 ? this::tryMovePieceAnywhere : this::tryMovePieceToNeighbor)
                .collect(Collectors.toSet());
    }

    private Stream<MinimaxAction> tryMovePieceToNeighbor(Point initialPoint) {
        return initialPoint.getNeighbors().stream()
                .filter(Point::isUnoccupied)
                .flatMap(dest -> generateActionsFromMovePiece(initialPoint, dest));
    }

    private Stream<MinimaxAction> tryMovePieceAnywhere(Point initialPoint) {
        return board.getUnoccupiedPoints().stream()
                .flatMap(this.generateActionsFromMovePiece(initialPoint));
    }

    private Stream<MinimaxAction> generateActionsFromPlacePiece(Point point) {
        boolean captures = board.getMillsAt(point).stream()
                .anyMatch(willCompleteMill(point));
        Coordinate coordinate = point.getCoordinate();

        if (captures) {
            return board.getOccupiedPoints(toMove.opposite()).stream()
                    .map(Point::getCoordinate)
                    .map(capture -> MinimaxAction.fromPlacePiece(coordinate).withCapture(capture));
        }
        return Stream.of(MinimaxAction.fromPlacePiece(coordinate));
    }

    private Stream<MinimaxAction> generateActionsFromMovePiece(Point initial, Point destination) {
        boolean captures = board.getMillsAt(destination).stream()
                .filter(mill -> !mill.getPoints().contains(initial))
                .anyMatch(willCompleteMill(destination));
        Coordinate movePieceFrom = initial.getCoordinate(), movePieceTo = destination.getCoordinate();

        if (captures) {
            return board.getOccupiedPoints(toMove.opposite()).stream()
                    .map(Point::getCoordinate)
                    .map(capture -> MinimaxAction.fromMovePiece(movePieceFrom, movePieceTo).withCapture(capture));
        }
        return Stream.of(MinimaxAction.fromMovePiece(movePieceFrom, movePieceTo));
    }

    public Piece getToMove() {
        return toMove;
    }

    public int getPlayerPieces() {
        return playerPieces;
    }

    private Function<Point, Stream<MinimaxAction>> generateActionsFromMovePiece(Point initialPoint) {
        return destination -> generateActionsFromMovePiece(initialPoint, destination);
    }

    private Predicate<Mill> willCompleteMill(Point point) {
        return mill -> mill.getPoints().stream()
                .filter(Predicate.isEqual(point).negate())
                .map(Point::getPiece)
                .allMatch(Predicates.equalTo(toMove));
    }

    Board copyBoard() {
        return Board.copy(board);
    }

    public static MinimaxState create(Board board, MinimaxPlayer player) {
        return new MinimaxState(board, player.getPiece(), player.getStartingPieces());
    }
}
