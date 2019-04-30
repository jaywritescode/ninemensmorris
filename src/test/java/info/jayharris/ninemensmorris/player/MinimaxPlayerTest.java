package info.jayharris.ninemensmorris.player;

import com.google.common.collect.ImmutableMap;
import info.jayharris.minimax.DecisionTree;
import info.jayharris.minimax.DecisionTreeFactory;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxAction;
import info.jayharris.ninemensmorris.minimax.MinimaxActionBuilder;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.jayharris.ninemensmorris.Piece.BLACK;
import static info.jayharris.ninemensmorris.Piece.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinimaxPlayerTest {

    private MinimaxPlayer player;

    private Board board;

    @Mock
    private DecisionTreeFactory<MinimaxState, MinimaxAction> decisionTreeFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("place piece phase")
    class PlacePiecePhase {

        private Map<Coordinate, Piece> pieces = Stream.of(
                Pair.of("e5", WHITE),
                Pair.of("e4", WHITE),
                Pair.of("g4", BLACK),
                Pair.of("b2", BLACK),
                Pair.of("f2", BLACK),
                Pair.of("a1", WHITE))
                .collect(Collectors.toMap(pair -> Coordinate.get(pair.getLeft()), Pair::getRight));

        @BeforeEach
        void setUp() {
            BoardBuilder builder = BoardBuilder.create();
            pieces.forEach(builder::withPiece);
            board = builder.build();
        }

        @Test
        @DisplayName("it takes a turn in the place piece phase without a capture")
        public void placePieceNoCapture() throws Exception {
            player = MinimaxPlayerBuilder.create()
                    .withPiece(BLACK)
                    .withDecisionTreeFactory(decisionTreeFactory)
                    .withStartingPieces(6)
                    .build();

            Coordinate placePiece = Coordinate.get("c3");

            MinimaxAction action = MinimaxActionBuilder.create().withPlacePiece(placePiece).build();
            DecisionTree<MinimaxState, MinimaxAction> decisionTree = mock(DecisionTree.class);
            when(decisionTree.perform()).thenReturn(action);
            when(decisionTreeFactory.build(eq(MinimaxState.create(board, player)))).thenReturn(decisionTree);

            player.takeTurn(board);

            Map<Coordinate, Piece> expectedBoard = new HashMap<>();
            expectedBoard.putAll(pieces);
            expectedBoard.put(placePiece, player.getPiece());

            assertThat(board).satisfies(boardMatches(expectedBoard));
        }

        @Test
        @DisplayName("it takes a turn in the place piece phase with a capture")
        public void placePieceWithCapture() throws Exception {
            player = MinimaxPlayerBuilder.create()
                    .withPiece(BLACK)
                    .withDecisionTreeFactory(decisionTreeFactory)
                    .withStartingPieces(6)
                    .build();

            Coordinate placePiece = Coordinate.get("d2");
            Coordinate capturePiece = Coordinate.get("e5");

            MinimaxAction action = MinimaxActionBuilder.create()
                    .withPlacePiece(placePiece)
                    .withCapturePiece(capturePiece)
                    .build();
            DecisionTree<MinimaxState, MinimaxAction> decisionTree = mock(DecisionTree.class);
            when(decisionTree.perform()).thenReturn(action);
            when(decisionTreeFactory.build(eq(MinimaxState.create(board, player)))).thenReturn(decisionTree);

            player.takeTurn(board);

            Map<Coordinate, Piece> expectedBoard = new HashMap<>();
            expectedBoard.putAll(pieces);
            expectedBoard.put(placePiece, player.getPiece());
            expectedBoard.remove(capturePiece);

            assertThat(board).satisfies(boardMatches(expectedBoard));
        }
    }

    @Nested
    @DisplayName("move piece phase")
    class MovePiecePhase {
         
    }

    @Test
    @DisplayName("it takes a turn in the move piece phase without a capture")
    public void movePieceNoCapture() throws Exception {

    }

    @Test
    @DisplayName("it takes a turn in the move piece phase with a capture")
    public void movePieceWithCapture() throws Exception {

    }

    static class MinimaxPlayerBuilder {

        Piece piece;
        int startingPieces = 0;
        DecisionTreeFactory<MinimaxState, MinimaxAction> decisionTreeFactory;

        private MinimaxPlayerBuilder() { }

        public MinimaxPlayerBuilder withPiece(Piece piece) {
            this.piece = piece;
            return this;
        }

        public MinimaxPlayerBuilder withStartingPieces(int startingPieces) {
            this.startingPieces = startingPieces;
            return this;
        }

        public MinimaxPlayerBuilder withDecisionTreeFactory(DecisionTreeFactory<MinimaxState, MinimaxAction> decisionTreeFactory) {
            this.decisionTreeFactory = decisionTreeFactory;
            return this;
        }

        public MinimaxPlayer build() throws Exception {

            PlayerAdapter player = new PlayerAdapter(piece);
            player.setStartingPieces(startingPieces);

            return new MinimaxPlayer(piece, decisionTreeFactory) {
                @Override
                public int getStartingPieces() {
                    return player.startingPieces;
                }
            };
        }

        static MinimaxPlayerBuilder create() {
            return new MinimaxPlayerBuilder();
        }

    }

    private Consumer<Board> boardMatches(Map<Coordinate, Piece> expectedBoard) {
        return board -> {
            Coordinate.COORDINATES.forEach(coordinate -> {
                Piece actual = board.getPoint(coordinate).getPiece();
                Piece expected = expectedBoard.get(coordinate);
                assertThat(actual).as("Cooordinate " + coordinate).isEqualTo(expected);
            });
        };
    };
}