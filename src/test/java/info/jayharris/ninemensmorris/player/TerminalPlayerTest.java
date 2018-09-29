package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TerminalPlayerTest {

    private TerminalPlayer player;
    private BoardBuilder builder;

    @Mock
    private BufferedReader reader;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        player = new TerminalPlayer(Piece.BLACK, reader, new PrintStream(output));
        builder = BoardBuilder.create();
    }

    @Nested
    @DisplayName("TerminalPlayer#placePiece")
    class _PlacePiece {

        String legal = "e3";

        @Test
        @DisplayName("legal move")
        void legal() throws Exception {
            doReturn(legal).when(reader).readLine();

            Board board = builder.build();

            assertThat(player.placePiece(board)).hasFieldOrPropertyWithValue("point", board.getPoint(legal));
            verify(reader).readLine();
        }

        @Test
        @DisplayName("invalid algebraic notation")
        void invalidAlgebraicNotation() throws Exception {
            String input = "b7";
            doReturn(input, legal).when(reader).readLine();

            player.placePiece(builder.build());

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).isEqualTo(
                    String.format(TerminalPlayer.INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input));
        }

        @Test
        @DisplayName("illegal move")
        void illegalMove() throws Exception {
            String input = "b2";
            doReturn(input, legal).when(reader).readLine();

            player.placePiece(builder.withPiece(input, Piece.BLACK).build());

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).contains(String.format(PlacePiece.ILLEGAL_MOVE_TEMPLATE, input));
            assertThat(output.toString()).contains(String.format(TerminalPlayer.TRY_AGAIN_TEMPLATE, ""));
        }
    }

    @Nested
    @DisplayName("TerminalPlayer#movePiece")
    class _MovePiece {

        String initStr = "b2", destStr = "b4";
        String legal = initStr + " - " + destStr;

        @Test
        @DisplayName("legal move")
        void legal() throws Exception {
            doReturn(legal).when(reader).readLine();

            Board board = builder.withPiece(initStr, player.getPiece()).build();

            assertThat(player.movePiece(board)).extracting("initial", "destination")
                    .containsExactly(board.getPoint(initStr), board.getPoint(destStr));
        }

        @Test
        @DisplayName("invalid algebraic notation — regex mismatch")
        void regexMismatch() throws Exception {
            String input = "bad-input";
            doReturn(input, legal).when(reader).readLine();

            Board board = builder.withPiece(initStr, player.getPiece()).build();

            player.movePiece(board);

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).contains(String.format(TerminalPlayer.INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input));
        }

        @Test
        @DisplayName("invalid algebraic notation — invalid point")
        void invalidPoint() throws Exception {
            String invalid = "d4";
            String[] badInputs = new String[] { invalid + "-" + destStr, initStr + "-" + invalid };
            doReturn(badInputs[0], badInputs[1], legal).when(reader).readLine();

            Board board = builder.withPiece(initStr, player.getPiece()).build();

            player.movePiece(board);

            verify(reader, times(3)).readLine();
            assertThat(output.toString()).contains(String.format(TerminalPlayer.INVALID_ALGEBRAIC_NOTATION_TEMPLATE, badInputs[0]));
            assertThat(output.toString()).contains(String.format(TerminalPlayer.INVALID_ALGEBRAIC_NOTATION_TEMPLATE, badInputs[1]));
        }

        @Test
        @DisplayName("illegal move")
        void illegal() throws Exception {
            String invalid = "a1";
            String input = invalid + "-a4";
            doReturn(input, legal).when(reader).readLine();

            Board board = builder.withPiece(initStr, player.getPiece()).build();

            player.movePiece(board);

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).contains(String.format(MovePiece.EMPTY_POINT_TEMPLATE, invalid));
            assertThat(output.toString()).contains(String.format(TerminalPlayer.TRY_AGAIN_TEMPLATE, ""));
        }
    }

    @Nested
    @DisplayName("TerminalPlayer#capturePiece")
    class _CapturePiece {

        String legal = "e3";

        @BeforeEach
        void setUp() {
            builder.withPiece(legal, player.getPiece().opposite());
        }

        @Test
        @DisplayName("legal move")
        void legal() throws Exception {
            doReturn(legal).when(reader).readLine();

            Board board = builder.build();

            assertThat(player.capturePiece(board)).hasFieldOrPropertyWithValue("point", board.getPoint(legal));
        }

        @Test
        @DisplayName("invalid algebraic notation")
        void invalidAlgebraicNotation() throws Exception {
            String input = "b7";
            doReturn(input, legal).when(reader).readLine();

            player.capturePiece(builder.build());

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).isEqualTo(
                    String.format(TerminalPlayer.INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input));
        }

        @Test
        @DisplayName("illegal move — point is unoccupied")
        void unoccupied() throws Exception {
            String input = "a4";
            doReturn(input, legal).when(reader).readLine();

            player.capturePiece(builder.build());

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).contains(
                    String.format(CapturePiece.ILLEGAL_MOVE_TEMPLATE, player.getPiece().opposite().toString(), input,
                                  CapturePiece.EMPTY_POINT));
            assertThat(output.toString()).contains(String.format(TerminalPlayer.TRY_AGAIN_TEMPLATE, ""));
        }

        @Test
        @DisplayName("illegal move — player's own piece is on point")
        void ownPiece() throws Exception {
            String input = "a4";
            doReturn(input, legal).when(reader).readLine();

            player.capturePiece(builder.withPiece(input, player.getPiece()).build());

            verify(reader, times(2)).readLine();
            assertThat(output.toString()).contains(
                    String.format(CapturePiece.ILLEGAL_MOVE_TEMPLATE, player.getPiece().opposite().toString(), input,
                                  player.getPiece()));
            assertThat(output.toString()).contains(String.format(TerminalPlayer.TRY_AGAIN_TEMPLATE, ""));
        }
    }
}