package info.jayharris.ninemensmorris.player;

import com.google.common.base.Suppliers;
import info.jayharris.minimax.search.MinimaxDecision;
import info.jayharris.minimax.search.Search;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.jayharris.ninemensmorris.Piece.BLACK;
import static info.jayharris.ninemensmorris.Piece.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MinimaxPlayerTest {

    private MinimaxPlayer player;

    private BoardBuilder boardBuilder;

    private Board board;

    @Mock
    private MinimaxDecision<MinimaxState, MinimaxAction> search;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        boardBuilder = BoardBuilder.create();
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
            pieces.forEach(boardBuilder::withPiece);
            board = boardBuilder.build();
        }

        @Test
        @DisplayName("it takes a turn in the place piece phase without a capture")
        public void placePieceNoCapture() throws Exception {
            player = MinimaxPlayerBuilder.create()
                    .withPiece(BLACK)
                    .withSearch(search)
                    .withStartingPieces(6)
                    .build();

            Coordinate placePiece = Coordinate.get("c3");

            MinimaxAction action = MinimaxActionBuilder.create().withPlacePiece(placePiece).build();
            when(search.perform(any())).thenReturn(action);

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
                    .withSearch(search)
                    .withStartingPieces(6)
                    .build();

            Coordinate placePiece = Coordinate.get("d2");
            Coordinate capturePiece = Coordinate.get("e5");

            MinimaxAction action = MinimaxActionBuilder.create()
                    .withPlacePiece(placePiece)
                    .withCapturePiece(capturePiece)
                    .build();
            when(search.perform(any())).thenReturn(action);

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

        @BeforeEach
        void setUp() {
            pieces.forEach(boardBuilder::withPiece);
            board = boardBuilder.build();
        }

        private Map<Coordinate, Piece> pieces = Stream.of(
                Pair.of("d7", WHITE),
                Pair.of("g7", BLACK),
                Pair.of("d6", WHITE),
                Pair.of("f6", BLACK),
                Pair.of("c5", WHITE),
                Pair.of("e5", BLACK),
                Pair.of("a4", WHITE),
                Pair.of("b4", BLACK),
                Pair.of("e4", WHITE),
                Pair.of("f4", WHITE),
                Pair.of("d3", BLACK),
                Pair.of("e3", WHITE),
                Pair.of("b2", WHITE),
                Pair.of("d2", BLACK),
                Pair.of("a1", BLACK),
                Pair.of("d1", BLACK))
                .collect(Collectors.toMap(pair -> Coordinate.get(pair.getLeft()), Pair::getRight));

        @Test
        @DisplayName("it takes a turn in the move piece phase without a capture")
        public void movePieceNoCapture() throws Exception {
            player = MinimaxPlayerBuilder.create()
                    .withPiece(WHITE)
                    .withSearch(search)
                    .withStartingPieces(0)
                    .build();

            Coordinate movePieceFrom = Coordinate.get("a4");
            Coordinate movePieceTo = Coordinate.get("a7");

            MinimaxAction action = MinimaxActionBuilder.create()
                    .withMovePieceFrom(movePieceFrom)
                    .withMovePieceTo(movePieceTo)
                    .build();
            when(search.perform(any())).thenReturn(action);

            player.takeTurn(board);

            Map<Coordinate, Piece> expectedBoard = new HashMap<>();
            expectedBoard.putAll(pieces);
            expectedBoard.remove(movePieceFrom);
            expectedBoard.put(movePieceTo, player.getPiece());

            assertThat(board).satisfies(boardMatches(expectedBoard));
        }

        @Test
        @DisplayName("it takes a turn in the move piece phase with a capture")
        public void movePieceWithCapture() throws Exception {
            player = MinimaxPlayerBuilder.create()
                    .withPiece(WHITE)
                    .withSearch(search)
                    .withStartingPieces(0)
                    .build();

            Coordinate movePieceFrom = Coordinate.get("c5");
            Coordinate movePieceTo = Coordinate.get("d5");
            Coordinate capturePiece = Coordinate.get("b4");

            MinimaxAction action = MinimaxActionBuilder.create()
                    .withMovePieceFrom(movePieceFrom)
                    .withMovePieceTo(movePieceTo)
                    .withCapturePiece(capturePiece)
                    .build();
            when(search.perform(any())).thenReturn(action);

            player.takeTurn(board);

            Map<Coordinate, Piece> expectedBoard = new HashMap<>();
            expectedBoard.putAll(pieces);
            expectedBoard.remove(movePieceFrom);
            expectedBoard.put(movePieceTo, player.getPiece());
            expectedBoard.remove(capturePiece);

            assertThat(board).satisfies(boardMatches(expectedBoard));
        }
    }

    static class MinimaxPlayerBuilder {

        Piece piece;
        int startingPieces = 0;
        Supplier<? extends Search<MinimaxState, MinimaxAction>> search;

        private MinimaxPlayerBuilder() { }

        public MinimaxPlayerBuilder withPiece(Piece piece) {
            this.piece = piece;
            return this;
        }

        public MinimaxPlayerBuilder withStartingPieces(int startingPieces) {
            this.startingPieces = startingPieces;
            return this;
        }

        public MinimaxPlayerBuilder withSearch(Search<MinimaxState, MinimaxAction> search) {
            this.search = Suppliers.ofInstance(search);
            return this;
        }

        public MinimaxPlayer build() {
            MinimaxPlayer player = new MinimaxPlayer(piece, search);
            player.startingPieces = startingPieces;
            return player;
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