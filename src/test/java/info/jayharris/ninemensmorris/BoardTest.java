package info.jayharris.ninemensmorris;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Comparator<Board> boardComparator = new BoardComparator();

    @Test
    void copy() {
        Board board = BoardBuilder.create()
                .withPiece("a7", Piece.WHITE)
                .withPiece("d6", Piece.BLACK)
                .withPiece("c5", Piece.BLACK)
                .withPiece("e5", Piece.BLACK)
                .withPiece("b4", Piece.BLACK)
                .withPiece("c4", Piece.WHITE)
                .withPiece("e4", Piece.WHITE)
                .withPiece("f4", Piece.BLACK)
                .withPiece("g4", Piece.WHITE)
                .withPiece("c3", Piece.WHITE)
                .withPiece("b2", Piece.WHITE)
                .withPiece("d2", Piece.BLACK)
                .build();

        assertThat(Board.copy(board)).usingComparator(boardComparator).isEqualTo(board);
    }

    class BoardComparator implements Comparator<Board> {

        @Override
        public int compare(Board o1, Board o2) {
            return Board.ALGEBRAIC_NOTATIONS_FOR_POINTS.stream()
                    .allMatch(point -> o1.getPoint(point).getPiece() == o2.getPoint(point).getPiece()) ? 0 : 1;
        }
    }
}