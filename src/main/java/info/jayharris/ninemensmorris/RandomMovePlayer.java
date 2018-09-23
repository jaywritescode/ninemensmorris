package info.jayharris.ninemensmorris;

import com.google.common.collect.Lists;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.FlyPiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RandomMovePlayer extends BasePlayer {

    protected RandomMovePlayer(Piece piece) {
        super(piece);
    }

    @Override
    public PlacePiece placePiece(Board board) {
        return PlacePiece.create(this, randomElement(board.getUnoccupiedPoints()));
    }

    @Override
    public FlyPiece movePiece(Board board) {
        if (board.getOccupiedPoints(getPiece()).size() < 3) {
            return flyPiece(board);
        }
        return moveToNeighbor(board);
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        return CapturePiece.create(this, randomElement(board.getOccupiedPoints(getPiece().opposite())));
    }

    public FlyPiece flyPiece(Board board) {
        Point init = randomElement(board.getOccupiedPoints(getPiece()));
        Point dest = randomElement(board.getUnoccupiedPoints());
        return FlyPiece.create(this, init, dest);
    }

    public MovePiece moveToNeighbor(Board board) {
        Point init = randomElement(
                board.getOccupiedPoints(getPiece()).stream()
                        .filter(point -> !point.getNeighbors().isEmpty())
                        .collect(Collectors.toSet()));
        Point dest = randomElement(init.getNeighbors());
        return MovePiece.create(this, init, dest);
    }

    private <T> T randomElement(Collection<T> collection) {
        ArrayList<T> arrayList = Lists.newArrayList(collection);
        return arrayList.get(RandomUtils.nextInt() % arrayList.size());
    }
}
