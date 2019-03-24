package chrgames.boardgame.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SoldierTest {

    private Soldier soldier = new Soldier();

    @Test
    public void getAvailableCellsToMoveFrom() {

        int input = 49;
        ArrayList<Integer> output;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(43, 44, 48));

        output = soldier.getAvailableCellsToMoveFrom(input);

        assertEquals(expected, output);
    }
}