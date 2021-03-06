package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlacePieceTest {

    BasePlayer player;
    Piece piece;

    Board board;

    @BeforeEach
    void setUp() throws Exception {
        player = new PlayerAdapter(Piece.BLACK);
        piece = player.getPiece();

        board = BoardBuilder.empty();
    }

    @Test
    @DisplayName("it places a piece of the player's color at the given point")
    void perform() {
        Point point = board.getPoint("e3");

        PlacePiece.create(piece, point).perform();
        assertThat(point).hasFieldOrPropertyWithValue("piece", piece);
    }


    @Nested
    class ValidateLegal {

        @Test
        @DisplayName("legal move")
        void legal() {
            PlacePiece move = PlacePiece.create(piece, board.getPoint("e3"));

            assertThatCode(move::validateLegal).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("illegal move — point is occupied")
        void occupied() {
            String point = "e3";
            board = BoardBuilder.create()
                    .withPiece(point, Piece.BLACK)
                    .build();

            PlacePiece move = PlacePiece.create(piece, board.getPoint(point));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }
    }
}