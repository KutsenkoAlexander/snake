package ua.kaj.snake.server.entity;

import org.slf4j.Logger;
import ua.kaj.snake.server.enums.Direction;
import ua.kaj.snake.server.exceptions.SnakeNotFountException;
import ua.kaj.snake.server.service.GameSession;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.server.enums.Direction.*;
import static ua.kaj.snake.server.enums.DotSize.DOT_SIZE;
import static ua.kaj.snake.server.enums.SizeField.SIZE_X;
import static ua.kaj.snake.server.enums.SizeField.SIZE_Y;

public class Snake {
    private static final Logger log = getLogger(Snake.class);
    private static final int SNAKE_HEAD_POSITION = 0;

    private int id = new Random().nextInt();
    private int size = 3;
    private List<Integer> x; //30 dots width
    private List<Integer> y; //40 dots height
    private Direction direction;

    public Snake() {
        this.x = new ArrayList<>();
        this.y = new ArrayList<>();
    }

    public Snake(int posX, int posY, Direction direction) {
        this();
        setDirection(direction);
        setX(posX);
        setY(posY);
    }

    public int getSize() {
        return size;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }

    public List<Integer> getX() {
        return x;
    }

    public void setX(int posX) {
        for (int i = 0; i < size; i++) {
            if (direction.equals(DOWN)) {
                x.add(i, posX);
            }
            if (direction.equals(UP)) {
                x.add(i, posX);
            }
            if (direction.equals(LEFT)) {
                x.add(i, posX - (i * DOT_SIZE.getValue()));
            }
            if (direction.equals(RIGHT)) {
                x.add(i, posX + (i * DOT_SIZE.getValue()));
            }
        }
    }

    public List<Integer> getY() {
        return y;
    }

    public void setY(int posY) {
        for (int i = 0; i < size; i++) {
            if (direction.equals(DOWN)) {
                y.add(i, posY + (i * DOT_SIZE.getValue()));
            }
            if (direction.equals(UP)) {
                y.add(i, posY - (i * DOT_SIZE.getValue()));
            }
            if (direction.equals(LEFT)) {
                y.add(i, posY);
            }
            if (direction.equals(RIGHT)) {
                y.add(i, posY);
            }
        }
    }

    public void move() {
        for (int i = size - 1; i > 0; i--) {
            x.set(i, x.get(i - 1));
            y.set(i, y.get(i - 1));
        }
        if (direction.equals(DOWN)) {
            y.set(SNAKE_HEAD_POSITION, y.get(SNAKE_HEAD_POSITION) + DOT_SIZE.getValue());
        }
        if (direction.equals(UP)) {
            y.set(SNAKE_HEAD_POSITION, y.get(SNAKE_HEAD_POSITION) - DOT_SIZE.getValue());
        }
        if (direction.equals(LEFT)) {
            x.set(SNAKE_HEAD_POSITION, x.get(SNAKE_HEAD_POSITION) - DOT_SIZE.getValue());
        }
        if (direction.equals(RIGHT)) {
            x.set(SNAKE_HEAD_POSITION, x.get(SNAKE_HEAD_POSITION) + DOT_SIZE.getValue());
        }
    }

    public void eatApple(@NotNull Apple apple) {
        if (x.get(SNAKE_HEAD_POSITION).equals(apple.getX()) && y.get(SNAKE_HEAD_POSITION).equals(apple.getY())) {
            size++;
            x.add(apple.getX());
            y.add(apple.getY());
            Apple.getInstance();
        }
    }

    public void collision(@NotNull GameSession game) throws SnakeNotFountException {
        if (game.isInGame()) {
            Snake rival = game.getPlayers().stream()
                    .filter(p -> !this.equals(p.getSnake()))
                    .map(Player::getSnake)
                    .findFirst()
                    .orElseThrow(SnakeNotFountException::new);
            for (int j = rival.getSize() - 1; j >= 0; j--) {
                if (x.get(SNAKE_HEAD_POSITION).equals(rival.getX().get(j)) && y.get(SNAKE_HEAD_POSITION).equals(rival.getY().get(j))) {
                    game.setInGame(false);
                }
            }

            for (int i = getSize() - 1; i >= 0; i--) {
                if (i > 4 && getX().get(SNAKE_HEAD_POSITION).equals(getX().get(i)) && getY().get(SNAKE_HEAD_POSITION).equals(getY().get(i))) {
                    game.setInGame(false);
                }
            }

            if (x.get(SNAKE_HEAD_POSITION) > SIZE_X.getValue()) {
                game.setInGame(false);
            }
            if (x.get(SNAKE_HEAD_POSITION) <= 0) {
                game.setInGame(false);
            }
            if (y.get(SNAKE_HEAD_POSITION) > SIZE_Y.getValue()) {
                game.setInGame(false);
            }
            if (y.get(SNAKE_HEAD_POSITION) <= 0) {
                game.setInGame(false);
            }
        }
    }

    @Override
    public String toString() {
        return "Snake{" +
                "id=" + id +
                ", size=" + size +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
