package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.Node;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DepthAwareCutoffTestTest {

    private final int MAX_DEPTH = 3;

    @Nested
    class TerminateSearch {

        @Test
        @DisplayName("Node depth is >= MAX_DEPTH and state is quiescent")
        void test() throws Exception {
            Board quiescentState = BoardBuilder.create()
                    .withPiece("d7", Piece.WHITE)
                    .withPiece("g7", Piece.BLACK)
                    .withPiece("d6", Piece.WHITE)
                    .withPiece("c5", Piece.BLACK)
                    .withPiece("d5", Piece.BLACK)
                    .withPiece("e5", Piece.BLACK)
                    .withPiece("a4", Piece.WHITE)
                    .withPiece("b4", Piece.BLACK)
                    .withPiece("c4", Piece.WHITE)
                    .withPiece("e4", Piece.BLACK)
                    .withPiece("f4", Piece.WHITE)
                    .withPiece("g4", Piece.WHITE)
                    .withPiece("d3", Piece.WHITE)
                    .withPiece("b2", Piece.BLACK)
                    .withPiece("d2", Piece.BLACK)
                    .withPiece("a1", Piece.BLACK)
                    .withPiece("d1", Piece.WHITE)
                    .build();

            Node node = NodeBuilder.create()
                    .withState(MinimaxStateBuilder.create()
                            .withBoard(quiescentState)
                            .withToMove(Piece.BLACK)
                            .build())
                    .withDepth(MAX_DEPTH)
                    .build();

            DepthAwareCutoffTest cutoffTest = new DepthAwareCutoffTest(MAX_DEPTH);

            assertThat(cutoffTest.test(node)).isTrue();
        }
    }

    @Nested
    class ContinueSearch {

        @Test
        @DisplayName("Node depth is < MAX_DEPTH")
        void testMaxDepthNotReached() throws Exception {
            Board board = BoardBuilder.create()
                    .withPiece("d7", Piece.WHITE)
                    .withPiece("g7", Piece.BLACK)
                    .withPiece("d6", Piece.WHITE)
                    .withPiece("d5", Piece.BLACK)
                    .withPiece("e5", Piece.BLACK)
                    .withPiece("b4", Piece.WHITE)
                    .withPiece("c4", Piece.BLACK)
                    .withPiece("e4", Piece.WHITE)
                    .withPiece("d3", Piece.BLACK)
                    .build();

            Node node = NodeBuilder.create()
                    .withState(MinimaxStateBuilder.create().withBoard(board).build())
                    .withDepth(1)
                    .build();

            DepthAwareCutoffTest cutoffTest = new DepthAwareCutoffTest(MAX_DEPTH);

            assertThat(cutoffTest.test(node)).isFalse();
        }

        @Test
        @DisplayName("State is not quiescent")
        void testStateNotQuiescent() throws Exception {
            // black will complete a mill by playing b4-b2
            Board nonQuiescentState = BoardBuilder.create()
                    .withPiece("a7", Piece.WHITE)
                    .withPiece("d7", Piece.WHITE)
                    .withPiece("g7", Piece.BLACK)
                    .withPiece("d6", Piece.WHITE)
                    .withPiece("c5", Piece.BLACK)
                    .withPiece("d5", Piece.BLACK)
                    .withPiece("e5", Piece.BLACK)
                    .withPiece("b4", Piece.BLACK)
                    .withPiece("c4", Piece.WHITE)
                    .withPiece("e4", Piece.WHITE)
                    .withPiece("f4", Piece.WHITE)
                    .withPiece("d3", Piece.WHITE)
                    .withPiece("e3", Piece.BLACK)
                    .withPiece("d2", Piece.BLACK)
                    .withPiece("f2", Piece.BLACK)
                    .withPiece("a1", Piece.BLACK)
                    .withPiece("d1", Piece.WHITE)
                    .build();

            Node node = NodeBuilder.create()
                    .withState(MinimaxStateBuilder.create()
                            .withBoard(nonQuiescentState)
                            .withToMove(Piece.BLACK)
                            .build())
                    .withDepth(MAX_DEPTH)
                    .build();

            DepthAwareCutoffTest cutoffTest = new DepthAwareCutoffTest(MAX_DEPTH);

            assertThat(cutoffTest.test(node)).isFalse();
        }
    }
}