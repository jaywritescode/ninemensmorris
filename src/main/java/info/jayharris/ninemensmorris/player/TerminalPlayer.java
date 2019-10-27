package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Game;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.IllegalMoveException;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalPlayer extends TwoStageTurnPlayer {

    private final BufferedReader reader;
    private final PrintStream out;

    private final static Pattern movePattern = Pattern.compile("^(\\w{2})\\s*-\\s*(\\w{2})$", Pattern.CASE_INSENSITIVE);

    public final static String INVALID_ALGEBRAIC_NOTATION_TEMPLATE = "%s is invalid algebraic notation. Try again >> ",
            TRY_AGAIN_TEMPLATE = "%s Try again >> ";

    public TerminalPlayer(Piece piece) {
        this(piece, new BufferedReader(new InputStreamReader(System.in)), System.out);
    }

    public TerminalPlayer(Piece piece, BufferedReader reader, PrintStream out) {
        super(piece);
        this.reader = reader;
        this.out = out;
    }

    @Override
    protected PlacePiece placePiece(Board board) {
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
                    return PlacePiece.createLegal(piece, board.getPoint(input));
                }
                catch (IllegalMoveException e) {
                    out.printf(TRY_AGAIN_TEMPLATE, e.getMessage());
                }
            }
            else {
                out.printf(INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input);
            }
        }
    }

    @Override
    protected MovePiece movePiece(Board board) {
        String input;

        while (true) {
            try {
                input = reader.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            Matcher matcher = movePattern.matcher(input);
            if (!matcher.matches()) {
                out.printf(INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input);
                continue;
            }

            String init = matcher.group(1), dest = matcher.group(2);
            if (!(matcher.matches() && valid(init) && valid(dest))) {
                out.printf(INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input);
                continue;
            }

            try {
                return MovePiece.createLegal(piece, board.getPoint(init), board.getPoint(dest),
                                             board.getOccupiedPoints(piece).size() == 3);
            }
            catch (IllegalMoveException e) {
                out.printf(TRY_AGAIN_TEMPLATE, e.getMessage());
            }
        }
    }

    @Override
    protected CapturePiece capturePiece(Board board) {
        out.printf("Capture %s piece >> ", piece.opposite());
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
                    return CapturePiece.createLegal(piece, board.getPoint(input));
                }
                catch (IllegalMoveException e) {
                    out.printf(TRY_AGAIN_TEMPLATE, e.getMessage());
                }
            }
            else {
                out.printf(INVALID_ALGEBRAIC_NOTATION_TEMPLATE, input);
            }
        }
    }

    private static boolean valid(String point) {
        return Coordinate.ALGEBRAIC_NOTATIONS_FOR_COORDINATES.contains(point);
    }

    @Override
    public void begin(Game game) {
        if (game.getPly() > 1) {
            out.println();
            out.println("Opponent played: " + game.lastPly().pretty());
            out.println("=======================================");
        }

        out.println(game.pretty());
        out.printf("Ply %d: %s to %s >> ", game.getPly(), piece.toString(), startingPieces > 0 ? "place piece" : "move piece");
    }

    @Override
    public void gameOver(BasePlayer winner) {
        out.println("Game Over ==============================");
        out.println(winner == this ? "You win!" : "You lose.");
    }
}
