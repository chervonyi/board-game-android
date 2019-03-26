package chrgames.boardgame.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BaseTest {

    private Base enemyBase = new Base(Game.BASE_SIZE, Game.PlayerState.ENEMY);
    private Base allianceBase = new Base(Game.BASE_SIZE, Game.PlayerState.ALLIANCE);

    @Test
    public void getListOfCells() {

        ArrayList<Integer> output;
        ArrayList<Integer> expected = new ArrayList<>();

        output = enemyBase.getCells();
        //output = allianceBase.getCells();

        assertEquals(expected, output);
    }

    @Test
    public void getRandomFreeCell() {


    }
}