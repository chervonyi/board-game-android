package chrgames.boardgame.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class BotTest {

    private Bot bot = new Bot(Bot.Level.EASY);

    @Test
    public void getPriority() {
        int input = 0;
        int output;
        int expected = 0;

        output = bot.getPriority(input);

        assertEquals(expected, output);
    }
}