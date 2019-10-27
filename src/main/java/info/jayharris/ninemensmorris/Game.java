package info.jayharris.ninemensmorris;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import info.jayharris.minimax.search.Search;
import info.jayharris.ninemensmorris.minimax.*;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.MinimaxPlayer;
import info.jayharris.ninemensmorris.player.TerminalPlayer;

import java.util.Arrays;
import java.util.List;

public class Game {

    private final BasePlayer black, white;
    private BasePlayer current;

    private Board board;

    private List<Turn> history = Lists.newLinkedList();
    private int ply = 1;

    Game(BasePlayer black, BasePlayer white) {
        this.black = this.current = black;
        this.white = white;

        this.board = new Board();
    }

    /**
     * Play the game.
     *
     * @return the winner
     */
    public BasePlayer play() {
        while (true) {
            current.begin(this);
            nextPly();
            current.done(this);

            if (isGameOver()) {
                return current;
            }

            current = (current == black ? white : black);
        }
    }

    public int getPly() {
        return ply;
    }

    private void nextPly() {
        Turn turn = current.takeTurn(board);
        history.add(turn);
        ++ply;
    }

    private boolean isGameOver() {
        if (current.getStartingPieces() > 0) {
            return false;
        }

        // After current takes their turn. there is no possibility that their opponent has won the game. And ties are
        // impossible, so we only need to check if current has won the game to see if the game is over.
        return BoardUtils.isWinner(board, current.getPiece());
    }

    public String pretty() {
        return board.pretty();
    }

    public static void main(String... args) {
        BasePlayer black = new TerminalPlayer(Piece.BLACK);

        Search<MinimaxState, MinimaxAction> search = new NineMensMorrisMinimaxDecision(
                new SampleCutoffTest(), new SampleHeuristicFunction(Piece.WHITE), Piece.WHITE);
        BasePlayer white = new MinimaxPlayer(Piece.WHITE, Suppliers.ofInstance(search));

        Game game = new Game(black, white);

        BasePlayer winner = game.play();
        System.out.println(game.pretty());

        Arrays.asList(black, white).forEach(player -> player.gameOver(winner));
    }
}
