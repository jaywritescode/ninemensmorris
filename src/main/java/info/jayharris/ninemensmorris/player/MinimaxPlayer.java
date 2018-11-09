package info.jayharris.ninemensmorris.player;

import info.jayharris.minimax.DecisionTree;
import info.jayharris.minimax.HeuristicEvaluationFunction;
import info.jayharris.minimax.Node;
import info.jayharris.minimax.transposition.InMemoryMapTranspositions;
import info.jayharris.minimax.transposition.Transpositions;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxAction;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

public class MinimaxPlayer extends BasePlayer {

    private final HeuristicEvaluationFunction<MinimaxState> heuristic;

    Transpositions<MinimaxState> transpositions = new InMemoryMapTranspositions<>();

    MinimaxPlayer(Piece piece, HeuristicEvaluationFunction<MinimaxState> heuristic) {
        super(piece);
        this.heuristic = heuristic;
    }

    @Override
    public PlacePiece placePiece(Board board) {
        DecisionTree<MinimaxState, MinimaxAction> tree = new DecisionTree<>(
                Node.root(MinimaxState.create(board, this)),
                transpositions,
                heuristic,
                node -> node.getDepth() >= 3);

        MinimaxAction a = tree.perform();

        return PlacePiece.create(getPiece(), board.getPoint(a.getPlacePiece()));
    }

    @Override
    public MovePiece movePiece(Board board) {
        return null;
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        return null;
    }
}
