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
    Board board;

    @BeforeEach
    void setUp() {
        player = new PlayerAdapter(Piece.WHITE);
    }

    @Test
    @DisplayName("it moves the player's piece from one point to another")
    void perform() {
        String initStr = "a7", destStr = "d7";
        board = BoardBuilder.create()
                .withPiece("a7", player.getPiece())
                .withPiece("b6", player.getPiece())
                .withPiece("c5", player.getPiece())
                .withPiece("a4", player.getPiece())
                .build();

        Point init = board.getPoint(initStr), dest = board.getPoint(destStr);

        MovePiece.create(player, board, init, dest).perform();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(init).hasFieldOrPropertyWithValue("piece", null);
        softly.assertThat(dest).hasFieldOrPropertyWithValue("piece", player.getPiece());
        softly.assertAll();
    }

    @Nested
    class ValidateLegal {

        @BeforeEach
        void setUp() {

        }

        @Test
        @DisplayName("legal move with > 3 pieces on the board")
        void legal() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", player.getPiece())
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .withPiece("a4", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatCode(move::validateLegal).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("illegal move — trying to move opponent's piece")
        void opponentPiece() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", player.getPiece().opposite())
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .withPiece("a4", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — initial point is unoccupied")
        void unoccupiedInitial() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .withPiece("a4", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — destination point is occupied")
        void occupiedDestination() {
            String initStr = "a7", destStr = "d7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", player.getPiece())
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .withPiece("a4", player.getPiece())
                    .withPiece("d7", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — initial and destination points not adjacent, > 3 pieces on board")
        void notAdjacent() {
            String initStr = "a7", destStr = "g7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", player.getPiece().opposite())
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .withPiece("a4", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("legal move with 3 pieces on the board")
        void flying() {
            String initStr = "a7", destStr = "g7";

            Board board = BoardBuilder.create()
                    .withPiece("a7", player.getPiece())
                    .withPiece("b6", player.getPiece())
                    .withPiece("c5", player.getPiece())
                    .build();

            MovePiece move = MovePiece.create(player, board, board.getPoint(initStr), board.getPoint(destStr));

            assertThatCode(move::validateLegal).doesNotThrowAnyException();
        }
    }
}