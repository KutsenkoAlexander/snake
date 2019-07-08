package ua.kaj.snake.server.dto;

import ua.kaj.snake.server.enums.Direction;

import java.util.List;

public class SnakeDto {
    private List<Integer> x;
    private List<Integer> y;
    private Direction direction;

    public SnakeDto(List<Integer> x, List<Integer> y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public List<Integer> getX() {
        return x;
    }

    public List<Integer> getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "SnakeDto{x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
