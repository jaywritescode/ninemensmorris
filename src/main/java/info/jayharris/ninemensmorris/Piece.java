package info.jayharris.ninemensmorris;

public enum Piece {

    BLACK("●"), WHITE("○");

    String pretty;

    Piece(String pretty) {
        this.pretty = pretty;
    }

    public String pretty() {
        return pretty;
    }

    public Piece opposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}
