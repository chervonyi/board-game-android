package chrgames.boardgame.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import chrgames.boardgame.models.products.figures.Predator;

import static org.junit.Assert.*;

public class PredatorTest {

    private Predator predator = new Predator();

    @Test
    public void getAvailableCellsToMoveFrom() {

        int input = 47;
        ArrayList<Integer> output;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(41, 43));

        output = predator.getAvailableCellsToMoveFrom(input);

        assertEquals(expected, output);
    }
}