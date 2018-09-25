package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.BasePlayer;

abstract class BaseMove implements Move {

    BasePlayer player;

    BaseMove(BasePlayer player) {
        this.player = player;
    }

    public abstract String pretty();
}
