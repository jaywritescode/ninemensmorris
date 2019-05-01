package info.jayharris.ninemensmorris.minimax;

import com.google.common.base.Predicates;
import info.jayharris.minimax.BaseState;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Mill;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.BasePlayer;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Can this class be consolidated into MinimaxPlayer?
/**
 * A state of the game represented in the decision tree.
 *
 * Instances of this class are intended to be immutable.
 */
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

    /**
     * Get all of the legal actions for this state.
     *
     * @return a collection of actions
     */
    @Override
    public Collection<MinimaxAction> actions() {
        if (playerPieces > 0) {
            return tryPlacePiece();
        }
        return tryMovePiece();
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

    private Function<Point, Stream<MinimaxAction>> generateActionsFromMovePiece(Point initialPoint) {
        return destination -> generateActionsFromMovePiece(initialPoint, destination);
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

    private Predicate<Mill> willCompleteMill(Point point) {
        return mill -> mill.getPoints().stream()
                .filter(Predicate.isEqual(point).negate())
                .map(Point::getPiece)
                .allMatch(Predicates.equalTo(toMove));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimaxState that = (MinimaxState) o;
        return getPlayerPieces() == that.getPlayerPieces() &&
                getBoard().equals(that.getBoard()) &&
                getToMove() == that.getToMove();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), getToMove(), getPlayerPieces());
    }

    /**
     * Creates a MinimaxState from the board and the player to move.
     *
     * @param board  the state's board
     * @param player the state's player to move
     * @return a new state
     */
    public static MinimaxState create(Board board, BasePlayer player) {
        return new MinimaxState(board, player.getPiece(), player.getStartingPieces());
    }

    /**
     * Creates a successor state from a predecessor state and an action.
     *
     * @param predecessor the predecessor state
     * @param action the action
     * @return a successor state
     */
    protected static MinimaxState create(MinimaxState predecessor, MinimaxAction action) {
        Piece currentPlayer = predecessor.getToMove();

        Board copy = Board.copy(predecessor.getBoard());
        action.makeChain(currentPlayer).forEach(fn -> fn.apply(copy).perform());

        int startingPieces = Math.max(0, predecessor.getPlayerPieces() - (currentPlayer == BasePlayer.FIRST_PLAYER ? 0 : 1));

        return new MinimaxState(copy, currentPlayer.opposite(), startingPieces);
    }
}
