package info.jayharris.ninemensmorris.minimax;

import com.google.common.base.Predicates;
import info.jayharris.minimax.BaseState;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Mill;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import info.jayharris.ninemensmorris.player.BasePlayer;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Can this class be consolidated into MinimaxPlayer?
public class MinimaxState extends BaseState<MinimaxState, MinimaxAction> {

    private final Board board;
    private final Piece toMove;

    /**
     * the number of pieces the player has left to put on the board in the
     * initial phase of the game
     */
    private final int playerPieces;

    private MinimaxState(Board board, Piece toMove, int playerPieces) {
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

    void doPlacePiece(Coordinate move) {
        if (move == null) {
            return;
        }

        PlacePiece.createLegal(toMove, board.getPoint(move)).perform();
    }

    void doMovePiece(Coordinate from, Coordinate to) {
        if (from == null && to == null) {
            return;
        }

        MovePiece.createLegal(toMove, board.getPoint(from), board.getPoint(to),
                board.getOccupiedPoints(toMove).size() == 3).perform();
    }

    void doCapturePiece(Coordinate capture) {
        if (capture == null) {
            return;
        }

        CapturePiece.createLegal(toMove, board.getPoint(capture)).perform();
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

    public Board getBoard() {
        return board;
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

    /**
     * Creates a new MinimaxState from a predecessor state. Changes to the board
     * in the returned state won't affect the predecessor state.
     *
     * @param board  the state's board
     * @param player the state's player to move
     * @return a new state
     */
    public static MinimaxState create(Board board, BasePlayer player) {
        return create(board, player.getPiece(), player.getStartingPieces());
    }

    /**
     * Creates a new MinimaxState from a predecessor state. Changes to the board
     * in the returned state won't affect the predecessor state.
     *
     * @param predecessor the predecessor state
     * @return a new state
     */
    public static MinimaxState create(MinimaxState predecessor) {
        Piece nextPiece = predecessor.getToMove().opposite();

        return MinimaxState.create(predecessor.getBoard(), nextPiece,
                predecessor.getPlayerPieces() - (nextPiece == BasePlayer.FIRST_PLAYER ? 1 : 0));
    }

    static MinimaxState create(Board board, Piece toMove, int startingPieces) {
        return new MinimaxState(Board.copy(board), toMove, Math.max(0, startingPieces));
    }
}
