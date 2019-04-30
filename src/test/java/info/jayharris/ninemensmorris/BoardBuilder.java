package info.jayharris.ninemensmorris;

import com.google.common.base.Preconditions;
import info.jayharris.ninemensmorris.Board.Point;

public class BoardBuilder {

    private Board board;

    private BoardBuilder(Board board) {
        this.board = board;
    }

    public BoardBuilder withPiece(String point, Piece piece) {
        return withPiece(board.getPoint(point), piece);
    }

    public BoardBuilder withPiece(Coordinate point, Piece piece) {
        return withPiece(board.getPoint(point), piece);
    }

    public BoardBuilder withPiece(Point point, Piece piece) {
        point.setPiece(piece);
        return this;
    }

    public Point findArbitraryPoint(Piece piece) {
        return board.getOccupiedPoints(piece).iterator().next();
    }

    public Board build() {
        return board;
    }

    public static Board empty() {
        return BoardBuilder.create().build();
    }

    public static BoardBuilder create() {
        return BoardBuilder.create(new Board());
    }

    public static BoardBuilder create(Board board) {
        return new BoardBuilder(board);
    }
}
