package ua.kaj.snake.server.entity;

import org.junit.Before;
import org.junit.Test;
import ua.kaj.snake.server.dto.SnakeDto;
import ua.kaj.snake.server.enums.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SnakeTest {
    private Snake snake;
    private SnakeDto snakeDto;

    @Before
    public void init() {
        snake = new Snake(40, 48, Direction.DOWN);

        List<Integer> xDtos = new ArrayList<>();
        xDtos.add(40);
        xDtos.add(40);
        xDtos.add(40);
        List<Integer> yDtos = new ArrayList<>();
        yDtos.add(80);
        yDtos.add(64);
        yDtos.add(48);


        snakeDto = new SnakeDto(xDtos, yDtos, snake.getDirection());
    }

    @Test
    public void testMove() {
        for(int i = 0; i < 5; i++) {
            snake.move();
            System.out.println(snake);
            System.out.println(snakeDto);
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
