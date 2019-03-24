package chrgames.boardgame.models;

import org.junit.Test;
import org.junit.Assert;


import static org.junit.Assert.*;

import java.util.ArrayList;

public class FigureTest {

    private Figure figure = new Figure() {
        @Override
        public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {
            return new ArrayList<>();
        }
    };

    @Test
    public void getXY() {
        int input = 50;
        int[] output;
        int[] expected = new int[]{4, 9};

        output = figure.getXY(input);

        Assert.assertArrayEquals(expected, output);
    }

    @Test
    public void getPosition() {
        int x = 4;
        int y = 9;
        int output;
        int expected = 49;

        output = figure.getPosition(x, y);

        assertEquals(expected, output);
    }

    @Test
    public void isExist() {
        int x = 2;
        int y = -1;
        boolean output;
        boolean expected = false;

        output = figure.isExist(x, y);

        assertEquals(expected, output);
    }
}