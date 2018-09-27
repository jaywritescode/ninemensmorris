package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

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
            nextPly();
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

    public static void main(String... args) {
        BasePlayer black = new RandomMovePlayer(Piece.BLACK);
        BasePlayer white = new RandomMovePlayer(Piece.WHITE);

        Game game = new Game(black, white);

        game.play();
    }
}
