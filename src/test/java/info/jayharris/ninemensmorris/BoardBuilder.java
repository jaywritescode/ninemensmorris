package info.jayharris.ninemensmorris;

import com.google.common.base.Preconditions;
import info.jayharris.ninemensmorris.Board.Point;
import org.apache.commons.lang3.RandomUtils;

public class BoardBuilder {

    Board board;

    private BoardBuilder(Board board) {
        this.board = board;
    }

    BoardBuilder withPiece(String point, Piece piece) {
        return withPiece(board.getPoint(point), piece);
    }

    BoardBuilder withPiece(Point point, Piece piece) {
        Preconditions.checkArgument(board.hasPoint(point));

        point.setPiece(piece);
        return this;
    }

    Point getPoint(String point) {
        return board.getPoint(point);
    }

    Point findArbitraryPoint(Piece piece) {
        return board.getOccupiedPoints(piece).iterator().next();
    }

    Board build() {
        return board;
    }

    public static BoardBuilder create() {
        return BoardBuilder.create(new Board());
    }

    public static BoardBuilder create(Board board) {
        return new BoardBuilder(board);
    }
}
