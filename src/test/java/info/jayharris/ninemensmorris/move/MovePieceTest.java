package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class MovePieceTest {

    BasePlayer player;
    Piece piece;

    Board board;

    @BeforeEach
    void setUp() throws Exception {
        player = new PlayerAdapter(Piece.WHITE);
        piece = player.getPiece();
    }

    @Test
    @DisplayName("it moves the player's piece from one point to another")
    void perform() {
        String initStr = "a7", destStr = "d7";
        board = BoardBuilder.create()
                .withPiece("a7", piece)
                .withPiece("b6", piece)
                .withPiece("c5", piece)
                .withPiece("a4", piece)
                .build();

        Point init = board.getPoint(initStr), dest = board.getPoint(destStr);

        MovePiece.create(piece, init, dest, false).perform();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(init).hasFieldOrPropertyWithValue("piece", null);
        softly.assertThat(dest).hasFieldOrPropertyWithValue("piece", piece);
        softly.assertAll();
    }

    @Nested
    class ValidateLegal {

        @BeforeEach
        void setUp() throws Exception {

        }

        @Test
        @DisplayName("legal move, cannot fly")
        void legal() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", piece)
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), false);

            assertThatCode(move::validateLegal).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("illegal move — trying to move opponent's piece")
        void opponentPiece() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", piece.opposite())
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), false);

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — initial point is unoccupied")
        void unoccupiedInitial() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), false);

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — destination point is occupied")
        void occupiedDestination() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", piece)
                    .withPiece("d7", piece)
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), false);

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — initial and destination points not adjacent, cannot fly")
        void notAdjacent() {
            String initStr = "a7", destStr = "g7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", piece)
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), false);

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("legal move — initial and destination points not adjacent, can fly")
        void flying() {
            String initStr = "a7", destStr = "g7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", piece)
                    .build();

            MovePiece move = MovePiece.create(piece, board.getPoint(initStr), board.getPoint(destStr), true);

            assertThatCode(move::validateLegal).doesNotThrowAnyException();
        }
    }
}