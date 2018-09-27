package info.jayharris.ninemensmorris.player;

import com.google.common.collect.ImmutableList;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Game;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalPlayer extends BasePlayer {

    private final BufferedReader reader;
    private final PrintStream out;

    private final static Pattern movePattern = Pattern.compile("^(\\w{2})\\s*-\\s*(\\w{2})$", Pattern.CASE_INSENSITIVE);

    TerminalPlayer(Piece piece) {
        this(piece, new BufferedReader(new InputStreamReader(System.in)), System.out);
    }

    public TerminalPlayer(Piece piece, BufferedReader reader, PrintStream out) {
        super(piece);
        this.reader = reader;
        this.out = out;
    }

    @Override
    public PlacePiece placePiece(Board board) {
        String input;

        while (true) {
            try {
                input = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (valid(input)) {
                try {
                    return PlacePiece.createLegal(this, board.getPoint(input));
                }
                catch (IllegalMoveException e) {
                    out.printf("%s Try again >> ", e.getMessage());
                }
            }
            else {
                out.printf("%s is invalid algebraic notation. Try again >> ", input);
            }
        }
    }

    @Override
    public FlyPiece movePiece(Board board) {
        String input;

        while (true) {
            try {
                input = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            Matcher matcher = movePattern.matcher(input);
            String init = matcher.group(1), dest = matcher.group(2);

            if (!(matcher.matches() && valid(init) && valid(dest))) {
                out.printf("%s is invalid algebraic notation.", input);
            }

            try {
                // TODO: maybe FlyPiece and MovePiece shouldn't be different classes
                if (board.getOccupiedPoints(getPiece()).size() == 3) {
                    return FlyPiece.createLegal(this, board.getPoint(init), board.getPoint(dest));
                }
                else {
                    return MovePiece.createLegal(this, board.getPoint(init), board.getPoint(dest));
                }
            }
            catch (IllegalMoveException e) {
                out.printf("%s Try again >> ", e.getMessage());
            }
        }
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        String input;

        while (true) {
            try {
                input = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (valid(input)) {
                try {
                    return CapturePiece.createLegalMove(this, board.getPoint(input));
                }
                catch (IllegalMoveException e) {
                    out.printf("%s Try again >> ", e.getMessage());
                }
            }
            else {
                out.printf("%s is invalid algebraic notation. Try again >> ", input);
            }
        }
    }

    private static boolean valid(String point) {
        return Board.ALGEBRAIC_NOTATIONS_FOR_POINTS.contains(point);
    }

    @Override
    public void begin(Game game) {
        out.println(game.pretty());
        out.printf("Ply %d: %s to %s >> ", game.getPly(), getPiece().toString(), getStartingPieces() > 0 ? "place piece" : "move piece");
    }
}
