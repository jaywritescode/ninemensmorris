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

public class RandomMovePlayer extends BasePlayer {

    public RandomMovePlayer(Piece piece) {
        super(piece);
    }

    @Override
    public PlacePiece placePiece(Board board) {
        return PlacePiece.create(getPiece(), randomElement(board.getUnoccupiedPoints()));
    }

    @Override
    public MovePiece movePiece(Board board) {
        if (board.getOccupiedPoints(getPiece()).size() == 3) {
            return movePieceAnywhere(board);
        }
        return moveToNeighbor(board);
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        return CapturePiece.create(getPiece(), randomElement(board.getOccupiedPoints(getPiece().opposite())));
    }

    private MovePiece movePieceAnywhere(Board board) {
        Point init = randomElement(board.getOccupiedPoints(getPiece()));
        Point dest = randomElement(board.getUnoccupiedPoints());
        return MovePiece.create(getPiece(), init, dest, true);
    }

    private MovePiece moveToNeighbor(Board board) {
        Point init = randomElement(
                board.getOccupiedPoints(getPiece()).stream()
                        .filter(point -> point.getNeighbors().stream().anyMatch(Point::isUnoccupied))
                        .collect(Collectors.toList())
        );
        Point dest = randomElement(
                init.getNeighbors().stream()
                        .filter(Point::isUnoccupied)
                        .collect(Collectors.toList()));
        return MovePiece.create(getPiece(), init, dest, false);
    }

    private <T> T randomElement(Collection<T> collection) {
        ArrayList<T> arrayList = Lists.newArrayList(collection);
        return arrayList.get(RandomUtils.nextInt() % arrayList.size());
    }
}
