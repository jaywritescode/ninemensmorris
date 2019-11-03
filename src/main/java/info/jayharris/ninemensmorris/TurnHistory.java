package info.jayharris.ninemensmorris;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TurnHistory {

    private final Deque<Turn> turnList;

    TurnHistory() {
        turnList = new LinkedList<>();
    }

    /**
     * Adds this turn to the history.
     *
     * @param turn the turn that just occurred
     */
    public void consume(Turn turn) {
        turnList.add(turn);
    }

    /**
     * Gets the most recent turn.
     *
     * @return the last turn, or null if the game hasn't started yet
     */
    public @Nullable Turn lastTurn() {
        return turnList.peekLast();
    }

    /**
     * Formats the history for pretty printing.
     *
     * @return a pretty-printable string
     */
    public String pretty() {
        return pretty(true);
    }

    /**
     * Formats the history for pretty printing.
     *
     * @param verbose true for a columnar layout, false for a horizontal layout
     * @return a pretty-printable string
     */
    public String pretty(boolean verbose) {
        return verbose ? prettyVerbose() : prettyCompact();
    }

    private String prettyVerbose() {
        AtomicInteger i = new AtomicInteger(0);
        StringBuilder sb = new StringBuilder()
                .append("BLACK").append("       ")
                .append("WHITE").append("\n")
                .append("-----").append("       ")
                .append("-----").append("\n");

        turnList.stream().forEach(turn -> {
            int ply = i.incrementAndGet();
            if (ply % 2 == 1) {
                sb.append((ply + 1) / 2).append(". ");
                sb.append(StringUtils.rightPad(turn.pretty(), 11));
            }
            else {
                sb.append(turn.pretty()).append('\n');
            }
        });
        return sb.toString();
    }

    private String prettyCompact() {
        AtomicInteger i = new AtomicInteger(0);

        return turnList.stream().map(turn -> {
            int ply = i.incrementAndGet();
            StringBuilder sb = new StringBuilder();

            if (ply % 2 == 1) {
                sb.append((ply + 1) / 2).append(". ");
            }
            sb.append(turn.pretty()).append(" ");
            return sb.toString();
        }).collect(Collectors.joining());
    }
}
