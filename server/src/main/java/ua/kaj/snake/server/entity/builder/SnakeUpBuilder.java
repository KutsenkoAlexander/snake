package ua.kaj.snake.server.entity.builder;

import ua.kaj.snake.server.enums.Direction;

public class SnakeUpBuilder extends SnakeBuilder {
    private static final int HEAD_X_POSITION = 400;
    private static final int HEAD_Y_POSITION = 496;

    @Override
    void buildDirection() {
        snake.setDirection(Direction.UP);
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
