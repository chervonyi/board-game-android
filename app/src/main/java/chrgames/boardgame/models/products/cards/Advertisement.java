package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class Advertisement extends Card {

    private final int REWARD = 15;

    public Advertisement() {
        cost = 0;

        level = Level.EASY;

        productView = "advertisement";
    }

    @Override
    public String getInformation() {
        return null;
    }

    @Override
    public String getSubmitQuestion() {
        return null;
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {
        // TODO: Register app and add reward video
        return false;
    }
}
