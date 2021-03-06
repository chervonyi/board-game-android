package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class Advertisement extends Card {

    private final int REWARD = 30;

    public Advertisement() {
        cost = 0;

        level = Level.EASY;

        productView = "advertisement";
    }

    @Override
    public String getInformation() {
        return "Watch video ads to get extra $" + REWARD + "!";
    }

    @Override
    public String getSubmitQuestion() {
        return "Watch";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        game.showRewardedVideo(REWARD);

        return false;
    }
}
