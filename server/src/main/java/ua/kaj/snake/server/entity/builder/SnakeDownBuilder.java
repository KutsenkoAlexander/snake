package ua.kaj.snake.server.entity.builder;

import ua.kaj.snake.server.enums.Direction;

public class SnakeDownBuilder extends SnakeBuilder {
    private static final int HEAD_X_POSITION = 48;
    private static final int HEAD_Y_POSITION = 80;

    @Override
    void buildDirection() {
        snake.setDirection(Direction.DOWN);
    }

    @Override
    void buildPositionX() {
        snake.setX(HEAD_X_POSITION);
    }

    @Override
    void buildPositionY() {
        snake.setY(HEAD_Y_POSITION);
    }
}
