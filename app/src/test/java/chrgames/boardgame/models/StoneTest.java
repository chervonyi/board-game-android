package chrgames.boardgame.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import chrgames.boardgame.models.products.figures.Stone;

import static org.junit.Assert.*;

public class StoneTest {

    private Stone stone = new Stone();

    @Test
    public void getAvailableCellsToMoveFrom() {

        int input = 44;
        ArrayList<Integer> output;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(38, 39, 43, 48, 49));

        output = stone.getAvailableCellsToMoveFrom(input);

        assertEquals(expected, output);
    }
}