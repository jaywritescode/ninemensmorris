package info.jayharris.ninemensmorris;

public abstract class BasePlayer {

    private final Piece piece;

    protected BasePlayer(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
