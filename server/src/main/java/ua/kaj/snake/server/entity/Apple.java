package ua.kaj.snake.server.entity;

import org.slf4j.Logger;
import ua.kaj.snake.server.enums.SizeField;
import ua.kaj.snake.server.exceptions.IllegalAppleCoordinatesException;

import java.util.Random;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.server.enums.DotSize.DOT_SIZE;

public class Apple {
    private static final Logger log = getLogger(Apple.class);

    private static int x; //30 dots in horizontal
    private static int y; //40 dots in vertical

    private static Apple apple = new Apple();

    private Apple() {
    }

    public static Apple getInstance() {
        create();
        return apple;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private static void create() {
        Apple.x = createCoordinates(30, SizeField.SIZE_X);
        Apple.y = createCoordinates(40, SizeField.SIZE_Y);
    }

    private static int createCoordinates(int pointsInLine, SizeField sizeField) {
        Random r = new Random();
        int value = r.nextInt(pointsInLine) * DOT_SIZE.getValue();
        if (value > sizeField.getValue()) {
            try {
                throw new IllegalAppleCoordinatesException(value+ " is not in the rang 0-"+sizeField.getValue());
            } catch (IllegalAppleCoordinatesException e) {
                log.warn(e.getMessage());
                value = r.nextInt(pointsInLine - 1) * DOT_SIZE.getValue();
            }
        }
        return value;
    }
}
