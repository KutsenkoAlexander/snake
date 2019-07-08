package ua.kaj.snake.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TickerGameTest {

    @Test
    public void gameLoop(){
        TickerGame tickerGame = new TickerGame();
        int count = 0;
        while(count < 20) {
            tickerGame.gameLoop();
            count++;
            System.out.println(count);
        }
    }
}
