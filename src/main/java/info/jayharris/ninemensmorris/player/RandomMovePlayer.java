package info.jayharris.ninemensmorris.player;

import com.google.common.collect.Lists;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RandomMovePlayer extends TwoStageTurnPlayer {

    public RandomMovePlayer(Piece piece) {
        super(piece);
    }

    @Override
    protected PlacePiece placePiece(Board board) {
        return PlacePiece.create(piece, randomElement(board.getUnoccupiedPoints()));
    }

    @Override
    protected MovePiece movePiece(Board board) {
        if (board.getOccupiedPoints(piece).size() == 3) {
            return movePieceAnywhere(board);
        }
        return moveToNeighbor(board);
    }

    @Override
    protected CapturePiece capturePiece(Board board) {
        return CapturePiece.create(piece, randomElement(board.getOccupiedPoints(piece.opposite())));
    }

    private MovePiece movePieceAnywhere(Board board) {
        Point init = randomElement(board.getOccupiedPoints(piece));
        Point dest = randomElement(board.getUnoccupiedPoints());
        return MovePiece.create(piece, init, dest, true);
    }

    private MovePiece moveToNeighbor(Board board) {
        Point init = randomElement(
                board.getOccupiedPoints(piece).stream()
                        .filter(point -> point.getNeighbors().stream().anyMatch(Point::isUnoccupied))
                        .collect(Collectors.toList())
        );
        Point dest = randomElement(
                init.getNeighbors().stream()
                        .filter(Point::isUnoccupied)
                        .collect(Collectors.toList()));
        return MovePiece.create(piece, init, dest, false);
    }

    private <T> T randomElement(Collection<T> collection) {
        ArrayList<T> arrayList = Lists.newArrayList(collection);
        return arrayList.get(RandomUtils.nextInt() % arrayList.size());
    }
}
