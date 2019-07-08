package ua.kaj.snake.server.entity.builder;

import ua.kaj.snake.server.entity.Snake;

public abstract class SnakeBuilder {
    protected Snake snake;

    public void createSnake() {
        this.snake = new Snake();
    }

    abstract void buildDirection();
    abstract void buildPositionX();
    abstract void buildPositionY();

    public Snake getSnake() {
        return snake;
    }
}
