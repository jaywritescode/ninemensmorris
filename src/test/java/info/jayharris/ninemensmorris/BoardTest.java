package info.jayharris.ninemensmorris;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void copy() {
        Board original = BoardBuilder.create()
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
        Board copy = Board.copy(original);;

        assertThat(copy)
                .as("Original board:\n%s\nis the same as copy:\n%s", original.pretty(), copy.pretty())
                .isEqualTo(original);
    }

    @Nested
    class Equals {
        @Test
        @DisplayName("two boards are #equals")
        void testEquals() {
            Board b1 = BoardBuilder.create()
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
            Board b2 = BoardBuilder.create()
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

            assertThat(b1.equals(b2)).isTrue();
        }

        @Test
        @DisplayName("two boards are not #equals")
        void testNotEquals() {
            Board b1 = BoardBuilder.create()
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
            Board b2 = BoardBuilder.create()
                    .withPiece("a7", Piece.WHITE)
                    .withPiece("d6", Piece.BLACK)
                    .withPiece("c5", Piece.BLACK)
                    .withPiece("e5", Piece.WHITE)
                    .withPiece("b4", Piece.BLACK)
                    .withPiece("c4", Piece.WHITE)
                    .withPiece("f4", Piece.BLACK)
                    .withPiece("g4", Piece.WHITE)
                    .withPiece("c3", Piece.WHITE)
                    .withPiece("b2", Piece.WHITE)
                    .withPiece("d2", Piece.BLACK)
                    .build();

            assertThat(b1.equals(b2)).isFalse();
        }
    }
}