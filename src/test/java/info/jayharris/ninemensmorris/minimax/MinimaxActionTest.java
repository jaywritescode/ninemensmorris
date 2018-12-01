package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MinimaxActionTest {

    @Test
    @DisplayName("place piece — black, no capture")
    void placePieceBlackNoCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("b4", Piece.BLACK)
                .withPiece("d6", Piece.WHITE)
                .withPiece("c3", Piece.BLACK)
                .withPiece("a7", Piece.WHITE)
                .build();
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.BLACK)
                .withPlayerPieces(7)
                .build();

        Coordinate coordinate = Coordinate.get("g1");
        MinimaxAction action = MinimaxAction.fromPlacePiece(coordinate);

        MinimaxState nextState = action.apply(currentState);

        assertThat(nextState)
                .hasFieldOrPropertyWithValue("toMove", Piece.WHITE)
                .hasFieldOrPropertyWithValue("playerPieces", 7)
                .hasFieldOrPropertyWithValue("board", BoardBuilder.create(board)
                        .withPiece(board.getPoint(coordinate), Piece.BLACK)
                        .build());
    }

    @Test
    @DisplayName("place piece — white, no capture")
    void placePieceWhiteNoCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("b4", Piece.BLACK)
                .withPiece("d6", Piece.WHITE)
                .withPiece("c3", Piece.BLACK)
                .withPiece("a7", Piece.WHITE)
                .withPiece("g1", Piece.BLACK)
                .build();
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.WHITE)
                .withPlayerPieces(7)
                .build();

        Coordinate coordinate = Coordinate.get("f6");
        MinimaxAction action = MinimaxAction.fromPlacePiece(coordinate);

        MinimaxState nextState = action.apply(currentState);

        assertThat(nextState)
                .hasFieldOrPropertyWithValue("toMove", Piece.BLACK)
                .hasFieldOrPropertyWithValue("playerPieces", 6)
                .hasFieldOrPropertyWithValue("board", BoardBuilder.create(board)
                        .withPiece(board.getPoint(coordinate), Piece.BLACK)
                        .build());
    }

    @Test
    @DisplayName("place piece — with capture")
    void placePieceWithCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("a7", Piece.WHITE)
                .withPiece("d6", Piece.BLACK)
                .withPiece("d5", Piece.WHITE)
                .withPiece("b4", Piece.WHITE)
                .withPiece("c4", Piece.BLACK)
                .withPiece("f4", Piece.WHITE)
                .withPiece("e3", Piece.BLACK)
                .withPiece("d2", Piece.BLACK)
                .build();
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.BLACK)
                .withPlayerPieces(5)
                .build();

        Coordinate place = Coordinate.get("c3");
        Coordinate capture = Coordinate.get("b4");
        MinimaxAction action = MinimaxAction.fromPlacePiece(place).withCapture(capture);

        MinimaxState nextState = action.apply(currentState);

        assertThat(nextState)
                .hasFieldOrPropertyWithValue("toMove", Piece.WHITE)
                .hasFieldOrPropertyWithValue("playerPieces", 5)
                .hasFieldOrPropertyWithValue("board", BoardBuilder.create(board)
                        .withPiece(board.getPoint(place), Piece.BLACK)
                        .withPiece(board.getPoint(capture), null)
                        .build());
    }

}