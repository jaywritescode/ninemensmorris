package info.jayharris.ninemensmorris;

public abstract class BaseMove implements Move {

    BasePlayer player;

    BaseMove(BasePlayer player) {
        this.player = player;
    }
}
