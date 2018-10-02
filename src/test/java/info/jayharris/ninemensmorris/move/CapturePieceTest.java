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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class CapturePieceTest {

    private BasePlayer player;
    private Piece piece;

    private Board board;
    private Point point;

    @BeforeEach
    void setUp() throws Exception {
        player = new PlayerAdapter(Piece.WHITE);
        piece = player.getPiece();

        String pointStr = "e3";
        board = BoardBuilder.create()
                .withPiece(pointStr, player.getPiece().opposite())
                .build();
        point = board.getPoint(pointStr);
    }

    @Test
    @DisplayName("it removes an opponent's piece at the given point")
    void perform() {
        CapturePiece.create(player.getPiece(), point).perform();
        assertThat(point).hasFieldOrPropertyWithValue("piece", null);
    }

    @Nested
    class ValidateLegal {

        @Test
        @DisplayName("legal move")
        void legal() {
            assertThatCode(CapturePiece.create(piece, point)::perform).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("illegal move — point is unoccupied")
        void unoccupied() {
            String point = "e3";
            board = BoardBuilder.empty();

            CapturePiece move = CapturePiece.create(piece, board.getPoint(point));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }

        @Test
        @DisplayName("illegal move — player's own piece is on point")
        void ownPiece() {
            String point = "e3";
            board = BoardBuilder.create()
                    .withPiece(point, piece)
                    .build();

            CapturePiece move = CapturePiece.create(piece, board.getPoint(point));

            assertThatExceptionOfType(IllegalMoveException.class).isThrownBy(move::validateLegal);
        }
    }
}