package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.BasePlayer;

public abstract class BaseMove implements Move {

    BasePlayer player;

    BaseMove(BasePlayer player) {
        this.player = player;
    }
}
