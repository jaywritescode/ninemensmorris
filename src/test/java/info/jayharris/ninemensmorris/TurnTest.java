package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;
import info.jayharris.ninemensmorris.move.PlacePiece;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class TurnTest {

    BasePlayer player;
    Piece piece;

    @BeforeEach
    void setUp() throws Exception {
        player = new PlayerAdapter(Piece.WHITE);
        piece = player.getPiece();
    }

    @Test
    void doInitialMove() {
        Board board = BoardBuilder.create().build();
        Point point = board.getPoint("c3");

        Turn turn = Turn.create(player, board);

        InitialMove initial = PlacePiece.create(piece, point);

        assertThatCode(() -> turn.doInitialMove(initial)).doesNotThrowAnyException();
        assertThat(point).hasFieldOrPropertyWithValue("piece", piece);
    }

    @Test
    void doCaptureMove() {
        String captureStr = "f6";
        Board board = BoardBuilder.create()
                .withPiece("d3", player.getPiece())
                .withPiece("e3", player.getPiece())
                .withPiece(captureStr, player.getPiece().opposite())
                .build();
        Point point = board.getPoint(captureStr);

        Turn turn = Turn.create(player, board);
        turn.doInitialMove(PlacePiece.create(piece, board.getPoint("c3")));

        CapturePiece capture = CapturePiece.create(piece, point);

        assertThatCode(() -> turn.doCaptureMove(capture)).doesNotThrowAnyException();
        assertThat(point).hasFieldOrPropertyWithValue("piece", null);
    }
}