package info.jayharris.ninemensmorris;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StalemateCheckerTest {

    @Nested
    class Accept {

        StalemateChecker stalemateChecker;

        Board board = BoardBuilder.create()
                .withPiece("b6", Piece.WHITE)
                .withPiece("d6", Piece.WHITE)
                .withPiece("f6", Piece.WHITE)
                .withPiece("b4", Piece.BLACK)
                .withPiece("f4", Piece.WHITE)
                .withPiece("d3", Piece.BLACK)
                .withPiece("d2", Piece.BLACK)
                .withPiece("a1", Piece.BLACK)
                .withPiece("d1", Piece.BLACK)
                .withPiece("g1", Piece.WHITE)
                .build();

        @BeforeEach
        public void setUp() {
            stalemateChecker = StalemateChecker.create();
        }

        @Test
        @DisplayName("it accepts a board state for the first time")
        public void noStalemate() throws Exception {
            stalemateChecker.accept(board);
            assertThat(stalemateChecker.isStalemate()).isFalse();
        }

        @Test
        @DisplayName("it accepts a board state for the second time")
        public void stalemate() throws Exception {
            stalemateChecker.accept(board);
            stalemateChecker.accept(board);
            assertThat(stalemateChecker.isStalemate()).isTrue();
        }
    }
}