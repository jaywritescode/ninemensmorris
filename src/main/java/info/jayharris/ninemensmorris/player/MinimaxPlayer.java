package info.jayharris.ninemensmorris.player;

import info.jayharris.minimax.DecisionTree;
import info.jayharris.minimax.HeuristicEvaluationFunction;
import info.jayharris.minimax.Node;
import info.jayharris.minimax.transposition.InMemoryMapTranspositions;
import info.jayharris.minimax.transposition.Transpositions;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.*;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

public class MinimaxPlayer extends BasePlayer {

    private HeuristicEvaluationFunction<MinimaxState> heuristic;

    Transpositions<MinimaxState> transpositions = new InMemoryMapTranspositions<>();

    /**
     * The Player interface and the minimax API work somewhat at odds with one another.
     *
     * The player makes two separate requests for the part of the turn that places or moves
     * a piece and the part of the turn that captures a piece, if necessary. But the minimax
     * algorithm combines both of those into a single action (due to the requirement that the
     * child of a min-node is a max-node and vice versa).
     *
     * So we have to figure out what the best next state is, and then remember the actions
     * that lead to that state in a global.
     */
    private MinimaxAction thisTurnAction;

    public MinimaxPlayer(Piece piece, HeuristicEvaluationFunction<MinimaxState> heuristic) {
        super(piece);
        this.heuristic = heuristic;
    }

    public void setHeuristic(HeuristicEvaluationFunction<MinimaxState> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public PlacePiece placePiece(Board board) {
        DecisionTree<MinimaxState, MinimaxAction> tree = new DecisionTree<>(
                MinimaxState.create(board, this),
                transpositions,
                heuristic,
                node -> node.getDepth() >= 3);

        thisTurnAction = tree.perform();

        return PlacePiece.create(piece, board.getPoint(thisTurnAction.getPlacePiece()));
    }

    @Override
    public MovePiece movePiece(Board board) {
        DecisionTree<MinimaxState, MinimaxAction> tree = new DecisionTree<>(
                MinimaxState.create(board, this),
                transpositions,
                heuristic,
                node -> node.getDepth() >= 3);

        thisTurnAction = tree.perform();

        return MovePiece.create(piece,
                board.getPoint(thisTurnAction.getMovePieceFrom()),
                board.getPoint(thisTurnAction.getMovePieceTo()),
                board.getOccupiedPoints(getPiece()).size() == 3);
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        return CapturePiece.create(piece, board.getPoint(thisTurnAction.getCapturePiece()));
    }
}
