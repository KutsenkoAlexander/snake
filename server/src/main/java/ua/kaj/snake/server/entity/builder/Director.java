package ua.kaj.snake.server.entity.builder;

import ua.kaj.snake.server.entity.Snake;
import javax.validation.constraints.NotNull;

public class Director {
    private SnakeBuilder snakeBuilder;

    public void setSnakeBuilder(@NotNull SnakeBuilder snakeBuilder) {
        this.snakeBuilder = snakeBuilder;
    }

    public Snake buildSnake() {
        snakeBuilder.createSnake();
        snakeBuilder.buildDirection();
        snakeBuilder.buildPositionX();
        snakeBuilder.buildPositionY();
        return snakeBuilder.getSnake();
    }
}
