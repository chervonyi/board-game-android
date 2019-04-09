package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;
import chrgames.boardgame.models.products.figures.Source;

public class Reversal extends Card {

    public Reversal() {
        cost = 50;

        level = Level.HARD;

        productView = "reversal";
    }

    @Override
    public String getInformation() {
        return "Swap ALL figures with your opponent";
    }

    @Override
    public String getSubmitQuestion() {
        return "Use";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        ArrayList<Cell> board = game.getBoard();

        Cell tmpCell;
        int reversePos;

        // Change position of figures
        for (int i = 0; i < Game.CELLS / 2; i++) {
            reversePos = Game.CELLS - 1 - i;

            tmpCell = new Cell(board.get(i));

            board.get(i).setFigure(board.get(reversePos));
            board.get(reversePos).setFigure(tmpCell);
        }

        ArrayList<Cell> allianceFigures = game.getAllFiguresOf(Game.PlayerState.ALLIANCE);
        ArrayList<Cell> enemyFigures = game.getAllFiguresOf(Game.PlayerState.ENEMY);

        int countFinalFigures = 0;

        for (Cell alliance : allianceFigures) {

            if (alliance.isEndingFigure()) {
                countFinalFigures++;
            }

            alliance.setOwner(Game.PlayerState.ENEMY);
        }

        game.setNewIncome(Game.PlayerState.ENEMY, countFinalFigures * Source.INCOME_FOR_EACH_FIGURE);

        // All enemy's figures set as alliance's
        for (Cell enemy : enemyFigures) {

            if (enemy.isEndingFigure()) {
                countFinalFigures++;
            }

            enemy.setOwner(Game.PlayerState.ALLIANCE);
        }

        game.setNewIncome(Game.PlayerState.ALLIANCE, countFinalFigures * Source.INCOME_FOR_EACH_FIGURE);


        return true;
    }

}
