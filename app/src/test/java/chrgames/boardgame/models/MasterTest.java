package chrgames.boardgame.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import chrgames.boardgame.models.products.figures.Master;

import static org.junit.Assert.*;

public class MasterTest {

    private Master master = new Master();

    @Test
    public void getAvailableCellsToMoveFrom() {

        int input = 49;
        ArrayList<Integer> output;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(37, 38, 39, 42, 43, 44, 47, 48));

        output = master.getAvailableCellsToMoveFrom(input);

        assertEquals(expected, output);
    }
}