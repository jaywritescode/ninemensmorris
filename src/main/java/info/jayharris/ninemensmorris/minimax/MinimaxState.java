package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.State;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Mill;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.StalemateChecker;
import info.jayharris.ninemensmorris.player.BasePlayer;

import java.util.Collection;
import java.util.Collections;
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
public class MinimaxState implements State<MinimaxState, MinimaxAction> {

    private final Board board;
    private final Piece toMove;
    private final StalemateChecker stalemateChecker;

    /**
     * the number of pieces the player has left to put on the board in the
     * initial phase of the game
     */
    private final int playerPieces;

    private MinimaxState(Board board, Piece toMove, int playerPieces, StalemateChecker stalemateChecker) {
        this.board = board;
        this.toMove = toMove;
        this.playerPieces = playerPieces;
        this.stalemateChecker = stalemateChecker;
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

    @Override
    public boolean terminalTest() {
        return isStalemate() || actions().isEmpty();
    }

    public boolean isStalemate() {
        return stalemateChecker.hasStalemateState();
    }

    private Set<MinimaxAction> tryPlacePiece() {
        return board.getUnoccupiedPoints().stream()
                .flatMap(this::generateActionsFromPlacePiece)
                .collect(Collectors.toSet());
    }

    private Set<MinimaxAction> tryMovePiece() {
        Set<Point> initialPoints = board.getOccupiedPoints(toMove);

        if (initialPoints.size() < 3) {
            return Collections.emptySet();
        }

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
                .allMatch(Predicate.isEqual(toMove));
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
     * @param stalemateChecker contains the history of states visited
     * @return a new state
     */
    public static MinimaxState create(Board board, BasePlayer player, StalemateChecker stalemateChecker) {
        return new MinimaxState(board, player.getPiece(), player.getStartingPieces(), stalemateChecker);
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

        // make copies of the mutable parts of the predecessor state
        Board copy = Board.copy(predecessor.getBoard());
        StalemateChecker stalemateChecker = StalemateChecker.copy(predecessor.stalemateChecker);

        action.makeChain(currentPlayer).forEach(fn -> fn.apply(copy).perform());
        if (action.isMovePiece()) {
            stalemateChecker.accept(copy);
        }

        int startingPieces = Math.max(0, predecessor.getPlayerPieces() - (currentPlayer == BasePlayer.FIRST_PLAYER ? 0 : 1));

        return new MinimaxState(copy, currentPlayer.opposite(), startingPieces, stalemateChecker);
    }
}
