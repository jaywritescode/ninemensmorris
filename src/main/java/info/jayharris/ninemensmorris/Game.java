package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.RandomMovePlayer;

import java.util.Set;

public class Game {

    private final BasePlayer black, white;
    private BasePlayer current;

    private Board board;

    private int ply = 0;

    Game(BasePlayer black, BasePlayer white) {
        this.black = this.current = black;
        this.white = white;

        this.board = new Board();
    }

    public void play() {
        while (!isGameOver()) {
            try {
                current.begin(this);
                nextPly();
                current.done(this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void nextPly() {
        ++ply;

        current.takeTurn(board);

        current = (current == black ? white : black);
    }

    private boolean isGameOver() {
        if (current.getStartingPieces() > 0) {
            return false;
        }

        Set<Point> occupied = board.getOccupiedPoints(current.getPiece());
        int count = occupied.size();

        return count < 3 || (count > 3 && occupied.stream().noneMatch(
                point -> point.getNeighbors().stream().anyMatch(Point::isUnoccupied)));
    }

    public String pretty() {
        return board.pretty();
    }

    public static void main(String... args) {
        BasePlayer black = new RandomMovePlayer(Piece.BLACK);
        BasePlayer white = new RandomMovePlayer(Piece.WHITE);

        Game game = new Game(black, white);

        game.play();
    }
}
