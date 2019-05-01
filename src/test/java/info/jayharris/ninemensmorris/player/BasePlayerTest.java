package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.Turn;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BasePlayerTest {

    SoftAssertions softly;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();
    }

    @Nested
    class TakeTurn {

        @Nested
        class _PlacePiece {

            Board board;

            @BeforeEach
            void setUp() {
                board = BoardBuilder.create()
                        .withPiece("e5", Piece.BLACK)
                        .withPiece("b4", Piece.WHITE)
                        .withPiece("c5", Piece.BLACK)
                        .withPiece("b6", Piece.WHITE)
                        .build();
            }

            @Test
            @DisplayName("place piece — no capture")
            void placePieceNoCapture() throws Exception {
                Point initial = board.getPoint("d6");

                PlayerAdapter player = new PlayerAdapter(Piece.BLACK) {
                    @Override
                    protected PlacePiece placePiece(Board board) {
                        return PlacePiece.create(getPiece(), initial);
                    }
                };
                player.setStartingPieces(7);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn)
                        .as("Turn places piece at [%s]", initial.algebraicNotation())
                        .extracting("initial.point", "capture.point")
                        .containsExactly(initial, null);
                softly.assertThat(initial)
                        .as("[%s] now has a [%s] piece on it", initial.algebraicNotation(), player.getPiece())
                        .hasFieldOrPropertyWithValue("piece", player.getPiece());

                softly.assertThat(player)
                        .as("Piece removed from player's initial collection.")
                        .hasFieldOrPropertyWithValue("startingPieces", 6);
                softly.assertAll();
            }

            @Test
            @DisplayName("place piece — with capture")
            void placePieceWithCapture() throws Exception {
                Point initial = board.getPoint("d5");
                Point capture = board.getPoint("b6");

                PlayerAdapter player = new PlayerAdapter(Piece.BLACK) {
                    @Override
                    protected PlacePiece placePiece(Board board) {
                        return PlacePiece.create(getPiece(), initial);
                    }

                    @Override
                    protected CapturePiece capturePiece(Board board) {
                        return CapturePiece.create(getPiece(), capture);
                    }
                };
                player.setStartingPieces(7);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn)
                        .as("Turn places piece at [%s] and then captures at [%s]",
                            initial.algebraicNotation(), capture.algebraicNotation())
                        .extracting("initial.point", "capture.point")
                        .containsExactly(initial, capture);

                softly.assertThat(initial)
                        .as("[%s] now has a [%s] piece on it", initial.algebraicNotation(), player.getPiece())
                        .hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertThat(capture)
                        .as("[%s] is now empty", capture.algebraicNotation())
                        .hasFieldOrPropertyWithValue("piece", null);

                softly.assertThat(player)
                        .as("Piece removed from player's initial collection.")
                        .hasFieldOrPropertyWithValue("startingPieces", 6);
                softly.assertAll();
            }
        }

        @Nested
        class _MovePiece {

            Board board;

            @BeforeEach
            void setUp() {
                board = BoardBuilder.create()
                        .withPiece("b6", Piece.BLACK)
                        .withPiece("d6", Piece.BLACK)
                        .withPiece("f6", Piece.WHITE)
                        .withPiece("c5", Piece.BLACK)
                        .withPiece("d5", Piece.WHITE)
                        .withPiece("a4", Piece.WHITE)
                        .withPiece("b4", Piece.BLACK)
                        .withPiece("c4", Piece.BLACK)
                        .withPiece("e4", Piece.WHITE)
                        .withPiece("f4", Piece.BLACK)
                        .withPiece("c3", Piece.WHITE)
                        .withPiece("d3", Piece.WHITE)
                        .withPiece("e3", Piece.WHITE)
                        .withPiece("b2", Piece.BLACK)
                        .withPiece("d2", Piece.BLACK)
                        .withPiece("f2", Piece.WHITE)
                        .build();
            }

            @Test
            @DisplayName("move piece — no capture")
            void movePieceNoCapture() throws Exception {
                Point initial = board.getPoint("d2"), destination = board.getPoint("d1");

                PlayerAdapter player = new PlayerAdapter(Piece.BLACK) {
                    @Override
                    protected MovePiece movePiece(Board board) {
                        return MovePiece.create(getPiece(), initial, destination, false);
                    }
                };
                player.setStartingPieces(0);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn)
                        .as("Turn moves piece from [%s] to [%s].",
                            initial.algebraicNotation(), destination.algebraicNotation())
                        .extracting("initial.initial", "initial.destination", "capture")
                        .containsExactly(initial, destination, null);

                softly.assertThat(initial)
                        .as("[%s] is now empty", initial.algebraicNotation())
                        .hasFieldOrPropertyWithValue("piece", null);
                softly.assertThat(destination)
                        .as("[%s] now has a [%s] piece on it.", destination.algebraicNotation(), player.getPiece())
                        .hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertAll();
            }

            @Test
            @DisplayName("move piece — with capture")
            void movePieceWithCapture() throws Exception {
                Point initial = board.getPoint("d5"), destination = board.getPoint("e5"), capture = board.getPoint("b2");

                PlayerAdapter player = new PlayerAdapter(Piece.WHITE) {
                    @Override
                    protected MovePiece movePiece(Board board) {
                        return MovePiece.create(getPiece(), initial, destination, false);
                    }

                    @Override
                    protected CapturePiece capturePiece(Board board) {
                        return CapturePiece.create(getPiece(), capture);
                    }
                };
                player.setStartingPieces(0);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn)
                        .as("Turn moves piece from [%s] to [%s] and then captures at [%s]",
                            initial.algebraicNotation(), destination.algebraicNotation(), capture.algebraicNotation())
                        .extracting("initial.initial", "initial.destination", "capture.point")
                        .containsExactly(initial, destination, capture);

                softly.assertThat(initial)
                        .as("[%s] is now empty", initial.algebraicNotation())
                        .hasFieldOrPropertyWithValue("piece", null);
                softly.assertThat(destination)
                        .as("[%s] now has a [%s] piece on it", destination.algebraicNotation(), player.getPiece())
                        .hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertThat(capture)
                        .as("[%s] is now empty", capture.algebraicNotation())
                        .hasFieldOrPropertyWithValue("piece", null);
                softly.assertAll();
            }
        }
    }
}