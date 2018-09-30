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
                    public PlacePiece placePiece(Board board) {
                        return PlacePiece.create(this, initial);
                    }
                };
                player.setStartingPieces(7);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn).extracting("initial", "capture")
                        .containsExactly(PlacePiece.create(player, initial), null);
                softly.assertThat(player).hasFieldOrPropertyWithValue("startingPieces", 6);
                softly.assertThat(initial).hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertAll();
            }

            @Test
            @DisplayName("place piece — with capture")
            void placePieceWithCapture() throws Exception {
                Point initial = board.getPoint("d5");
                Point capture = board.getPoint("b6");

                PlayerAdapter player = new PlayerAdapter(Piece.BLACK) {
                    @Override
                    public PlacePiece placePiece(Board board) {
                        return PlacePiece.create(this, initial);
                    }

                    @Override
                    public CapturePiece capturePiece(Board board) {
                        return CapturePiece.create(this, capture);
                    }
                };
                player.setStartingPieces(7);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn).extracting("initial", "capture")
                        .containsExactly(
                                PlacePiece.create(player, initial),
                                CapturePiece.create(player, capture));
                softly.assertThat(player).hasFieldOrPropertyWithValue("startingPieces", 6);
                softly.assertThat(initial).hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertThat(capture).hasFieldOrPropertyWithValue("piece", null);
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
                    public MovePiece movePiece(Board board) {
                        return MovePiece.create(this, board, initial, destination);
                    }
                };
                player.setStartingPieces(0);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn).extracting("initial", "capture")
                        .containsExactly(MovePiece.create(player, board, initial, destination), null);
                softly.assertThat(initial).hasFieldOrPropertyWithValue("piece", null);
                softly.assertThat(destination).hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertAll();
            }

            @Test
            @DisplayName("move piece — with capture")
            void movePieceWithCapture() throws Exception {
                Point initial = board.getPoint("d5"), destination = board.getPoint("e5"), capture = board.getPoint("b2");

                PlayerAdapter player = new PlayerAdapter(Piece.WHITE) {
                    @Override
                    public MovePiece movePiece(Board board) {
                        return MovePiece.create(this, board, initial, destination);
                    }

                    @Override
                    public CapturePiece capturePiece(Board board) {
                        return CapturePiece.create(this, capture);
                    }
                };
                player.setStartingPieces(0);

                Turn turn = player.takeTurn(board);

                softly.assertThat(turn).extracting("initial", "capture")
                        .containsExactly(
                                MovePiece.create(player, board, initial, destination),
                                CapturePiece.create(player, capture));
                softly.assertThat(initial).hasFieldOrPropertyWithValue("piece", null);
                softly.assertThat(destination).hasFieldOrPropertyWithValue("piece", player.getPiece());
                softly.assertThat(capture).hasFieldOrPropertyWithValue("piece", null);
                softly.assertAll();
            }
        }
    }
}