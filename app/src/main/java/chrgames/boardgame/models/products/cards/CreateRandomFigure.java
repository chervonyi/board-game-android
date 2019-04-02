package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;
import java.util.Random;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;
import chrgames.boardgame.models.products.Figure;
import chrgames.boardgame.models.products.figures.Master;
import chrgames.boardgame.models.products.figures.Predator;
import chrgames.boardgame.models.products.figures.Soldier;
import chrgames.boardgame.models.products.figures.Source;
import chrgames.boardgame.models.products.figures.Stone;

public class CreateRandomFigure extends Card {

    public CreateRandomFigure() {
        cost = 8;

        level = Level.EASY;

        productView = "new_figure";
    }

    @Override
    public String getInformation() {
        return "Create a random figure for $" + cost;
    }

    @Override
    public String getSubmitQuestion() {
        return "Use";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        ArrayList<Integer> legalCells = new ArrayList<>();

        ArrayList<Cell> board = game.getBoard();

        for (int i = 0; i < Game.CELLS; i++) {
            // If empty and does not belongs to enemy's base
            if (board.get(i).isEmpty() && !game.getBase(game.getEnemyStateOf(user)).doesBelong(i)) {
                legalCells.add(i);
            }
        }

        if (legalCells.size() == 0) { return false; }

        Kind[] figureTypes = new Kind[] {
                Kind.Master,
                Kind.Predator,
                Kind.Soldier,
                Kind.Source,
                Kind.Stone
        };

        Figure randomFigure;
        Random random = new Random();

        switch (figureTypes[random.nextInt(figureTypes.length)]) {
            case Master: randomFigure = new Master();
                break;
            case Predator: randomFigure = new Predator();
                break;
            case Soldier: randomFigure = new Soldier();
                break;
            case Source: randomFigure = new Source();
                break;
            case Stone: randomFigure = new Stone();
                break;
            default: return false;
        }

        int randomPosition = legalCells.get(random.nextInt(legalCells.size()));

        // Set random figure with current user's state to random position on the board
        board.get(randomPosition).setFigure(randomFigure, user);

        return true;
    }
}
