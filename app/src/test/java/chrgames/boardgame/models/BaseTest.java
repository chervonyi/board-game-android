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

        output = enemyBase.getListOfCells();
        //output = allianceBase.getListOfCells();

        assertEquals(expected, output);
    }

    @Test
    public void getRandomYourCell() {

        ArrayList<Integer> vault = new ArrayList<>();
        ArrayList<Integer> expected = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            //vault.add(enemyBase.getRandomYourCell());
            vault.add(allianceBase.getRandomYourCell());
        }

        assertEquals(expected, vault);
    }
}