package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.CutoffTest;
import info.jayharris.minimax.Node;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DepthAwareCutoffTest extends CutoffTest<MinimaxState, MinimaxAction> {

    private final int maxDepth;

    public DepthAwareCutoffTest(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean test(Node<MinimaxState, MinimaxAction> node) {
        if (node.getDepth() < maxDepth) {
            return false;
        }

        return isQuiescentState(node);
    }

    private boolean isQuiescentState(Node<MinimaxState, MinimaxAction> node) {
        MinimaxState state = node.getState();

        // If the current player can make a move that creates a mill, then we'll say
        // this state is not quiescent.
        Piece piece = state.getToMove();
        Predicate<Board.Mill> canCompleteMill =
                state.getBoard().getOccupiedPoints(piece).size() == 3 ?
                        playerCanCompleteMillWithFlying(piece) :
                        playerCanCompleteMillWithoutFlying(piece);
        return state.getBoard().getMills().stream()
                .noneMatch(canCompleteMill);
    }

    private Predicate<Board.Mill> playerCanCompleteMillWithoutFlying(Piece piece) {
        return mill -> {
            Set<Board.Point> points = mill.getPoints();

            Map<Piece, List<Board.Point>> grouped = points.stream()
                    .filter(point -> !point.isUnoccupied())
                    .collect(Collectors.groupingBy(Board.Point::getPiece));

            // Two points in mill need to have `piece` on them.
            if (!grouped.containsKey(piece) || grouped.get(piece).size() != 2) {
                return false;
            }

            Optional<Board.Point> unoccupied = points.stream().filter(Board.Point::isUnoccupied).findAny();
            if (!unoccupied.isPresent()) {
                return false;
            }

            // The unoccupied point needs a neighbor that is (a) not in the mill
            // and (b) covered by a `piece` piece to complete a mill on the next ply.
            return unoccupied.get().getNeighbors().stream()
                    .filter(neighbor -> !points.contains(neighbor))
                    .anyMatch(point -> point.getPiece() == piece);
        };
    }

    private Predicate<Board.Mill> playerCanCompleteMillWithFlying(Piece piece) {
        return mill -> {
            Set<Board.Point> points = mill.getPoints();

            Map<Piece, List<Board.Point>> grouped = points.stream()
                    .filter(point -> !point.isUnoccupied())
                    .collect(Collectors.groupingBy(Board.Point::getPiece));

            // Two points in mill need to have `piece` on them.
            if (!grouped.containsKey(piece) || grouped.get(piece).size() != 2) {
                return false;
            }

            // If the other point is unoccupied, then the player's third piece
            // can fly to it and complete the mill.
            return points.stream().anyMatch(Board.Point::isUnoccupied);
        };
    }
}
